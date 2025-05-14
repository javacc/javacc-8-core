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

/** Describes the input token stream. */
public class Token {

  /**
   * An integer that describes the kind of this token.<br>
   * This numbering system is determined by JavaCCParser, and a table of these numbers is stored in
   * the file ...Constants.java.
   */
  public int kind;

  /** The line number of the first character of this Token. */
  public int beginLine;

  /** The column number of the first character of this Token. */
  public int beginColumn;

  /** The line number of the last character of this Token. */
  public int endLine;

  /** The column number of the last character of this Token. */
  public int endColumn;

  /** The string image of the token. */
  public String image;

  /**
   * A reference to the next regular (non-special) token from the input stream.<br>
   * If this is the last token from the input stream, or if the token manager has not read tokens
   * beyond this one, this field is set to null. <br>
   * This is true only if this token is also a regular token.<br>
   * Otherwise, see below for a description of the contents of this field.
   */
  public Token next;

  /**
   * This field is used to access special tokens that occur prior to this token, but after the
   * immediately preceding regular (non-special) token.<br>
   * If there are no such special tokens, this field is set to null.<br>
   * When there are more than one such special token, this field refers to the last of these special
   * tokens, which in turn refers to the next previous special token through its specialToken field,
   * and so on until the first special token (whose specialToken field is null). <br>
   * The next fields of special tokens refer to other special tokens that immediately follow it
   * (without an intervening regular token). <br>
   * If there is no such token, this field is null.
   */
  public Token specialToken;

  /**
   * An optional attribute value of the Token.<br>
   * Tokens which are not used as syntactic sugar will often contain meaningful values that will be
   * used later on by the compiler or interpreter.<br>
   * This attribute value is often different from the image.<br>
   * Any subclass of Token that actually wants to return a non-null value can override this method
   * as appropriate.
   */
  public Object getValue() {
    return null;
  }

  /** No-argument constructor */
  public Token() {}

  /** Constructs a new token for the specified Image and Kind. */
  private Token(final int kind, final String image) {
    this.kind = kind;
    this.image = image;
  }

  /** equals */
  @Override
  public boolean equals(final Object object) {
    if (object == null) {
      return false;
    }
    if (this == object) {
      return true;
    }
    if (object instanceof String) {
      return object.equals(image);
    }
    return false;
  }

  /** hashCode */
  @Override
  public int hashCode() {
    return image.hashCode();
  }

  /** Returns the image. */
  @Override
  public String toString() {
    return image;
  }

  /**
   * Returns a new Token object, by default. <br>
   * However, if you want, you can create and return subclass objects based on the value of <code>
   * ofKind</code>. <br>
   * Simply add the cases to the switch for all those special cases.<br>
   * For example, if you have a subclass of <code>Token</code> called <code>IDToken</code> that you
   * want to create if <code>ofKind</code> is <code>ID</code>, simply add something like: <code>
   * case MyParserConstants.ID: return new IDToken(ofKind, image);</code> to the following switch
   * statement.<br>
   * Then you can cast <code>matchedToken</code> variable to the appropriate type and use it in your
   * lexical actions.
   */
  static final Token newToken(final int ofKind, final String image) {
    switch (ofKind) {
      default:
        return new Token(ofKind, image);
      case JavaCCParserConstants.RUNSIGNEDSHIFT:
      case JavaCCParserConstants.RSIGNEDSHIFT:
      case JavaCCParserConstants.GT:
        return new GTToken(ofKind, image);
    }
  }

  /** Greater than Token. */
  static class GTToken extends Token {

    int realKind = JavaCCParserConstants.GT;

    private GTToken(final int kind, final String image) {
      super(kind, image);
    }
  }

  public String printTokenOnly(final JavaCCGlobals globals, final boolean escape) {
    String retval = "";
    for (; globals.cline < beginLine; globals.cline++) {
      retval += "\n";
      globals.ccol = 1;
    }
    for (; globals.ccol < beginColumn; globals.ccol++) {
      retval += " ";
    }
    if ((kind == JavaCCParserConstants.STRING_LITERAL)
        || (kind == JavaCCParserConstants.CHARACTER_LITERAL)) {
      retval += escape ? JavaCCGlobals.addUnicodeEscapes(image) : image;
    } else {
      retval += image;
    }
    globals.cline = endLine;
    globals.ccol = endColumn + 1;
    final char last = image.charAt(image.length() - 1);
    if ((last == '\n') || (last == '\r')) {
      globals.cline++;
      globals.ccol = 1;
    }
    return retval;
  }
}
