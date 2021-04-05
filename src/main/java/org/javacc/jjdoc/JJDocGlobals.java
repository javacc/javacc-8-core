/* Copyright (c) 2006, Sun Microsystems, Inc.
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
 *     * Neither the name of the Sun Microsystems, Inc. nor the names of its
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

/**
 * Global variables for JJDoc.
 *
 */
public class JJDocGlobals {

  /**
   * The name of the input file.
   */
  public static String    input_file;
  /**
   * The name of the output file.
   */
  public static String    output_file;

  /**
   * The Generator to create output with.
   */
  public static Generator generator;

  /**
   * The commandline option is either TEXT or not, but the generator might have
   * been set to some other Generator using the setGenerator method.
   *
   * @return the generator configured in options or set by setter.
   */
  public static Generator getGenerator(JJDocContext context) {
    if (JJDocGlobals.generator == null) {
      if (context.getText()) {
        JJDocGlobals.generator = new TextGenerator(context);
      } else if (context.getBNF()) {
        JJDocGlobals.generator = new BNFGenerator(context);
      } else if (context.getXText()) {
          JJDocGlobals.generator = new XTextGenerator(context);
      } else if (context.getJCC()) {
          JJDocGlobals.generator = new JCCGenerator(context);
      } else {
        JJDocGlobals.generator = new HTMLGenerator(context);
      }
    } else {
      if (context.getText()) {
        if (JJDocGlobals.generator instanceof HTMLGenerator) {
          JJDocGlobals.generator = new TextGenerator(context);
        }
      } else if (context.getBNF()) {
        JJDocGlobals.generator = new BNFGenerator(context);
      } else if (context.getXText()) {
          JJDocGlobals.generator = new XTextGenerator(context);
      } else if (context.getJCC()) {
          JJDocGlobals.generator = new JCCGenerator(context);
      } else {
        if (JJDocGlobals.generator instanceof TextGenerator) {
          JJDocGlobals.generator = new HTMLGenerator(context);
        }
      }
    }
    return JJDocGlobals.generator;
  }

  /**
   * Log informational messages.
   *
   * @param message the message to log
   */
  public static void info(JJDocContext context, String message) {
    JJDocGlobals.getGenerator(context).info(message);
  }

  /**
   * Log error messages.
   *
   * @param message the message to log
   */
  public static void error(JJDocContext context, String message) {
    JJDocGlobals.getGenerator(context).error(message);
  }


}
