/*
 * Copyright (c) 2020-2025, Sreeni Viswanadha <sreeni@viswanadha.net>.
 * Copyright (c) 2024-2025, Marc Mazas <mazas.marc@gmail.com>.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 *     * Redistributions of source code must retain the above copyright notice,
 *       this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above copyright
 *       notice, this list of conditions and the following disclaimer in the
 *       documentation and/or other materials provided with the distribution.
 *     * Neither the names of the copyright holders nor the names of its
 *       contributors may be used to endorse or promote products derived from
 *       this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF
 * THE POSSIBILITY OF SUCH DAMAGE.
 */
package org.javacc.jjdoc;

import java.util.Hashtable;
import org.javacc.parser.CppCodeProduction;
import org.javacc.parser.Expansion;
import org.javacc.parser.JavaCodeProduction;
import org.javacc.parser.NonTerminal;
import org.javacc.parser.NormalProduction;
import org.javacc.parser.RegularExpression;
import org.javacc.parser.TokenProduction;

/**
 * Generator for the BNF representation of the grammar in the HTML 3.2 format.
 */
public class HTMLGenerator extends TextGenerator {
  
  /** A counter for ids. */
  int id = 1;
  
  /** The ids map. */
  final Hashtable<String, String> id_map = new Hashtable<>();
  
  /**
   * Constructor with parameters.
   * 
   * @param context - the JJDoc context
   * @param jjdoc - the JJDoc tool
   */
  HTMLGenerator(final JJDocContext context, final JJDoc jjdoc) {
    super(context, jjdoc);
  }
  
  /**
   * @param nt - a non terminal
   * @return its id
   */
  String get_id(final String nt) {
    String s = id_map.get(nt);
    if (s == null) {
      s = "prod" + id++;
      id_map.put(nt, s);
    }
    return s;
  }
  
  /**
   * Outputs a string with a new line.
   * 
   * @param s - a string
   */
  void println(final String s) {
    print(s + "\n");
  }
  
  @Override
  public void text(final String s) {
    String str = "";
    for (int i = 0; i < s.length(); ++i) {
      if (s.charAt(i) == '<') {
        str += "&lt;";
      } else if (s.charAt(i) == '>') {
        str += "&gt;";
      } else if (s.charAt(i) == '&') {
        str += "&amp;";
      } else {
        str += s.charAt(i);
      }
    }
    print(str);
  }
  
  @Override
  public void print(final String s) {
    ostr.print(s);
  }
  
  @Override
  public void documentStart() {
    ostr = create_output_stream();
    println("<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 3.2//EN\">");
    println("<HTML>");
    println("<HEAD>");
    if (!"".equals(context.getCSS())) {
      println("<LINK REL=\"stylesheet\" type=\"text/css\" href=\"" + context.getCSS() + "\"/>");
    }
    if (jjdoc.input_file != null) {
      println("<TITLE>BNF for " + jjdoc.input_file + "</TITLE>");
    } else {
      println("<TITLE>A BNF grammar by JJDoc</TITLE>");
    }
    println("</HEAD>");
    println("<BODY>");
    println("<H1 ALIGN=CENTER>BNF for " + jjdoc.input_file + "</H1>");
  }
  
  @Override
  public void documentEnd() {
    println("</BODY>");
    println("</HTML>");
    ostr.close();
  }
  
  @Override
  public void specialTokens(final String s) {
    println(" <!-- Special token -->");
    println(" <TR>");
    println("  <TD>");
    println("<PRE>");
    print(s);
    println("</PRE>");
    println("  </TD>");
    println(" </TR>");
  }
  
  @Override
  public void handleTokenProduction(final String text, final TokenProduction tp) {
    println(" <!-- Token -->");
    println(" <TR>");
    println("  <TD>");
    println("   <PRE>");
    text(text);
    println("   </PRE>");
    println("  </TD>");
    println(" </TR>");
  }
  
  @Override
  public void nonterminalsStart() {
    println("<H2 ALIGN=CENTER>NON-TERMINALS</H2>");
    if (context.getOneTable()) {
      println("<TABLE>");
    }
  }
  
  @Override
  public void nonterminalsEnd() {
    if (context.getOneTable()) {
      println("</TABLE>");
    }
  }
  
  @Override
  public void tokensStart() {
    println("<H2 ALIGN=CENTER>TOKENS</H2>");
    println("<TABLE>");
  }
  
  @Override
  public void tokensEnd() {
    println("</TABLE>");
  }
  
  @Override
  public void javacode(final JavaCodeProduction jp) {
    productionStart(jp);
    println("<I>java code</I></TD></TR>");
    productionEnd(jp);
  }
  
  @Override
  public void cppcode(final CppCodeProduction cp) {
    productionStart(cp);
    println("<I>cpp code</I></TD></TR>");
    productionEnd(cp);
  }
  
  @Override
  public void productionStart(final NormalProduction np) {
    if (!context.getOneTable()) {
      println("");
      println("<TABLE ALIGN=CENTER>");
      println("<CAPTION><STRONG>" + np.getLhs() + "</STRONG></CAPTION>");
    }
    println("<TR>");
    println("<TD ALIGN=RIGHT VALIGN=BASELINE><A NAME=\"" + get_id(np.getLhs()) + "\">" + np.getLhs()
        + "</A></TD>");
    println("<TD ALIGN=CENTER VALIGN=BASELINE>::=</TD>");
    print("<TD ALIGN=LEFT VALIGN=BASELINE>");
  }
  
  @Override
  public void productionEnd(final NormalProduction np) {
    if (!context.getOneTable()) {
      println("</TABLE>");
      println("<HR>");
    }
  }
  
  @Override
  public void expansionStart(final Expansion e, final boolean first) {
    if (!first) {
      println("<TR>");
      println("<TD ALIGN=RIGHT VALIGN=BASELINE></TD>");
      println("<TD ALIGN=CENTER VALIGN=BASELINE>|</TD>");
      print("<TD ALIGN=LEFT VALIGN=BASELINE>");
    }
  }
  
  @Override
  public void expansionEnd(final Expansion e, final boolean first) {
    println("</TD>");
    println("</TR>");
  }
  
  @Override
  public void nonTerminalStart(final NonTerminal nt) {
    print("<A HREF=\"#" + get_id(nt.getName()) + "\">");
  }
  
  @Override
  public void nonTerminalEnd(final NonTerminal nt) {
    print("</A>");
  }
  
  @Override
  public void reStart(final RegularExpression r) {}
  
  @Override
  public void reEnd(final RegularExpression r) {}
}
