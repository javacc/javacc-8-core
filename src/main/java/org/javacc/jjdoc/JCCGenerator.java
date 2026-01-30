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

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import org.javacc.parser.CppCodeProduction;
import org.javacc.parser.Expansion;
import org.javacc.parser.JavaCodeProduction;
import org.javacc.parser.Lookahead;
import org.javacc.parser.NonTerminal;
import org.javacc.parser.NormalProduction;
import org.javacc.parser.RegularExpression;
import org.javacc.parser.Token;
import org.javacc.parser.TokenProduction;

/**
 * Generator for the JavaCC representation of the grammar in simplified format.
 */
public class JCCGenerator implements Generator {
  
  /** The JJDoc context. */
  JJDocContext context;
  
  /** The JJDoc tool. */
  JJDoc jjdoc;
  
  /** The print stream. */
  PrintStream ostr;
  
  /**
   * Constructor with parameters.
   * 
   * @param context - the JJDoc context
   * @param jjdoc - the JJDoc tool
   */
  JCCGenerator(final JJDocContext context, final JJDoc jjdoc) {
    this.context = context;
    this.jjdoc = jjdoc;
  }
  
  /**
   * Create an output stream for the generated Jack code. Try to open a file based on the name of
   * the parser, but if that fails use the standard output stream.
   * 
   * @return a print stream
   */
  PrintStream create_output_stream() {
    if (context.getOutputFile().equals("")) {
      
      if (jjdoc.input_file.equals("standard input")) {
        ostr = System.out;
        return ostr;
      }
      
      final String ext = ".bnf";
      final int i = jjdoc.input_file.lastIndexOf('.');
      if (i == -1) {
        jjdoc.output_file_path = jjdoc.input_file + ext;
      } else {
        final String suffix = jjdoc.input_file.substring(i);
        if (suffix.equals(ext)) {
          jjdoc.output_file_path = jjdoc.input_file + ext;
        } else {
          jjdoc.output_file_path = jjdoc.input_file.substring(0, i) + ext;
        }
      }
      final String od = context.getOutputDirectory();
      if (!od.equals("")) {
        final File odFile = new File(od);
        if (!odFile.exists()) {
          odFile.mkdirs();
        }
        jjdoc.output_file_path = od + File.separator + jjdoc.output_file_path;
      } else {
        jjdoc.output_file_path = jjdoc.input_directory + File.separator + jjdoc.output_file_path;
      }
      
    } else {
      
      jjdoc.output_file_path = context.getOutputFile();
      final File ofpFile = new File(jjdoc.output_file_path).getParentFile();
      if (!ofpFile.exists()) {
        ofpFile.mkdirs();
      }
      
    }
    
    try {
      ostr = new PrintStream(jjdoc.output_file_path);
    }
    catch (final IOException e) {
      error("JJDoc: can't open output stream on file " + jjdoc.output_file_path
          + ".  Using standard output.");
      ostr = System.out;
    }
    
    return ostr;
  }
  
  /**
   * Outputs a new line.
   */
  void println() {
    ostr.println();
  }
  
  /**
   * Outputs a string with a new line.
   * 
   * @param s - a string
   */
  void println(final String s) {
    ostr.println(s);
  }
  
  /**
   * Outputs an integer.
   * 
   * @param i - an integer
   */
  void print(final int i) {
    ostr.print(i);
  }
  
  /**
   * Outputs an integer with a new line.
   * 
   * @param i - an integer
   */
  @SuppressWarnings("unused")
  void println(final int i) {
    ostr.println(i);
  }
  
  @Override
  public void text(final String s) {
    if (!((s.length() == 1) && ((s.charAt(0) == '\n') || (s.charAt(0) == '\r')))) {
      print(s);
    }
  }
  
  @Override
  public void print(final String s) {
    ostr.print(s);
  }
  
  @Override
  public void documentStart() {
    ostr = create_output_stream();
    println("PARSER_BEGIN(ChangeMe)");
    println("PARSER_END(ChangeMe)");
    println();
  }
  
  @Override
  public void documentEnd() {
    ostr.close();
  }
  
  @Override
  public void specialTokens(final String s) {}
  
  @Override
  public void handleTokenProduction(final String text, final TokenProduction tp) {
    if (tp.firstToken == null) {
      return;
    }
    int line = 1;
    for (Token token = tp.firstToken; token != tp.lastToken; token = token.next) {
      if (token.beginLine > line) {
        println();
        line = token.beginLine;
      }
      print(token.toString());
    }
    println();
    println("}");
  }
  
  @Override
  public void nonterminalsStart() {}
  
  @Override
  public void nonterminalsEnd() {}
  
  @Override
  public void tokensStart() {}
  
  @Override
  public void tokensEnd() {}
  
  @Override
  public void javacode(final JavaCodeProduction jp) {}
  
  @Override
  public void cppcode(final CppCodeProduction cp) {}
  
  @Override
  public void lookAheadStart(final Lookahead l) {
    if (l.isExplicit()) {
      print("LOOKAHEAD(");
      print(l.getAmount());
      print(") ");
    }
  }
  
  @Override
  public void lookAheadEnd(final Lookahead l) {}
  
  @Override
  public void productionStart(final NormalProduction np) {
    print("void ");
    println(np.getLhs() + "() : {} {");
  }
  
  @Override
  public void productionEnd(final NormalProduction np) {
    println();
    println("}");
  }
  
  @Override
  public void expansionStart(final Expansion e, final boolean first) {
    print("  ");
  }
  
  @Override
  public void expansionEnd(final Expansion e, final boolean first) {}
  
  @Override
  public void nonTerminalStart(final NonTerminal nt) {}
  
  @Override
  public void nonTerminalEnd(final NonTerminal nt) {
    print("()");
  }
  
  @Override
  public void reStart(final RegularExpression re) {}
  
  @Override
  public void reEnd(final RegularExpression re) {}
  
  @Override
  public void debug(final String message) {
    System.err.println(message);
  }
  
  @Override
  public void info(final String message) {
    System.err.println(message);
  }
  
  @Override
  public void warn(final String message) {
    System.err.println(message);
  }
  
  @Override
  public void error(final String message) {
    System.err.println(message);
  }
}
