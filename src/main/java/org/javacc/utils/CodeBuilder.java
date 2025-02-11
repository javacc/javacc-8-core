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
 *     * Neither the names of of the copyright holders nor the names of its
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
package org.javacc.utils;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import org.javacc.jjtree.TokenUtils;
import org.javacc.parser.CodeGeneratorSettings;
import org.javacc.parser.Context;
import org.javacc.parser.JavaCCGlobals;
import org.javacc.parser.JavaCCParserConstants;
import org.javacc.parser.Token;

public abstract class CodeBuilder<B extends CodeBuilder<?>> implements Closeable {

  private final Context context;
  private final CodeGeneratorSettings options;

  private File file;
  private String version;
  private final Set<String> tools = new LinkedHashSet<>();
  private final List<String> option = new ArrayList<>();

  private int cline;
  private int ccol;

  /**
   * Constructs an instance of {@link CodeBuilder}.
   *
   * @param context
   * @param options
   */
  protected CodeBuilder(final Context context, final CodeGeneratorSettings options) {
    this.context = context;
    this.options = options;
  }

  /** Get the {@link StringBuffer} */
  protected abstract StringBuffer getBuffer();

  /** Gets the target {@link File}. */
  protected final File getFile() {
    return file;
  }

  /**
   * Sets the target {@link File}.
   *
   * @param file
   */
  @SuppressWarnings("unchecked")
  public final B setFile(final File file) {
    this.file = file;
    return (B) this;
  }

  /**
   * Sets the compatible version.
   *
   * @param version
   */
  @SuppressWarnings("unchecked")
  public final B setVersion(final String version) {
    this.version = version;
    return (B) this;
  }

  /**
   * Add a tool.
   *
   * @param tool
   */
  @SuppressWarnings("unchecked")
  public final B addTools(final String... tools) {
    for (final String tool : tools) {
      this.tools.add(tool);
    }
    return (B) this;
  }

  /**
   * Add a tool.
   *
   * @param tool
   */
  @SuppressWarnings("unchecked")
  public final B addOption(final String... options) {
    for (final String option : options) {
      this.option.add(option);
    }
    return (B) this;
  }

  /**
   * Append code snippet to the builder.
   *
   * @param code
   */
  @SuppressWarnings("unchecked")
  public final B print(final Object... code) {
    for (final Object s : code) {
      getBuffer().append(s);
    }
    return (B) this;
  }

  /**
   * Append code snippet to the builder & a new line.
   *
   * @param code
   */
  @SuppressWarnings("unchecked")
  public final B println(final Object... code) {
    print(code);
    print("\n");
    return (B) this;
  }

  /**
   * Append the processed template, optionally provides additional options.
   *
   * @param name
   * @param additionalOptions
   * @throws IOException
   */
  public final B printTemplate(final String name) throws IOException {
    return printTemplate(name, CodeGeneratorSettings.create());
  }

  @SuppressWarnings("unchecked")
  public final B printTemplate(final String name, final CodeGeneratorSettings additionalOptions)
      throws IOException {
    final CodeGeneratorSettings options =
        additionalOptions.isEmpty()
            ? this.options
            : CodeGeneratorSettings.of(this.options).add(additionalOptions);

    try (StringWriter writer = new StringWriter()) {
      final TemplateBuilder generator = new TemplateBuilder(name, options);
      generator.generate(new PrintWriter(writer));
      writer.flush();
      print(writer.toString());
    }
    return (B) this;
  }

  /** Write the buffer to the file. */
  protected void build() {
    store(getFile(), getBuffer());
  }

  @Override
  public final void close() throws IOException {
    build();
  }

  protected final void store(final File file, final StringBuffer buffer) {
    final String tool = tools.isEmpty() ? JavaCCGlobals.toolName : String.join(",", tools);

    try (OutputFile output = new OutputFile(file, tool, version, option, context)) {
      output.getPrintWriter().print(buffer.toString());
    } catch (final IOException ioe) {
      context.errors().fatal("Could not create output file: " + file.getAbsolutePath());
    }
  }

  public final void printTokenSetup(final Token token) {
    Token tt = token;
    while (tt.specialToken != null) {
      tt = tt.specialToken;
    }
    cline = tt.beginLine;
    ccol = tt.beginColumn;
  }

  public final void printTokenList(final List<Token> list) {
    Token t = null;
    for (final Iterator<Token> it = list.iterator(); it.hasNext(); ) {
      t = it.next();
      printToken(t);
    }

    if (t != null) {
      printTrailingComments(t);
    }
  }

  public final void printTokenOnly(final Token t) {
    print(getStringForTokenOnly(t));
  }

  private String getStringForTokenOnly(final Token t) {
    String retval = "";
    for (; cline < t.beginLine; cline++) {
      retval += "\n";
      ccol = 1;
    }
    for (; ccol < t.beginColumn; ccol++) {
      retval += " ";
    }
    if ((t.kind == JavaCCParserConstants.STRING_LITERAL)
        || (t.kind == JavaCCParserConstants.CHARACTER_LITERAL)) {
      retval += escapeToUnicode(t.image);
    } else {
      retval += t.image;
    }
    cline = t.endLine;
    ccol = t.endColumn + 1;
    if (t.image.length() > 0) {
      final char last = t.image.charAt(t.image.length() - 1);
      if ((last == '\n') || (last == '\r')) {
        cline++;
        ccol = 1;
      }
    }
    return retval;
  }

  @SuppressWarnings("unused")
  private String getStringForSpecialTokenOnly(final Token t) {
    return getStringForSpecialTokenOnly(t, "");
  }

  private String getStringForSpecialTokenOnly(final Token t, final String pfx) {
    if ((t.kind == JavaCCParserConstants.MULTI_LINE_COMMENT)
        || (t.kind == JavaCCParserConstants.FORMAL_COMMENT)) {
      cline = t.endLine;
      ccol = t.endColumn + 1;
      return pfx + t.image;
    } else {
      // JavaCCParserConstants.SINGLE_LINE_COMMENT or SKIPPED TOKEN
      cline = t.endLine + 1;
      ccol = 1;
      return t.kind == JavaCCParserConstants.SINGLE_LINE_COMMENT ? pfx + t.image : t.image;
    }
  }

  public String escapeToUnicode(final String text) {
    return TokenUtils.addUnicodeEscapes(text);
  }

  public final void printToken(final Token t) {
    print(CodeBuilder.toString(t));
  }

  public final void printLeadingComments(final Token t) {
    print(getLeadingComments(t, ""));
  }

  public final void printLeadingComments(final Token t, final String pfx) {
    print(getLeadingComments(t, pfx));
  }

  public final String getLeadingComments(final Token t) {
    return getLeadingComments(t, "");
  }

  public final String getLeadingComments(final Token t, final String pfx) {
    if (t.specialToken == null) {
      return "";
    }
    Token tt = t.specialToken;
    while (tt.specialToken != null) {
      tt = tt.specialToken;
    }
    String retval = "";
    while (tt != null && (tt.image.equals("\r") || tt.image.equals("\n"))) {
      //      retval = getStringForSpecialTokenOnly(tt, pfx); // yes, = and not +=
      getStringForSpecialTokenOnly(tt, pfx);
      tt = tt.next;
    }
    while (tt != null) {
      retval += getStringForSpecialTokenOnly(tt, pfx);
      tt = tt.next;
    }
    return retval;
  }

  public final void printTrailingComments(final Token token) {
    getBuffer().append(getTrailingComments(token));
  }

  public final String getTrailingComments(final Token token) {
    if (token.next == null) {
      return "";
    }
    return getLeadingComments(token.next);
  }

  /**
   * Get the string representation of a {@link Token}.
   *
   * @param token
   */
  public static String toString(final Token token) {
    final StringBuilder builder = new StringBuilder();
    Token sToken = token.specialToken;
    if (sToken != null) {
      while (sToken.specialToken != null) {
        sToken = sToken.specialToken;
      }
      while (sToken != null) {
        builder.append(sToken.image);
        sToken = sToken.next;
      }
    }
    builder.append(token.image);
    return builder.toString();
  }

  /** The {@link GenericCodeBuilder} class. */
  public static class GenericCodeBuilder extends CodeBuilder<GenericCodeBuilder> {

    private final StringBuffer buffer = new StringBuffer();

    /**
     * Constructs an instance of {@link AbstractCodeGenBuilder2}.
     *
     * @param context
     * @param options
     */
    private GenericCodeBuilder(final Context context, final CodeGeneratorSettings options) {
      super(context, options);
    }

    /** Get the {@link StringBuffer} */
    @Override
    protected final StringBuffer getBuffer() {
      return buffer;
    }

    /**
     * Constructs an instance of {@link GenericCodeBuilder}.
     *
     * @param context
     * @param options
     */
    public static GenericCodeBuilder of(
        final Context context, final CodeGeneratorSettings options) {
      return new GenericCodeBuilder(context, options);
    }
  }
}
