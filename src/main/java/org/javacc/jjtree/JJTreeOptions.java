/*
 * Copyright (c) 2005-2006, Kees Jan Koster <kjkoster@kjkoster.org>.
 * Copyright (c) 2011-2025, Sreeni Viswanadha <sreeni@viswanadha.net>.
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

import java.io.File;
import org.javacc.parser.Options;

// TODO refactor Options / JJTreeOptions / JJTreeContext to make them consistent.
/**
 * The JJTree-specific options.
 *
 * @author Kees Jan Koster &lt;kjkoster@kjkoster.org&gt;
 */
public class JJTreeOptions extends Options {

  /** Limit subclassing. */
  protected JJTreeOptions() {}

  /**
   * Find the build node files value (core).
   *
   * @return The requested build node files value.
   */
  public final boolean getBuildNodeFiles() {
    return Options.booleanValue("BUILD_NODE_FILES");
  }

  /**
   * Find the output directory to place the JJTree generated files into (that annotated grammar, the
   * nodes, the visitors and other helper files).<br>
   * If none is configured, use the value of <code>getOutputDirectory()</code> (a JavaCC option,
   * which defaults to <code>"."</code>.<br>
   * (java / gen, csharp / gen, cpp / gen).
   *
   * @return The requested JJTree output directory
   */
  public final File getJJTreeOutputDirectory() {
    final String dirName = Options.stringValue("JJTREE_OUTPUT_DIRECTORY");
    if ("".equals(dirName)) {
      return Options.getOutputDirectory();
    } else {
      return new File(dirName);
    }
  }

  /**
   * Find the multi value (java / gen, csharp / gen, cpp / gen).
   *
   * @return The requested multi value.
   */
  public final boolean getMulti() {
    return Options.booleanValue("MULTI");
  }

  /**
   * Find the node class name (java / gen + tpl, csharp / gen + tpl, cpp / gen + tpl).
   *
   * @return The requested node class
   */
  public final String getNodeClass() {
    return Options.stringValue("NODE_CLASS");
  }

  /**
   * Find the node default void value (core, -> java, csharp, cpp).
   *
   * @return The requested node default void value.
   */
  public final boolean getNodeDefaultVoid() {
    return Options.booleanValue("NODE_DEFAULT_VOID");
  }

  /**
   * Find the directory of the user defined node files (not the JJTree generated node files).<br>
   * If none is configured, use the value of <code>getJJTreeOutputDirectory()</code>.<br>
   * (java / gen, cpp / gen).
   *
   * @return the requested node directory directory
   */
  public final File getNodeDirectory() {
    final String dirName = Options.stringValue("NODE_DIRECTORY");
    if ("".equals(dirName)) {
      return getJJTreeOutputDirectory();
    } else {
      return new File(dirName);
    }
  }

  /**
   * Find the node super class name (java / gen + tpl, csharp / gen + tpl, cpp / gen + tpl).
   *
   * @return The requested node super class
   */
  public final String getNodeExtends() {
    return Options.stringValue("NODE_EXTENDS");
  }

  /**
   * Find the node factory value (java / gen + tpl, csharp / gen + tpl, cpp / gen + tpl).
   *
   * @return The requested node factory value.
   */
  public final String getNodeFactory() {
    return Options.stringValue("NODE_FACTORY");
  }

  /**
   * Find the node includes value (cpp / tpl).
   *
   * @return The requested node includes value.
   */
  public final String getNodeIncludes() {
    return Options.stringValue("NODE_INCLUDES");
  }

  /**
   * Find the node package value (java / gen + tpl).
   *
   * @return The requested node package value.
   */
  public final String getNodePackage() {
    return Options.stringValue("NODE_PACKAGE");
  }

  /**
   * Find the node prefix value (java / gen, csharp / gen, cpp / gen).
   *
   * @return The requested node prefix value.
   */
  public final String getNodePrefix() {
    return Options.stringValue("NODE_PREFIX");
  }

  /**
   * Find the node scope hook value (java / gen, csharp / gen, cpp / gen).
   *
   * @return The requested node scope hook value.
   */
  public final boolean getNodeScopeHook() {
    return Options.booleanValue("NODE_SCOPE_HOOK");
  }

  /**
   * Find the node uses parser value (java / gen, csharp / gen, cpp / gen + tpl).
   *
   * @return The requested node uses parser value.
   */
  public final boolean getNodeUsesParser() {
    return Options.booleanValue("NODE_USES_PARSER");
  }

  /**
   * Find the output file value (java / gen, cpp / gen).
   *
   * @return The requested output file value.
   */
  public final String getOutputFile() {
    return Options.stringValue("OUTPUT_FILE");
  }

  /**
   * Find the trackTokens value (java / gen + tpl, csharp / gen + tpl, cpp / gen + tpl).
   *
   * @return The requested trackTokens value.
   */
  public final boolean getTrackTokens() {
    return Options.booleanValue("TRACK_TOKENS");
  }

  /**
   * Find the visitor value (java / gen + tpl, csharp / gen + tpl, cpp / gen + tpl).
   *
   * @return The requested visitor value.
   */
  public final boolean getVisitor() {
    return Options.booleanValue("VISITOR");
  }

  /**
   * Find the visitor data type value (java / gen + tpl, csharp / gen + tpl, cpp / gen + tpl).
   *
   * @return The requested visitor data type value.
   */
  public final String getVisitorDataType() {
    return Options.stringValue("VISITOR_DATA_TYPE");
  }

  /**
   * Find the visitor data type is pointer value (csharp / gen + tpl). (non user option).
   *
   * @return The requested visitor data type is pointer value.
   */
  public final Boolean getVisitorDataTypeIsPointer() {
    return Options.booleanValue("VISITOR_DATA_TYPE_IS_POINTER");
  }

  /**
   * Find the visitor exception value (java / gen + tpl, csharp / gen + tpl).
   *
   * @return The requested visitor exception value.
   */
  public final String getVisitorException() {
    return Options.stringValue("VISITOR_EXCEPTION");
  }

  /**
   * Find the visitor method name includes type name value (java / gen + tpl, csharp / gen + tpl).
   *
   * @return The requested visitor method name includes type name value.
   */
  public final Boolean getVisitorMethodNameIncludesTypeName() {
    return Options.booleanValue("VISITOR_METHOD_NAME_INCLUDES_TYPE_NAME");
  }

  /**
   * Find the visitor return type value (java / gen + tpl, csharp / gen + tpl, cpp / gen + tpl).
   *
   * @return The requested visitor return type value.
   */
  public final String getVisitorReturnType() {
    return Options.stringValue("VISITOR_RETURN_TYPE");
  }
}
