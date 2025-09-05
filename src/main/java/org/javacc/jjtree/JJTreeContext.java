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

import org.javacc.parser.Context;
import org.javacc.parser.Options;

public class JJTreeContext extends Context {

  private final JJTreeOptions treeOptions = new JJTreeOptions();

  public JJTreeContext() {

    Options.resOptions.put("BUILD_NODE_FILES", Boolean.TRUE);
    Options.resOptions.put("JJTREE_OUTPUT_DIRECTORY", "");
    Options.resOptions.put("MULTI", Boolean.FALSE);
    Options.resOptions.put("NODE_CLASS", "");
    Options.resOptions.put("NODE_DEFAULT_VOID", Boolean.FALSE);
    Options.resOptions.put("NODE_DIRECTORY", "");
    Options.resOptions.put("NODE_EXTENDS", "");
    Options.resOptions.put("NODE_FACTORY", "");
    Options.resOptions.put("NODE_INCLUDES", "");
    Options.resOptions.put("NODE_PACKAGE", "");
    Options.resOptions.put("NODE_PREFIX", "AST");
    Options.resOptions.put("NODE_SCOPE_HOOK", Boolean.FALSE);
    Options.resOptions.put("NODE_USES_PARSER", Boolean.FALSE);
    Options.resOptions.put("OUTPUT_FILE", "");
    Options.resOptions.put("SINGLE_TREE_FILE", Boolean.TRUE);
    Options.resOptions.put("TRACK_TOKENS", Boolean.FALSE);
    Options.resOptions.put("VISITOR", Boolean.FALSE);
    Options.resOptions.put("VISITOR_DATA_TYPE", "");
    Options.resOptions.put("VISITOR_DATA_TYPE_IS_POINTER", Boolean.FALSE);
    Options.resOptions.put("VISITOR_EXCEPTION", "");
    Options.resOptions.put("VISITOR_METHOD_NAME_INCLUDES_TYPE_NAME", Boolean.FALSE);
    Options.resOptions.put("VISITOR_RETURN_TYPE", "Object");

    // JavaCC options that must also be known and used by JJTree; managed in the parent class
    //    Options.resOptions.put(Options.UO__CODE_GENERATOR, "");
    //    Options.resOptions.put(Options.UO__GRAMMAR_ENCODING, "");
    //    Options.resOptions.put(Options.UO__IGNORE_ACTIONS, Boolean.FALSE);
    //    //    Options.resOptions.put(Options.UO__JDK_VERSION, "1.5");
    //    Options.resOptions.put(Options.UO__NAMESPACE, "");
    //    Options.resOptions.put(Options.UO__OUTPUT_DIRECTORY, ".");
    //    Options.resOptions.put(Options.UO__STATIC, Boolean.TRUE);
  }

  public final JJTreeOptions treeOptions() {
    return treeOptions;
  }

  /** Check options for consistency and set non user options. */
  final void validate() {

    if (!treeOptions().getVisitor()) {
      if (treeOptions().getVisitorDataType().length() > 0) {
        errors().warning("VISITOR_DATA_TYPE option will be ignored since VISITOR is false");
      }
      if ((treeOptions().getVisitorReturnType().length() > 0)
          && !treeOptions().getVisitorReturnType().equals("Object")) {
        errors().warning("VISITOR_RETURN_TYPE option will be ignored since VISITOR is false");
      }
      if (treeOptions().getVisitorException().length() > 0) {
        errors().warning("VISITOR_EXCEPTION option will be ignored since VISITOR is false");
      }
    } else if (treeOptions().getVisitorDataType().endsWith("*")) {
      Options.resOptions.put("VISITOR_DATA_TYPE_IS_POINTER", Boolean.TRUE);
    }
  }
}
