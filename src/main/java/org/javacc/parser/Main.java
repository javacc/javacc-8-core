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

import java.util.Set;
import org.javacc.Version;
import org.javacc.utils.CodeBuilder;
import org.javacc.utils.OptionInfo;
import org.javacc.utils.OptionType;

/** Entry point. */
public class Main {

  protected Main() {}

  public static void printUsage(
      final String tool, final String ext, final Set<OptionInfo> options) {
    System.out.println("Usage:");
    System.out.println("    " + tool + " option-settings inputfile");
    System.out.println("");
    System.out.println("\"option-settings\" is a sequence of settings separated by spaces.");
    System.out.println("Each option setting must be of one of the following forms:");
    System.out.println("");
    System.out.println("    -optionname=value (e.g. -STATIC=false)");
    System.out.println("    -optionname:value (e.g. -STATIC:false)");
    System.out.println("    -optionname       (same as -optionname=true;  e.g. -STATIC)");
    System.out.println("    -NOoptionname     (same as -optionname=false; e.g. -NOSTATIC)");
    System.out.println("");
    System.out.println(
        "Option names are case-insensitive: one can use \"-nOsTaTiC\" instead of \"-NOSTATIC\".");
    System.out.println("Option values must be appropriate for the corresponding option,");
    System.out.println(" and must be either an integer, a boolean or a string value.");
    System.out.println("");

    // 2013/07/23 -- Changed this to auto-generate from metadata in Options
    //  so that help is always in-sync with codebase
    printOptions(options);

    System.out.println("EXAMPLE:");
    System.out.println(
        "    " + tool + " -STATIC=false -LOOKAHEAD:2 -NONO_DFA -debug_parser mygrammar." + ext);
    System.out.println("");
  }

  private static void printOptions(final Set<OptionInfo> options) {

    int maxLengthInt = 0;
    int maxLengthBool = 0;
    int maxLengthString = 0;

    for (final OptionInfo i : options) {
      final int length = i.getName().length();

      if (i.getType() == OptionType.INTEGER) {
        maxLengthInt = length > maxLengthInt ? length : maxLengthInt;
      } else if (i.getType() == OptionType.BOOLEAN) {
        maxLengthBool = length > maxLengthBool ? length : maxLengthBool;

      } else if (i.getType() == OptionType.STRING) {
        maxLengthString = length > maxLengthString ? length : maxLengthString;

      } else {
        // Not interested
      }
    }

    if (maxLengthInt > 0) {
      System.out.println("The integer valued options are:");
      System.out.println("");
      for (final OptionInfo i : options) {
        printOptionInfo(OptionType.INTEGER, i, maxLengthInt);
      }
      System.out.println("");
    }

    if (maxLengthBool > 0) {
      System.out.println("The boolean valued options are:");
      System.out.println("");
      for (final OptionInfo i : options) {
        printOptionInfo(OptionType.BOOLEAN, i, maxLengthBool);
      }
      System.out.println("");
    }

    if (maxLengthString > 0) {
      System.out.println("The string valued options are:");
      System.out.println("");
      for (final OptionInfo i : options) {
        printOptionInfo(OptionType.STRING, i, maxLengthString);
      }
      System.out.println("");
    }
  }

  private static void printOptionInfo(
      final OptionType filter, final OptionInfo optionInfo, final int padLength) {
    if (optionInfo.getType() == filter) {
      final Object defVal = optionInfo.getDefault();
      final String q =
          (optionInfo.getType() == OptionType.STRING
                  || optionInfo.getType() == OptionType.STRINGLIST)
              ? "\""
              : "";
      final String r =
          (optionInfo.getType() == OptionType.BOOLEAN && defVal.toString().equals("true"))
              ? " "
              : "";
      System.out.println(
          "    "
              + padRight(optionInfo.getName(), padLength + 1)
              + (defVal == null
                  ? "(no default)"
                  : ("(default: "
                      + (defVal.toString().length() == 0 ? "\"\"" : q + defVal + r + q)
                      + ")")));
    }
  }

  private static String padRight(final String name, final int maxLengthInt) {
    final int nameLength = name.length();
    if (nameLength == maxLengthInt) {
      return name;
    } else {
      final int charsToPad = maxLengthInt - nameLength;
      final StringBuilder sb = new StringBuilder(charsToPad);
      sb.append(name);

      for (int i = 0; i < charsToPad; i++) {
        sb.append(" ");
      }

      return sb.toString();
    }
  }

  /** A main program that exercises the parser. */
  public static void main(final String args[]) throws Exception {
    final int errorcode = mainProgram(args);
    System.exit(errorcode);
  }

  /**
   * The method to call to exercise the parser from other Java programs. It returns an error code.
   * See how the main program above uses this method.
   */
  public static int mainProgram(final String args[]) throws Exception {

    if (args.length == 1 && args[args.length - 1].equalsIgnoreCase("-version")) {
      System.out.println(Version.fullVersion);
      return 0;
    }

    // Initialize all static state
    final Context context = reInitAll();

    JavaCCGlobals.bannerLine("Parser Generator", "");

    JavaCCParser parser = null;
    if (args.length == 0) {
      System.out.println("");
      printUsage("javacc", "jj", Options.getUserOptions());
      return 1;
    } else {
      System.out.println("(type \"javacc\" with no arguments for help)");
    }

    if (Options.canArgBeAnOption(args[args.length - 1])) {
      System.out.println("Last argument \"" + args[args.length - 1] + "\" is not a filename.");
      return 1;
    }
    for (int arg = 0; arg < (args.length - 1); arg++) {
      if (!Options.canArgBeAnOption(args[arg])) {
        System.out.println("Argument \"" + args[arg] + "\" must be an option setting.");
        return 1;
      }
      Options.processCmdLineOption(args[arg]);
    }

    try {
      final java.io.File fp = new java.io.File(args[args.length - 1]);
      if (!fp.exists()) {
        System.out.println("File " + args[args.length - 1] + " not found.");
        return 1;
      }
      if (fp.isDirectory()) {
        System.out.println(
            args[args.length - 1] + " is a directory. Please use a valid file name.");
        return 1;
      }
      parser =
          new JavaCCParser(
              new java.io.BufferedReader(
                  new java.io.InputStreamReader(
                      new java.io.FileInputStream(args[args.length - 1]),
                      Options.getGrammarEncoding())));
    } catch (final SecurityException se) {
      System.out.println("Security violation while trying to open " + args[args.length - 1]);
      return 1;
    } catch (final java.io.FileNotFoundException e) {
      System.out.println("File " + args[args.length - 1] + " not found.");
      return 1;
    }

    try {
      System.out.println("Reading from file " + args[args.length - 1] + " . . .");
      // JavaCCGlobals.fileName = JavaCCGlobals.origFileName = args[args.length - 1];
      context.globals().jjtreeGenerated =
          JavaCCGlobals.isGeneratedBy("JJTree", args[args.length - 1]);
      context.globals().toolNames.addAll(JavaCCGlobals.getToolNames(args[args.length - 1]));
      parser.javacc_input(context);

      context.createOutputDir(Options.getOutputDirectory());

      boolean unicodeWarning = false;
      if (Options.getUnicodeInput()) {
        unicodeWarning = true;
        System.out.println(
            "Note: UNICODE_INPUT option is specified. "
                + "Please make sure you create the parser/lexer using a Reader with the correct character encoding.");
      }

      Semanticize.start(context);

      final CodeGenerator codeGenerator = context.getCodeGenerator();
      if (codeGenerator != null) {
        final ParserCodeGenerator parserCodeGenerator =
            codeGenerator.getParserCodeGenerator(context);
        if (Options.getBuildParser() && (parserCodeGenerator != null)) {
          final ParserData parserData = createParserData(context);
          final CodeGeneratorSettings settings = CodeGeneratorSettings.of(Options.getOptions());
          parserCodeGenerator.generateCode(settings, parserData);
          parserCodeGenerator.finish(settings, parserData);
        }

        // Must always create the lexer object even if not building a parser.
        final LexGen lg = new LexGen(context);
        final TokenizerData tokenizerData = lg.generateTokenizerData(false, unicodeWarning);

        Options.setStringOption(Options.NUO__PARSER_NAME, context.globals().cu_name);

        if (context.errors().get_error_count() != 0) {
          throw new MetaParseException();
        }
        if (Options.getGenerateBoilerplateCode()) {
          final CodeGeneratorSettings cgs = CodeGeneratorSettings.of(Options.getOptions());
          if (!codeGenerator.getTokenCodeGenerator(context).generateCodeForToken(cgs)
              || !codeGenerator.generateHelpers(context, cgs, tokenizerData)) {
            context
                .errors()
                .semantic_error("Could not generate the code for Token or helper classes.");
          }
        }
      }

      if ((context.errors().get_error_count() == 0)
          && (Options.getBuildParser() || Options.getBuildTokenManager())) {
        if (context.errors().get_warning_count() == 0) {
          if (Options.getBuildParser()) {
            System.out.println("Parser generated successfully.");
          }
        } else {
          System.out.println(
              "Parser generated with 0 errors and "
                  + context.errors().get_warning_count()
                  + " warnings.");
        }
        return 0;
      } else {
        System.out.println(
            "Detected "
                + context.errors().get_error_count()
                + " errors and "
                + context.errors().get_warning_count()
                + " warnings.");
        return (context.errors().get_error_count() == 0) ? 0 : 1;
      }
    } catch (final MetaParseException e) {
      System.out.println(
          "Detected "
              + context.errors().get_error_count()
              + " errors and "
              + context.errors().get_warning_count()
              + " warnings.");
      return 1;
    } catch (final ParseException e) {
      System.out.println(e.toString());
      System.out.println(
          "Detected "
              + (context.errors().get_error_count() + 1)
              + " errors and "
              + context.errors().get_warning_count()
              + " warnings.");
      return 1;
    }
  }

  private static ParserData createParserData(final Context context) {
    final ParserData parserData = new ParserData();
    parserData.bnfproductions = context.globals().bnfproductions;
    parserData.parserName = context.globals().cu_name;
    parserData.tokenCount = context.globals().tokenCount;
    parserData.namesOfTokens = context.globals().names_of_tokens;
    parserData.productionTable = context.globals().production_table;
    final StringBuilder decls = new StringBuilder();
    if (context.globals().otherLanguageDeclTokenBeg != null) {
      // int line = context.globals().otherLanguageDeclTokenBeg.beginLine;
      for (Token t = context.globals().otherLanguageDeclTokenBeg;
          t != context.globals().otherLanguageDeclTokenEnd;
          t = t.next) {
        decls.append(CodeBuilder.toString(t));
      }
    }
    parserData.decls = decls.toString();
    return parserData;
  }

  public static Context reInitAll() {
    return new Context();
  }
}
