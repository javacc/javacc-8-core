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
package org.javacc.parser;

import java.io.File;
import junit.framework.TestCase;

/**
 * Test cases on Options.
 *
 * @author Kees Jan Koster &lt;kjkoster@kjkoster.org&gt;
 */
public final class OptionsTest extends TestCase {

  public void testDefaults() {
    Options.init();
    final Context context = new Context();

    assertEquals(true, Options.getBuildParser());
    assertEquals(true, Options.getBuildTokenManager());
    assertEquals(false, Options.getCacheTokens());
    assertEquals(false, Options.getCommonTokenAction());
    assertEquals(false, Options.getDebugLookahead());
    assertEquals(false, Options.getDebugParser());
    assertEquals(false, Options.getDebugTokenManager());
    assertEquals(true, Options.getErrorReporting());
    assertEquals(false, Options.getForceLaCheck());
    assertEquals(false, Options.getIgnoreCase());
    assertEquals(false, Options.getJavaUnicodeEscape());
    assertEquals(true, Options.getKeepLineColumn());
    assertEquals(true, Options.getSanityCheck());
    assertEquals(true, Options.getStatic());
    assertEquals(false, Options.getUnicodeInput());
    assertEquals(false, Options.getUserCharStream());
    assertEquals(false, Options.getUserTokenManager());
    assertEquals(false, Options.getTokenManagerUsesParser());

    assertEquals(2, Options.getChoiceAmbiguityCheck());
    assertEquals(1, Options.getLookahead());
    assertEquals(1, Options.getOtherAmbiguityCheck());

    //    assertEquals("1.5", Options.getJdkVersion());
    assertEquals(new File("."), Options.getOutputDirectory());
    assertEquals("", Options.getTokenExtends());
    assertEquals("", Options.getTokenFactory());
    assertEquals(System.getProperties().get("file.encoding"), Options.getGrammarEncoding());

    assertEquals(0, context.errors().get_warning_count());
    assertEquals(0, context.errors().get_error_count());
    assertEquals(0, context.errors().get_parse_error_count());
    assertEquals(0, context.errors().get_semantic_error_count());
  }

  public void testSetBooleanOption() {
    Options.init();
    final Context context = new Context();

    assertEquals(true, Options.getStatic());
    Options.processCmdLineOption("-NOSTATIC");
    assertEquals(false, Options.getStatic());

    assertEquals(false, Options.getJavaUnicodeEscape());
    Options.processCmdLineOption("-JAVA_UNICODE_ESCAPE:true");
    assertEquals(true, Options.getJavaUnicodeEscape());

    assertEquals(true, Options.getSanityCheck());
    Options.processCmdLineOption("-SANITY_CHECK=false");
    assertEquals(false, Options.getSanityCheck());

    assertEquals(0, context.errors().get_warning_count());
    assertEquals(0, context.errors().get_error_count());
    assertEquals(0, context.errors().get_parse_error_count());
    assertEquals(0, context.errors().get_semantic_error_count());
  }

  public void testIntBooleanOption() {
    Options.init();
    final Context context = new Context();

    assertEquals(1, Options.getLookahead());
    Options.processCmdLineOption("LOOKAHEAD=2");
    assertEquals(2, Options.getLookahead());
    assertEquals(0, context.errors().get_warning_count());
    Options.processCmdLineOption("LOOKAHEAD=0");
    assertEquals(2, Options.getLookahead());
    assertEquals(0, context.errors().get_warning_count());
    Options.processGrammarFileOption(null, null, Options.UO__LOOKAHEAD, new Integer(0), context);
    assertEquals(2, Options.getLookahead());
    assertEquals(1, context.errors().get_warning_count());

    assertEquals(0, context.errors().get_error_count());
    assertEquals(0, context.errors().get_parse_error_count());
    assertEquals(0, context.errors().get_semantic_error_count());
  }

  public void testSetStringOption() {
    Options.init();
    final Context context = new Context();

    assertEquals("", Options.getTokenExtends());
    Options.processCmdLineOption("-TOKEN_EXTENDS=java.lang.Object");
    assertEquals("java.lang.Object", Options.getTokenExtends());
    Options.processGrammarFileOption(null, null, Options.UO__TOKEN_EXTENDS, "Object", context);
    // File option does not override cmd line
    assertEquals("java.lang.Object", Options.getTokenExtends());

    Options.init();

    Options.processGrammarFileOption(null, null, Options.UO__TOKEN_EXTENDS, "Object", context);
    assertEquals("Object", Options.getTokenExtends());
    Options.processCmdLineOption("-TOKEN_EXTENDS=java.lang.Object");
    assertEquals("java.lang.Object", Options.getTokenExtends());
  }

  public void testSetNonexistentOption() {
    Options.init();
    final Context context = new Context();

    assertEquals(0, context.errors().get_warning_count());
    Options.processGrammarFileOption(null, null, "NONEXISTENTOPTION", Boolean.TRUE, context);
    assertEquals(1, context.errors().get_warning_count());

    assertEquals(0, context.errors().get_error_count());
    assertEquals(0, context.errors().get_parse_error_count());
    assertEquals(0, context.errors().get_semantic_error_count());
  }

  public void testSetWrongTypeForOption() {
    Options.init();
    final Context context = new Context();

    assertEquals(0, context.errors().get_warning_count());
    assertEquals(0, context.errors().get_error_count());
    Options.processGrammarFileOption(null, null, Options.UO__STATIC, new Integer(8), context);
    assertEquals(1, context.errors().get_warning_count());

    assertEquals(0, context.errors().get_error_count());
    assertEquals(0, context.errors().get_parse_error_count());
    assertEquals(0, context.errors().get_semantic_error_count());
  }

  public void testNormalize() {
    Options.init();
    final Context context = new Context();

    assertEquals(false, Options.getDebugLookahead());
    assertEquals(false, Options.getDebugParser());

    Options.processCmdLineOption("-DEBUG_LOOKAHEAD=TRUE");
    Options.normalize(context);

    assertEquals(true, Options.getDebugLookahead());
    assertEquals(true, Options.getDebugParser());

    assertEquals(0, context.errors().get_warning_count());
    assertEquals(0, context.errors().get_error_count());
    assertEquals(0, context.errors().get_parse_error_count());
    assertEquals(0, context.errors().get_semantic_error_count());
  }

  public void testOptionsString() throws ParseException {
    Options.init();

    Options.processCmdLineOption("-STATIC=False");
    Options.processCmdLineOption("-IGNORE_CASE=True");
    final String[] options = {Options.UO__STATIC, Options.UO__IGNORE_CASE};
    final String optionString = Options.fmtOptionsArray(options);
    assertEquals("STATIC=false,IGNORE_CASE=true", optionString);
  }
}
