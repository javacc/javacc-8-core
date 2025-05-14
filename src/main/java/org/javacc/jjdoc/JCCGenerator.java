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

public class JCCGenerator implements Generator {

  private final JJDocContext context;
  private PrintStream ostr;

  public JCCGenerator(final JJDocContext context) {
    this.context = context;
  }

  private void println() {
    ostr.println();
  }

  private void println(final String s) {
    ostr.println(s);
  }

  private void print(final int i) {
    ostr.print(i);
  }

  @SuppressWarnings("unused")
  private void println(final int i) {
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
  public void handleTokenProduction(final TokenProduction tp) {
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
  public void debug(final String message) {}

  @Override
  public void info(final String message) {}

  @Override
  public void warn(final String message) {}

  @Override
  public void error(final String message) {}

  protected PrintStream create_output_stream() {
    if (context.getOutputFile().equals("")) {
      if (JJDocGlobals.input_file.equals("standard input")) {
        return System.out;
      } else {
        final String ext = ".bnf";
        final int i = JJDocGlobals.input_file.lastIndexOf('.');
        if (i == -1) {
          JJDocGlobals.output_file = JJDocGlobals.input_file + ext;
        } else {
          final String suffix = JJDocGlobals.input_file.substring(i);
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
      ostr = new java.io.PrintStream(JJDocGlobals.output_file);
    } catch (final java.io.IOException e) {
      error(
          "JJDoc: can't open output stream on file "
              + JJDocGlobals.output_file
              + ".  Using standard output.");
      ostr = System.out;
    }

    return ostr;
  }
}
