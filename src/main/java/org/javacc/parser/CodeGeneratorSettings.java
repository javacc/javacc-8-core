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

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/** The {@link CodeGeneratorSettings} implements a {@link Map} builder. */
public final class CodeGeneratorSettings extends HashMap<String, Object> {

  private static final long serialVersionUID = -3963288772981602994L;

  /**
   * Constructs an instance of {@link CodeGeneratorSettings}.
   *
   * @param options
   */
  private CodeGeneratorSettings(final Map<String, Object> options) {
    putAll(options);
  }

  /**
   * Add another {@link CodeGeneratorSettings} to the current instance.
   *
   * @param options
   */
  public final CodeGeneratorSettings add(final CodeGeneratorSettings options) {
    putAll(options);
    return this;
  }

  /** Set an option to he {@link CodeGeneratorSettings}. */
  public final CodeGeneratorSettings set(final String key, final Object value) {
    put(key, value);
    return this;
  }

  /**
   * Creates a new instance of {@link CodeGeneratorSettings} from another option {@link Map}.
   *
   * @param options
   */
  public static CodeGeneratorSettings of(final Map<String, Object> options) {
    return new CodeGeneratorSettings(options);
  }

  /** Creates an empty instance of {@link CodeGeneratorSettings}. */
  public static CodeGeneratorSettings create() {
    return new CodeGeneratorSettings(Collections.emptyMap());
  }
}
