/*
 * Copyright (c) 2006, Sun Microsystems, Inc. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * * Redistributions of source code must retain the above copyright notice, this
 * list of conditions and the following disclaimer. * Redistributions in binary
 * form must reproduce the above copyright notice, this list of conditions and
 * the following disclaimer in the documentation and/or other materials provided
 * with the distribution. * Neither the name of the Sun Microsystems, Inc. nor
 * the names of its contributors may be used to endorse or promote products
 * derived from this software without specific prior written permission.
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
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */


package org.javacc.jjdoc;

import org.javacc.parser.JavaCCGlobals;
import org.javacc.parser.JavaCCParser;
import org.javacc.parser.Options;

/**
 * Main class.
 */
public final class JJDocMain extends JJDocGlobals {

  private JJDocMain() {}

  static void help_message(JJDocContext context) {
    JJDocGlobals.info(context, "");
    JJDocGlobals.info(context, "    jjdoc option-settings - (to read from standard input)");
    JJDocGlobals.info(context, "OR");
    JJDocGlobals.info(context, "    jjdoc option-settings inputfile (to read from a file)");
    JJDocGlobals.info(context, "");
    JJDocGlobals.info(context, "WHERE");
    JJDocGlobals.info(context, "    \"option-settings\" is a sequence of settings separated by spaces.");
    JJDocGlobals.info(context, "");

    JJDocGlobals.info(context, "Each option setting must be of one of the following forms:");
    JJDocGlobals.info(context, "");
    JJDocGlobals.info(context, "    -optionname=value (e.g., -TEXT=false)");
    JJDocGlobals.info(context, "    -optionname:value (e.g., -TEXT:false)");
    JJDocGlobals.info(context, "    -optionname       (equivalent to -optionname=true.  e.g., -TEXT)");
    JJDocGlobals.info(context, "    -NOoptionname     (equivalent to -optionname=false. e.g., -NOTEXT)");
    JJDocGlobals.info(context, "");
    JJDocGlobals.info(context, "Option settings are not case-sensitive, so one can say \"-nOtExT\" instead");
    JJDocGlobals.info(context, "of \"-NOTEXT\".  Option values must be appropriate for the corresponding");
    JJDocGlobals.info(context, "option, and must be either an integer, boolean or string value.");
    JJDocGlobals.info(context, "");
    JJDocGlobals.info(context, "The string valued options are:");
    JJDocGlobals.info(context, "");
    JJDocGlobals.info(context, "    OUTPUT_FILE");
    JJDocGlobals.info(context, "    CSS");
    JJDocGlobals.info(context, "");
    JJDocGlobals.info(context, "The boolean valued options are:");
    JJDocGlobals.info(context, "");
    JJDocGlobals.info(context, "    ONE_TABLE              (default true)");
    JJDocGlobals.info(context, "    TEXT                   (default false)");
    JJDocGlobals.info(context, "    BNF                    (default false)");
    JJDocGlobals.info(context, "    JCC                    (default false)");
    JJDocGlobals.info(context, "");

    JJDocGlobals.info(context, "");
    JJDocGlobals.info(context, "EXAMPLES:");
    JJDocGlobals.info(context, "    jjdoc -ONE_TABLE=false mygrammar.jj");
    JJDocGlobals.info(context, "    jjdoc - < mygrammar.jj");
    JJDocGlobals.info(context, "");
    JJDocGlobals.info(context, "ABOUT JJDoc:");
    JJDocGlobals.info(context, "    JJDoc generates JavaDoc documentation from JavaCC grammar files.");
    JJDocGlobals.info(context, "");
    JJDocGlobals.info(context, "    For more information, see the online JJDoc documentation at");
    JJDocGlobals.info(context, "    https://github.com/javacc/doc/JJDoc.html");
  }

  /**
   * A main program that exercises the parser.
   */
  public static void main(String args[]) throws Exception {
    int errorcode = JJDocMain.mainProgram(args);
    System.exit(errorcode);
  }

  /**
   * The method to call to exercise the parser from other Java programs. It
   * returns an error code. See how the main program above uses this method.
   */
  public static int mainProgram(String args[]) throws Exception {
    JJDocContext context = new JJDocContext();
    JavaCCGlobals.bannerLine("Documentation Generator", "0.1.4");

    JavaCCParser parser = null;
    if (args.length == 0) {
      JJDocMain.help_message(context);
      return 1;
    } else {
      JJDocGlobals.info(context, "(type \"jjdoc\" with no arguments for help)");
    }


    if (Options.isOption(args[args.length - 1])) {
      JJDocGlobals.error(context, "Last argument \"" + args[args.length - 1] + "\" is not a filename or \"-\".  ");
      return 1;
    }
    for (int arg = 0; arg < (args.length - 1); arg++) {
      if (!Options.isOption(args[arg])) {
        JJDocGlobals.error(context, "Argument \"" + args[arg] + "\" must be an option setting.  ");
        return 1;
      }
      Options.setCmdLineOption(args[arg]);
    }

    if (args[args.length - 1].equals("-")) {
      JJDocGlobals.info(context, "Reading from standard input . . .");
      parser = new JavaCCParser(new java.io.DataInputStream(System.in));
      JJDocGlobals.input_file = "standard input";
      JJDocGlobals.output_file = "standard output";
    } else {
      JJDocGlobals.info(context, "Reading from file " + args[args.length - 1] + " . . .");
      try {
        java.io.File fp = new java.io.File(args[args.length - 1]);
        if (!fp.exists()) {
          JJDocGlobals.error(context, "File " + args[args.length - 1] + " not found.");
          return 1;
        }
        if (fp.isDirectory()) {
          JJDocGlobals.error(context, args[args.length - 1] + " is a directory. Please use a valid file name.");
          return 1;
        }
        JJDocGlobals.input_file = fp.getName();
        parser = new JavaCCParser(new java.io.BufferedReader(new java.io.InputStreamReader(
            new java.io.FileInputStream(args[args.length - 1]), Options.getGrammarEncoding())));
      } catch (SecurityException se) {
        JJDocGlobals.error(context, "Security violation while trying to open " + args[args.length - 1]);
        return 1;
      } catch (java.io.FileNotFoundException e) {
        JJDocGlobals.error(context, "File " + args[args.length - 1] + " not found.");
        return 1;
      }
    }
    try {

      parser.javacc_input(context);
      JJDoc.start(context);

      if (context.errors().get_error_count() == 0) {
        if (context.errors().get_warning_count() == 0) {
          JJDocGlobals.info(context, "Grammar documentation generated successfully in " + JJDocGlobals.output_file);
        } else {
          JJDocGlobals.info(context, "Grammar documentation generated with 0 errors and " + context.errors().get_warning_count()
              + " warnings.");
        }
        return 0;
      } else {
        JJDocGlobals.error(context, "Detected " + context.errors().get_error_count() + " errors and "
            + context.errors().get_warning_count() + " warnings.");
        return context.errors().get_error_count() == 0 ? 0 : 1;
      }
    } catch (org.javacc.parser.MetaParseException e) {
      JJDocGlobals.error(context, e.toString());
      JJDocGlobals.error(context, "Detected " + context.errors().get_error_count() + " errors and "
          + context.errors().get_warning_count() + " warnings.");
      return 1;
    } catch (org.javacc.parser.ParseException e) {
      JJDocGlobals.error(context, e.toString());
      JJDocGlobals.error(context, "Detected " + (context.errors().get_error_count() + 1) + " errors and "
          + context.errors().get_warning_count() + " warnings.");
      return 1;
    }
  }

}
