/*
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

import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;
import org.javacc.parser.CodeGenerator;
import org.javacc.parser.JavaCCGlobals;
import org.javacc.parser.Options;

public class JJTree {

  private IO io;

  private void p(final String s) {
    io.getMsg().println(s);
  }

  private void printUsage() {
    p("Usage:");
    p("    jjtree option-settings inputfile");
    p("");
    p("\"option-settings\" is a sequence of settings separated by spaces.");
    p("Each option setting must be of one of the following forms:");
    p("");
    p("    -optionname=value (e.g. -STATIC=false)");
    p("    -optionname:value (e.g. -STATIC:false)");
    p("    -optionname       (same as -optionname=true;  e.g. -STATIC)");
    p("    -NOoptionname     (same as -optionname=false; e.g. -NOSTATIC)");
    p("");
    p("Option names are case-insensitive, so one can use \"-nOsTaTiC\" instead of \"-NOSTATIC\".");
    p("Option values must be appropriate for the corresponding option,");
    p(" and must be either an integer, a boolean or a string value.");
    p("");
    p("The boolean valued JJTree-specific options are:");
    p("");
    p("    BUILD_NODE_FILES         (default: true )");
    p("    MULTI                    (default: false)");
    p("    NODE_DEFAULT_VOID        (default: false)");
    p("    NODE_SCOPE_HOOK          (default: false)");
    p("    NODE_USES_PARSER         (default: false)");
    p("    TRACK_TOKENS             (default: false)");
    p("    VISITOR                  (default: false)");
    p("");
    p("The string valued JJTree-specific options are:");
    p("");
    p("    JJTREE_OUTPUT_DIRECTORY  (default: value of (JavaCC) OUTPUT_DIRECTORY option)");
    p("    NODE_CLASS               (default: \"\")");
    p("    NODE_DIRECTORY           (default: value of JJTREE_OUTPUT_DIRECTORY option)");
    p("    NODE_EXTENDS             (default: \"\")");
    p("    NODE_FACTORY             (default: \"\")");
    p("    NODE_PACKAGE             (default: \"\")");
    p("    NODE_PREFIX              (default: \"AST\")");
    p("    OUTPUT_FILE              (default: replace input file suffix by .jj)");
    p("    VISITOR_DATA_TYPE        (default: \"\")");
    p("    VISITOR_EXCEPTION        (default: \"\")");
    p("    VISITOR_RETURN_TYPE      (default: \"Object\")");
    p("");
    p("JJTree accepts all JavaCC options, and inserts them into the generated file;");
    p(" it also uses the following ones:");
    p("");
    p("    GRAMMAR_ENCODING         (default: \"\" -> the platform file.encoding)");
    p("    IGNORE_ACTIONS           (default: false)");
    p("    NAMESPACE                (default: \"\")");
    p("    OUTPUT_DIRECTORY         (default: \".\")");
    p("    STATIC                   (default: true )");
    p("");
    p("EXAMPLES:");
    p("    jjtree -STATIC=false -VISITOR_RETURN_TYPE=\"my.pkg.type\" mygrammar.jjt");
    p("");
  }

  /** A main program that exercises the parser. */
  public int main(final String args[]) {

    // initialize static state for allowing repeat runs without exiting
    ASTNodeDescriptor.nodeIds = new ArrayList<>();
    ASTNodeDescriptor.nodeNames = new ArrayList<>();
    ASTNodeDescriptor.nodeSeen = new Hashtable<>();
    org.javacc.parser.Main.reInitAll();

    JavaCCGlobals.bannerLine("Tree Builder", "");

    io = new IO();

    try {
      final JJTreeContext context = new JJTreeContext();
      JJTreeGlobals.initialize();

      if (args.length == 0) {
        p("");
        printUsage();
        return 1;
      } else {
        p("(type \"jjtree\" with no arguments for help)");
      }

      final String fn = args[args.length - 1];

      if (Options.canArgBeAnOption(fn)) {
        p("Last argument \"" + fn + "\" is not a filename");
        return 1;
      }
      for (int arg = 0; arg < (args.length - 1); arg++) {
        if (!Options.canArgBeAnOption(args[arg])) {
          p("Argument \"" + args[arg] + "\" must be an option setting.");
          return 1;
        }
        Options.processCmdLineOption(args[arg]);
      }

      //      context.validate();

      try {
        io.setInput(fn);
      } catch (final JJTreeIOException ioe) {
        p("Error setting input: " + ioe.getMessage());
        return 1;
      }
      p("Reading from file " + io.getInputFileName() + " . . .");

      JJTreeGlobals.toolList = JavaCCGlobals.getToolNames(fn);
      JJTreeGlobals.toolList.add("JJTree");

      try {
        final JJTreeParser parser = new JJTreeParser(io.getIn());
        parser.javacc_input(context);

        context.validate();

        final ASTGrammar root = (ASTGrammar) parser.jjtree.rootNode();
        if (Boolean.getBoolean("jjtree-dump")) {
          root.dump(" ");
        }
        try {
          io.setOutput(context);
        } catch (final JJTreeIOException ioe) {
          p("Error setting output: " + ioe.getMessage());
          return 1;
        }
        JJTree.generateIO(io, root, context);
        io.getOut().close();
        p("Annotated grammar generated successfully in " + io.getOutputFileName());

      } catch (final ParseException pe) {
        p("Error parsing input: " + pe.toString());
        return 1;
      } catch (final Exception e) {
        p("Error parsing input: " + e.toString());
        e.printStackTrace(io.getMsg());
        return 1;
      }

      return 0;

    } finally {
      io.closeAll();
    }
  }

  private static void generateIO(final IO io, final ASTGrammar grammar, final JJTreeContext context)
      throws IOException {
    // TODO :: CBA -- Require Unification of output language specific processing
    // into a single Enum class
    final CodeGenerator cg = context.getCodeGenerator();
    if (cg != null) {
      final DefaultJJTreeVisitor vis = cg.getJJTreeCodeGenerator(context);
      vis.visit(grammar, io);
      vis.generateHelperFiles();
    } else {
      // Catch all to ensure we don't accidently do nothing
      throw new RuntimeException(
          "No valid CodeGenerator for JJTree : " + Options.getCodeGenerator());
    }
  }
}
