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

import org.javacc.parser.CppCodeProduction;
import org.javacc.parser.Expansion;
import org.javacc.parser.JavaCodeProduction;
import org.javacc.parser.Lookahead;
import org.javacc.parser.NonTerminal;
import org.javacc.parser.NormalProduction;
import org.javacc.parser.RegularExpression;
import org.javacc.parser.TokenProduction;

/**
 * A report generator for a grammar.<br>
 * TODO pass it as an abstract class.
 */
public interface Generator {
  
  /**
   * Outputs a string with entity substitution for brackets and ampersands.
   *
   * @param s - the String to output
   */
  void text(String s);
  
  /**
   * Outputs a string.
   *
   * @param s - String to output
   */
  void print(String s);
  
  /** Outputs a document header. */
  void documentStart();
  
  /** Outputs a document footer. */
  void documentEnd();
  
  /**
   * Outputs special tokens.
   *
   * @param s - tokens to output
   */
  void specialTokens(String s);
  
  /**
   * Outputs a token production.
   * 
   * @param text - the standard text of a token production
   * @param tp - the token production
   */
  void handleTokenProduction(String text, TokenProduction tp);
  
  /** Outputs start of non-terminal. */
  void nonterminalsStart();
  
  /** Outputs end of non-terminal. */
  void nonterminalsEnd();
  
  /** Outputs start of tokens. */
  void tokensStart();
  
  /** Outputs end of tokens. */
  void tokensEnd();
  
  /**
   * Outputs comment from a production.
   *
   * @param jp - the JavaCodeProduction to output
   */
  void javacode(JavaCodeProduction jp);
  
  /**
   * Outputs comment from a production.
   *
   * @param cp - the CppCodeProduction to output
   */
  void cppcode(CppCodeProduction cp);
  
  /**
   * Outputs start of a normal production.
   *
   * @param np - the NormalProduction being output
   */
  void productionStart(NormalProduction np);
  
  /**
   * Outputs end of a normal production.
   *
   * @param np - the NormalProduction being output
   */
  void productionEnd(NormalProduction np);
  
  /**
   * Outputs start of an Expansion.
   *
   * @param e - Expansion being output
   * @param first - whether this is the first expansion
   */
  void expansionStart(Expansion e, boolean first);
  
  /**
   * Outputs end of Expansion.
   *
   * @param e - Expansion being output
   * @param first - whether this is the first expansion
   */
  void expansionEnd(Expansion e, boolean first);
  
  /**
   * Outputs start of non-terminal.
   *
   * @param nt - the NonTerminal being output
   */
  void nonTerminalStart(NonTerminal nt);
  
  /**
   * Outputs start of lookahead.
   * 
   * @param l - the lookahead
   */
  void lookAheadStart(Lookahead l);
  
  /**
   * Outputs end of lookahead.
   * 
   * @param l - the lookahead
   */
  void lookAheadEnd(Lookahead l);
  
  /**
   * Outputs end of non-terminal.
   *
   * @param nt - the NonTerminal being output
   */
  void nonTerminalEnd(NonTerminal nt);
  
  /**
   * Outputs start of regular expression.
   *
   * @param re - the RegularExpression being output
   */
  void reStart(RegularExpression re);
  
  /**
   * Outputs end of regular expression.
   *
   * @param re - the RegularExpression being output
   */
  void reEnd(RegularExpression re);
  
  /**
   * Log debug messages.
   *
   * @param message - the string to log
   */
  void debug(String message);
  
  /**
   * Log informational messages.
   *
   * @param message - the string to log
   */
  void info(String message);
  
  /**
   * Log warning messages.
   *
   * @param message - the string to log
   */
  void warn(String message);
  
  /**
   * Log error messages.
   *
   * @param message - the string to log
   */
  void error(String message);
}
