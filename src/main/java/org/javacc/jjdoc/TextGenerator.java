/*
 * Copyright (c) 2006, Sun Microsystems, Inc. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * * Redistributions of source code must retain the above copyright notice, this
 * list of conditions and the following disclaimer. * Redistributions in binary
 * form must reproduce the above copyright notice, this list of conditions and
 * the following disclaimer in the documentation and/or other materials provided
 * with the distribution. * Neither the name of the Sun Microsystems, Inc. nor
 * the names of its contributors may be used to endorse or promote products
 * derived from this software without specific prior written permission.
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
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */


package org.javacc.jjdoc;

import org.javacc.parser.CppCodeProduction;
import org.javacc.parser.Expansion;
import org.javacc.parser.JavaCodeProduction;
import org.javacc.parser.Lookahead;
import org.javacc.parser.NonTerminal;
import org.javacc.parser.NormalProduction;
import org.javacc.parser.RegularExpression;
import org.javacc.parser.TokenProduction;

import java.io.PrintWriter;

/**
 * Output BNF in text format.
 */
public class TextGenerator implements Generator {

  protected final JJDocContext context;
  protected PrintWriter        ostr;


  public TextGenerator(JJDocContext context) {
    this.context = context;
  }

  /**
   * {@inheritDoc}
   *
   * @see org.javacc.jjdoc.Generator#text(java.lang.String)
   */
  @Override
  public void text(String s) {
    print(s);
  }

  /**
   * {@inheritDoc}
   *
   * @see org.javacc.jjdoc.Generator#print(java.lang.String)
   */
  @Override
  public void print(String s) {
    ostr.print(s);
  }

  /**
   * {@inheritDoc}
   *
   * @see org.javacc.jjdoc.Generator#documentStart()
   */
  @Override
  public void documentStart() {
    ostr = create_output_stream();
    ostr.print("\nDOCUMENT START\n");
  }

  /**
   * {@inheritDoc}
   *
   * @see org.javacc.jjdoc.Generator#documentEnd()
   */
  @Override
  public void documentEnd() {
    ostr.print("\nDOCUMENT END\n");
    ostr.close();
  }

  /**
   * {@inheritDoc}
   *
   * @see org.javacc.jjdoc.Generator#specialTokens(java.lang.String)
   */
  @Override
  public void specialTokens(String s) {
    ostr.print(s);
  }


  /**
   * {@inheritDoc}
   *
   * @see org.javacc.jjdoc.Generator#nonterminalsStart()
   */
  @Override
  public void nonterminalsStart() {
    text("NON-TERMINALS\n");
  }

  /**
   * {@inheritDoc}
   *
   * @see org.javacc.jjdoc.Generator#nonterminalsEnd()
   */
  @Override
  public void nonterminalsEnd() {}

  /**
   * {@inheritDoc}
   *
   * @see org.javacc.jjdoc.Generator#tokensStart()
   */
  @Override
  public void tokensStart() {
    text("TOKENS\n");
  }

  @Override
  public void handleTokenProduction(TokenProduction tp) {
    String text = JJDoc.getStandardTokenProductionText(tp, context);
    text(text);
  }


  /**
   * {@inheritDoc}
   *
   * @see org.javacc.jjdoc.Generator#tokensEnd()
   */
  @Override
  public void tokensEnd() {}

  /**
   * {@inheritDoc}
   *
   * @see org.javacc.jjdoc.Generator#javacode(org.javacc.parser.JavaCodeProduction)
   */
  @Override
  public void javacode(JavaCodeProduction jp) {
    productionStart(jp);
    text("java code");
    productionEnd(jp);
  }

  /**
   * {@inheritDoc}
   *
   * @see org.javacc.jjdoc.Generator#cppcode(org.javacc.parser.CppCodeProduction)
   */
  @Override
  public void cppcode(CppCodeProduction cp) {
    productionStart(cp);
    text("c++ code");
    productionEnd(cp);
  }

  /**
   * {@inheritDoc}
   *
   * @see org.javacc.jjdoc.Generator#productionStart(org.javacc.parser.NormalProduction)
   */
  @Override
  public void productionStart(NormalProduction np) {
    ostr.print("\t" + np.getLhs() + "\t:=\t");
  }

  /**
   * {@inheritDoc}
   *
   * @see org.javacc.jjdoc.Generator#productionEnd(org.javacc.parser.NormalProduction)
   */
  @Override
  public void productionEnd(NormalProduction np) {
    ostr.print("\n");
  }

  /**
   * {@inheritDoc}
   *
   * @see org.javacc.jjdoc.Generator#expansionStart(org.javacc.parser.Expansion,
   *      boolean)
   */
  @Override
  public void expansionStart(Expansion e, boolean first) {
    if (!first) {
      ostr.print("\n\t\t|\t");
    }
  }

  /**
   * {@inheritDoc}
   *
   * @see org.javacc.jjdoc.Generator#expansionEnd(org.javacc.parser.Expansion,
   *      boolean)
   */
  @Override
  public void expansionEnd(Expansion e, boolean first) {}

  /**
   * {@inheritDoc}
   *
   * @see org.javacc.jjdoc.Generator#nonTerminalStart(org.javacc.parser.NonTerminal)
   */
  @Override
  public void nonTerminalStart(NonTerminal nt) {}

  /**
   * {@inheritDoc}
   *
   * @see org.javacc.jjdoc.Generator#nonTerminalEnd(org.javacc.parser.NonTerminal)
   */
  @Override
  public void nonTerminalEnd(NonTerminal nt) {}

  /**
   * {@inheritDoc}
   *
   * @see org.javacc.jjdoc.Generator#reStart(org.javacc.parser.RegularExpression)
   */
  @Override
  public void reStart(RegularExpression r) {}

  /**
   * {@inheritDoc}
   *
   * @see org.javacc.jjdoc.Generator#reEnd(org.javacc.parser.RegularExpression)
   */
  @Override
  public void reEnd(RegularExpression r) {}

  /**
   * Create an output stream for the generated Jack code. Try to open a file
   * based on the name of the parser, but if that fails use the standard output
   * stream.
   */
  protected PrintWriter create_output_stream() {
    if (context.getOutputFile().equals("")) {
      if (JJDocGlobals.input_file.equals("standard input")) {
        return new java.io.PrintWriter(new java.io.OutputStreamWriter(System.out));
      } else {
        String ext = ".html";

        if (context.getText()) {
          ext = ".txt";
        } else if (context.getXText()) {
          ext = ".xtext";
        }

        int i = JJDocGlobals.input_file.lastIndexOf('.');
        if (i == -1) {
          JJDocGlobals.output_file = JJDocGlobals.input_file + ext;
        } else {
          String suffix = JJDocGlobals.input_file.substring(i);
          if (suffix.equals(ext)) {
            JJDocGlobals.output_file = JJDocGlobals.input_file + ext;
          } else {
            JJDocGlobals.output_file = JJDocGlobals.input_file.substring(0, i) + ext;
          }
        }
      }
    } else {
      JJDocGlobals.output_file = context.getOutputFile();
    }

    try {
      ostr = new java.io.PrintWriter(new java.io.FileWriter(JJDocGlobals.output_file));
    } catch (java.io.IOException e) {
      error("JJDoc: can't open output stream on file " + JJDocGlobals.output_file + ".  Using standard output.");
      ostr = new java.io.PrintWriter(new java.io.OutputStreamWriter(System.out));
    }

    return ostr;
  }

  /**
   * {@inheritDoc}
   *
   * @see org.javacc.jjdoc.Generator#debug(java.lang.String)
   */
  @Override
  public void debug(String message) {
    System.err.println(message);
  }

  /**
   * {@inheritDoc}
   *
   * @see org.javacc.jjdoc.Generator#info(java.lang.String)
   */
  @Override
  public void info(String message) {
    System.err.println(message);
  }

  /**
   * {@inheritDoc}
   *
   * @see org.javacc.jjdoc.Generator#warn(java.lang.String)
   */
  @Override
  public void warn(String message) {
    System.err.println(message);
  }

  /**
   * {@inheritDoc}
   *
   * @see org.javacc.jjdoc.Generator#error(java.lang.String)
   */
  @Override
  public void error(String message) {
    System.err.println(message);
  }
  @Override
  public void lookAheadStart(Lookahead l) {
  }

  @Override
  public void lookAheadEnd(Lookahead l) {
  }
}
