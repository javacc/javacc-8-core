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

import java.util.Iterator;
import org.javacc.parser.CppCodeProduction;
import org.javacc.parser.Expansion;
import org.javacc.parser.JavaCodeProduction;
import org.javacc.parser.NonTerminal;
import org.javacc.parser.NormalProduction;
import org.javacc.parser.RegExprSpec;
import org.javacc.parser.RegularExpression;
import org.javacc.parser.TokenProduction;

/**
 * Generator for the BNF representation of the grammar in the Eclipse XText format.
 */
public class XTextGenerator extends TextGenerator {
  
  /**
   * Constructor with parameters.
   * 
   * @param context - the JJDoc context
   * @param jjdoc - the JJDoc tool
   */
  XTextGenerator(final JJDocContext context, final JJDoc jjdoc) {
    super(context, jjdoc);
  }
  
  @Override
  public void handleTokenProduction(final String text, final TokenProduction tp) {
    
    final StringBuilder sb = new StringBuilder(64);
    
    for (final Iterator<RegExprSpec> it2 = tp.respecs.iterator(); it2.hasNext();) {
      final RegExprSpec res = it2.next();
      
      final String regularExpressionText = jjdoc.emitRE(res.rexp, context);
      sb.append(regularExpressionText);
      
      if (res.nsTok != null) {
        sb.append(" : " + res.nsTok.image);
      }
      
      sb.append("\n");
    }
    
  }
  
  /**
   * Outputs a string with an end of line.
   * 
   * @param s - a string
   */
  void println(final String s) {
    print(s + "\n");
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
    println("grammar " + jjdoc.input_file + " with org.eclipse.xtext.common.Terminals");
    println("import \"http://www.eclipse.org/emf/2002/Ecore\" as ecore");
    println("");
  }
  
  @Override
  public void documentEnd() {
    ostr.close();
  }
  
  @Override
  public void specialTokens(final String s) {
    print(s);
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
  public void productionStart(final NormalProduction np) {}
  
  @Override
  public void productionEnd(final NormalProduction np) {}
  
  @Override
  public void expansionStart(final Expansion e, final boolean first) {}
  
  @Override
  public void expansionEnd(final Expansion e, final boolean first) {
    println(";");
  }
  
  @Override
  public void nonTerminalStart(final NonTerminal nt) {
    print("terminal ");
  }
  
  @Override
  public void nonTerminalEnd(final NonTerminal nt) {
    print(";");
  }
  
  @Override
  public void reStart(final RegularExpression r) {}
  
  @Override
  public void reEnd(final RegularExpression r) {}
}
