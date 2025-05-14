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

import org.javacc.parser.Context;
import org.javacc.parser.Options;

public class JJDocContext extends Context {

  public JJDocContext() {
    super(new Options());

    Options.resOptions.put("ONE_TABLE", Boolean.TRUE);
    Options.resOptions.put("TEXT", Boolean.FALSE);
    Options.resOptions.put("XTEXT", Boolean.FALSE);
    Options.resOptions.put("BNF", Boolean.FALSE);
    Options.resOptions.put("JCC", Boolean.FALSE);
    Options.resOptions.put("OUTPUT_FILE", "");
    Options.resOptions.put("CSS", "");
  }

  /** Find the one table value. */
  public final boolean getOneTable() {
    return Options.booleanValue("ONE_TABLE");
  }

  /** Find the CSS value. */
  public final String getCSS() {
    return Options.stringValue("CSS");
  }

  /** Find the text value. */
  public final boolean getText() {
    return Options.booleanValue("TEXT");
  }

  public final boolean getXText() {
    return Options.booleanValue("XTEXT");
  }

  /** Find the BNF value. */
  public final boolean getBNF() {
    return Options.booleanValue("BNF");
  }

  /** Find the BNF value. */
  public final boolean getJCC() {
    return Options.booleanValue("JCC");
  }

  /** Find the output file value. */
  public final String getOutputFile() {
    return Options.stringValue("OUTPUT_FILE");
  }
}
