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
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import org.javacc.parser.CppCodeProduction;
import org.javacc.parser.Expansion;
import org.javacc.parser.JavaCodeProduction;
import org.javacc.parser.Lookahead;
import org.javacc.parser.NonTerminal;
import org.javacc.parser.NormalProduction;
import org.javacc.parser.RegularExpression;
import org.javacc.parser.TokenProduction;

/**
 * Generator for the BNF representation of the grammar in the text(ual) format.
 */
public class TextGenerator implements Generator {
  
  /** The JJDoc context. */
  JJDocContext context;
  
  /** The JJDoc tool. */
  JJDoc jjdoc;
  
  /** The print writer. */
  PrintWriter ostr;
  
  /**
   * Constructor with parameters.
   * 
   * @param context - the JJDoc context
   * @param jjdoc - the JJDoc tool
   */
  TextGenerator(final JJDocContext context, final JJDoc jjdoc) {
    this.context = context;
    this.jjdoc = jjdoc;
  }
  
  /**
   * Create an output stream for the generated Jack code. Try to open a file based on the name of
   * the parser, but if that fails use the standard output stream.
   * 
   * @return a print writer
   */
  PrintWriter create_output_stream() {
    if (context.getOutputFile().equals("")) {
      
      if (jjdoc.input_file.equals("standard input")) {
        ostr = new PrintWriter(new OutputStreamWriter(System.out));
        return ostr;
      }
      
      String ext = ".html";
      if (context.getText()) {
        ext = ".txt";
      } else if (context.getXText()) {
        ext = ".xtext";
      }
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
      ostr = new PrintWriter(new FileWriter(jjdoc.output_file_path));
    }
    catch (final IOException e) {
      error("JJDoc: can't open output stream on file " + jjdoc.output_file_path
          + ".  Using standard output.");
      ostr = new PrintWriter(new OutputStreamWriter(System.out));
    }
    
    return ostr;
  }
  
  @Override
  public void text(final String s) {
    print(s);
  }
  
  @Override
  public void print(final String s) {
    ostr.print(s);
  }
  
  @Override
  public void documentStart() {
    ostr = create_output_stream();
    ostr.print("\nDOCUMENT START\n");
  }
  
  @Override
  public void documentEnd() {
    ostr.print("\nDOCUMENT END\n");
    ostr.close();
  }
  
  @Override
  public void specialTokens(final String s) {
    ostr.print(s);
  }
  
  @Override
  public void nonterminalsStart() {
    text("NON-TERMINALS\n");
  }
  
  @Override
  public void nonterminalsEnd() {}
  
  @Override
  public void tokensStart() {
    text("TOKENS\n");
  }
  
  @Override
  public void handleTokenProduction(final String text, final TokenProduction tp) {
    text(text);
  }
  
  @Override
  public void tokensEnd() {}
  
  @Override
  public void javacode(final JavaCodeProduction jp) {
    productionStart(jp);
    text("java code");
    productionEnd(jp);
  }
  
  @Override
  public void cppcode(final CppCodeProduction cp) {
    productionStart(cp);
    text("c++ code");
    productionEnd(cp);
  }
  
  @Override
  public void productionStart(final NormalProduction np) {
    ostr.print("\t" + np.getLhs() + "\t:=\t");
  }
  
  @Override
  public void productionEnd(final NormalProduction np) {
    ostr.print("\n");
  }
  
  @Override
  public void expansionStart(final Expansion e, final boolean first) {
    if (!first) {
      ostr.print("\n\t\t|\t");
    }
  }
  
  @Override
  public void expansionEnd(final Expansion e, final boolean first) {}
  
  @Override
  public void nonTerminalStart(final NonTerminal nt) {}
  
  @Override
  public void nonTerminalEnd(final NonTerminal nt) {}
  
  @Override
  public void reStart(final RegularExpression r) {}
  
  @Override
  public void reEnd(final RegularExpression r) {}
  
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
  
  @Override
  public void lookAheadStart(final Lookahead l) {}
  
  @Override
  public void lookAheadEnd(final Lookahead l) {}
}
