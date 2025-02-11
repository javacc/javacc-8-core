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
package org.javacc.parser;

import java.io.File;
import java.util.ServiceLoader;

/**
 * The JavaCC context holds the objects & methods for the code generation process (options, globals,
 * errors & codeGenerator).
 */
public class Context {

  private final Options options;
  private final JavaCCGlobals globals;
  private final JavaCCErrors errors;
  private CodeGenerator codeGenerator = null;

  public Context() {
    this(new Options());
  }

  public Context(final Options options) {
    this.options = options;
    this.errors = new JavaCCErrors();
    this.globals = new JavaCCGlobals();
    this.codeGenerator = null;
    Options.init();
  }

  /** Get the {@link Options} instance. */
  public final Options options() {
    return options;
  }

  /** Get the {@link JavaCCGlobals} instance. */
  public final JavaCCGlobals globals() {
    return globals;
  }

  /** Get the {@link JavaCCErrors} instance. */
  public final JavaCCErrors errors() {
    return errors;
  }

  /** Get the {@link CodeGenerator} instance. */
  public final CodeGenerator getCodeGenerator() {
    if (codeGenerator != null) {
      return codeGenerator;
    }

    final String name = Options.getCodeGenerator();
    if (name == null) {
      return null;
    }

    final ServiceLoader<CodeGenerator> serviceLoader = ServiceLoader.load(CodeGenerator.class);
    for (final CodeGenerator generator : serviceLoader) {
      if (generator.getName().equalsIgnoreCase(name)) {
        codeGenerator = generator;
        return codeGenerator;
      }
    }
    errors().semantic_error("Could not load the CodeGenerator class: \"" + name + "\"");
    return codeGenerator;
  }

  /**
   * Create and checks the output directory.
   *
   * @param outputDir - the output directory
   */
  public final void createOutputDir(final File outputDir) {
    if (!outputDir.exists()) {
      errors()
          .warning(
              "Output directory \"" + outputDir + "\" does not exist. Creating the directory.");

      if (!outputDir.mkdirs()) {
        errors().semantic_error("Cannot create the output directory : " + outputDir);
        return;
      }
    }

    if (!outputDir.isDirectory()) {
      errors().semantic_error("\"" + outputDir + " is not a valid output directory.");
      return;
    }

    if (!outputDir.canWrite()) {
      errors()
          .semantic_error("Cannot write to the output output directory : \"" + outputDir + "\"");
      return;
    }
  }
}
