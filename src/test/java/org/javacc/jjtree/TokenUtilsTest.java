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
package org.javacc.jjtree;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

import junit.framework.TestCase;

public final class TokenUtilsTest extends TestCase {

  /** A latin letter, the euro sign, the line and paragraph separators, an emoji. */
  private static final String TEXT = "\u00e9\u20ac\u2028\u2029\ud83d\ude00";

  public void testAscii() {
    final String str = "~[\"a\",\"\\n\"]\t/* ~ */\r\n\f";
    assertEquals(str, TokenUtils.addUnicodeEscapes(str));
    assertEquals(str, TokenUtils.addUnicodeEscapes(str, StandardCharsets.US_ASCII));
  }

  public void testControlCharacters() {
    assertEquals("a\\u0000b\\u001fc", TokenUtils.addUnicodeEscapes("a\u0000b\u001fc"));
  }

  public void testEncodingWithAllCharacters() {
    assertEquals(TEXT, TokenUtils.addUnicodeEscapes(TEXT, StandardCharsets.UTF_8));
  }

  public void testEncodingWithSomeCharacters() {
    assertEquals(
        "\u00e9\\u20ac\\u2028\\u2029\\ud83d\\ude00",
        TokenUtils.addUnicodeEscapes(TEXT, StandardCharsets.ISO_8859_1));
  }

  public void testEncodingWithoutTheCharacters() {
    assertEquals(
        "\\u00e9\\u20ac\\u2028\\u2029\\ud83d\\ude00",
        TokenUtils.addUnicodeEscapes(TEXT, StandardCharsets.US_ASCII));
  }

  public void testUnpairedSurrogate() {
    assertEquals("\\ud83d", TokenUtils.addUnicodeEscapes("\ud83d", StandardCharsets.UTF_8));
  }

  /** Whatever the platform encoding is, what is written to a file is read back from it. */
  public void testPlatformEncoding() {
    final Charset charset = Charset.defaultCharset();
    final String str = TokenUtils.addUnicodeEscapes(TEXT);
    assertEquals(str, new String(str.getBytes(charset), charset));
  }
}
