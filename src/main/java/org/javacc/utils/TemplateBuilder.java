/*
 * Copyright (c) 2020-2025, Sreeni Viswanadha <sreeni@viswanadha.net>.
 * Copyright (c) 2024-2025, Marc Mazas <mazas.marc@gmail.com>.
 * Copyright (c) 2008, Paul Cager.
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
package org.javacc.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringReader;
import org.javacc.parser.CodeGeneratorSettings;

/**
 * Generates boiler-plate files from templates. Only very basic template processing is supplied - if
 * we need something more sophisticated I suggest we use a third-party library.
 *
 * @author paulcager
 * @since 4.2
 */
class TemplateBuilder {

  private final String template;
  private final CodeGeneratorSettings options;

  private String currentLine;

  /**
   * @param template the template. E.g. "/templates/java/Token.template".
   * @param options the processing options in force, such as "STATIC=yes"
   */
  TemplateBuilder(final String template, final CodeGeneratorSettings options) {
    this.template = template;
    this.options = options;
  }

  /**
   * Generate the output file.
   *
   * @param writer
   * @throws IOException
   */
  public void generate(final PrintWriter writer) throws IOException {
    final InputStream istream = getClass().getResourceAsStream(template);
    if (istream == null) {
      throw new IOException("Invalid template name: " + template);
    }
    try (BufferedReader reader = new BufferedReader(new InputStreamReader(istream))) {
      process(reader, writer, false);
    }
  }

  private String peekLine(final BufferedReader in) throws IOException {
    if (currentLine == null) {
      currentLine = in.readLine();
    }
    return currentLine;
  }

  private String getLine(final BufferedReader in) throws IOException {
    final String line = currentLine;
    currentLine = null;

    if (line == null) {
      in.readLine();
    }
    return line;
  }

  private boolean evaluate(String condition) {
    condition = condition.trim();

    try {
      return new ConditionParser(new StringReader(condition)).CompilationUnit(options);
    } catch (final ParseException e) {
      return false;
    }
  }

  private String substitute(final String text) throws IOException {
    int startPos;

    if ((startPos = text.indexOf("${")) == -1) {
      return text;
    }

    // Find matching "}".
    int braceDepth = 1;
    int endPos = startPos + 2;

    while ((endPos < text.length()) && (braceDepth > 0)) {
      if (text.charAt(endPos) == '{') {
        braceDepth++;
      } else if (text.charAt(endPos) == '}') {
        braceDepth--;
      }

      endPos++;
    }

    if (braceDepth != 0) {
      throw new IOException("Mismatched \"{}\" in template string: " + text);
    }

    final String variableExpression = text.substring(startPos + 2, endPos - 1);

    // Find the end of the variable name
    String value = null;

    for (int i = 0; i < variableExpression.length(); i++) {
      final char ch = variableExpression.charAt(i);

      if ((ch == ':')
          && (i < (variableExpression.length() - 1))
          && (variableExpression.charAt(i + 1) == '-')) {
        value =
            substituteWithDefault(
                variableExpression.substring(0, i), variableExpression.substring(i + 2));
        break;
      } else if (ch == '?') {
        value =
            substituteWithConditional(
                variableExpression.substring(0, i), variableExpression.substring(i + 1));
        break;
      } else if ((ch != '_') && !Character.isJavaIdentifierPart(ch)) {
        throw new IOException("Invalid variable in " + text);
      }
    }

    if (value == null) {
      value = substituteWithDefault(variableExpression, "");
    }

    return text.substring(0, startPos) + value + text.substring(endPos);
  }

  /**
   * @param substring
   * @param defaultValue
   * @return
   * @throws IOException
   */
  private String substituteWithConditional(final String variableName, final String values)
      throws IOException {
    // Split values into true and false values.

    final int pos = values.indexOf(':');
    if (pos == -1) {
      throw new IOException("No ':' separator in " + values);
    }

    if (evaluate(variableName)) {
      return substitute(values.substring(0, pos));
    } else {
      return substitute(values.substring(pos + 1));
    }
  }

  /**
   * @param variableName
   * @param defaultValue
   * @return
   */
  private String substituteWithDefault(final String variableName, final String defaultValue)
      throws IOException {
    final Object obj = options.get(variableName.trim());
    if ((obj == null) || (obj.toString().length() == 0)) {
      return substitute(defaultValue);
    }

    return obj.toString();
  }

  private void write(final PrintWriter out, String text) throws IOException {
    while (text.indexOf("${") != -1) {
      text = substitute(text);
    }

    // TODO :: Added by Sreenivas on 12 June 2013 for 6.0 release, merged in to
    // 6.1 release for sake of compatibility by cainsley ... This needs to be
    // removed urgently!!!
    //    if (text.startsWith("\\#")) { // Hack to escape # for C++
    //      text = text.substring(1);
    //    }
    out.println(text.replace("\\#", "#"));
  }

  private void process(final BufferedReader in, final PrintWriter out, final boolean ignoring)
      throws IOException {
    // out.println("*** process ignore=" + ignoring + " : " + peekLine(in));
    while (peekLine(in) != null) {
      if (peekLine(in).trim().startsWith("#if")) {
        processIf(in, out, ignoring);
      } else if (peekLine(in).trim().startsWith("#")) {
        break;
      } else {
        final String line = getLine(in);
        if (!ignoring) {
          write(out, line);
        }
      }
    }

    out.flush();
  }

  private void processIf(final BufferedReader in, final PrintWriter out, final boolean ignoring)
      throws IOException {
    String line = getLine(in).trim();
    assert line.trim().startsWith("#if");
    boolean foundTrueCondition = false;

    boolean condition = evaluate(line.substring(3).trim());
    while (true) {
      process(in, out, ignoring || foundTrueCondition || !condition);
      foundTrueCondition |= condition;

      if ((peekLine(in) == null) || !peekLine(in).trim().startsWith("#elif")) {
        break;
      }

      condition = evaluate(getLine(in).trim().substring(5).trim());
    }

    if ((peekLine(in) != null) && peekLine(in).trim().startsWith("#else")) {
      getLine(in); // Discard the #else line
      process(in, out, ignoring || foundTrueCondition);
    }

    line = getLine(in);

    if (line == null) {
      throw new IOException("Missing \"#fi\"");
    }

    if (!line.trim().startsWith("#fi")) {
      throw new IOException("Expected \"#fi\", got: " + line);
    }
  }
}
