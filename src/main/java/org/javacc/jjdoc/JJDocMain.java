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
package org.javacc.jjdoc;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import org.javacc.parser.JavaCCGlobals;
import org.javacc.parser.JavaCCParser;
import org.javacc.parser.MetaParseException;
import org.javacc.parser.Options;
import org.javacc.parser.ParseException;

/**
 * The main class for the JJDoc tool.
 */
public final class JJDocMain {
  
  /** Standard constructor. */
  private JJDocMain() {}
  
  /** The JJDoc context. */
  JJDocContext context;
  
  /** The JJDoc tool. */
  JJDoc jjdoc;
  
  /**
   * Outputs a help message.
   * 
   * @param context - the JJDoc context
   */
  void help_message(final JJDocContext context) {
    jjdoc.info(context, "");
    jjdoc.info(context, "    jjdoc option-settings - (to read from standard input)");
    jjdoc.info(context, "OR");
    jjdoc.info(context, "    jjdoc option-settings inputfile (to read from a file)");
    jjdoc.info(context, "");
    jjdoc.info(context, "WHERE");
    jjdoc.info(context, "    \"option-settings\" is a sequence of settings separated by spaces.");
    jjdoc.info(context, "");
    
    jjdoc.info(context, "Each option setting must be of one of the following forms:");
    jjdoc.info(context, "");
    jjdoc.info(context, "    -optionname=value (e.g., -TEXT=false)");
    jjdoc.info(context, "    -optionname:value (e.g., -TEXT:false)");
    jjdoc.info(context, "    -optionname       (equivalent to -optionname=true.  e.g., -TEXT)");
    jjdoc.info(context, "    -NOoptionname     (equivalent to -optionname=false. e.g., -NOTEXT)");
    jjdoc.info(context, "");
    jjdoc.info(context,
        "Option settings are not case-sensitive, so one can say \"-nOtExT\" instead");
    jjdoc.info(context, "of \"-NOTEXT\".  Option values must be appropriate for the corresponding");
    jjdoc.info(context, "option, and must be either an integer, boolean or string value.");
    jjdoc.info(context, "");
    jjdoc.info(context, "The string valued options are:");
    jjdoc.info(context, "");
    jjdoc.info(context, "    OUTPUT_DIRECTORY");
    jjdoc.info(context, "    OUTPUT_FILE");
    jjdoc.info(context, "    CSS");
    jjdoc.info(context, "");
    jjdoc.info(context, "The boolean valued options are:");
    jjdoc.info(context, "");
    jjdoc.info(context, "    ONE_TABLE              (default true)");
    jjdoc.info(context, "    BNF                    (default false)");
    jjdoc.info(context, "    JCC                    (default false)");
    jjdoc.info(context, "    TEXT                   (default false)");
    jjdoc.info(context, "    XTEXT                  (default false)");
    jjdoc.info(context, "");
    
    jjdoc.info(context, "");
    jjdoc.info(context, "EXAMPLES:");
    jjdoc.info(context, "    jjdoc -ONE_TABLE=false mygrammar.jj");
    jjdoc.info(context, "    jjdoc - < mygrammar.jj");
    jjdoc.info(context, "");
    jjdoc.info(context, "ABOUT JJDoc:");
    jjdoc.info(context, "    JJDoc generates JavaDoc documentation from JavaCC grammar files.");
    jjdoc.info(context, "");
    jjdoc.info(context, "    For more information, see the online JJDoc documentation at");
    jjdoc.info(context, "    https://github.com/javacc/doc/JJDoc.html");
  }
  
  /**
   * A standard main that exercises the parser.
   * 
   * @param args - the command line arguments
   */
  public static void main(final String args[]) {
    final int errorcode = JJDocMain.mainProgram(args);
    System.exit(errorcode);
  }
  
  /**
   * The static method to call to exercise the parser from other Java programs.<br>
   * It returns an error code, and passes the output file path to the caller through the last
   * argument.
   * 
   * @param args - the command line arguments
   * @return a return code, negative if errors, zero if generation ok, positive if generation ok but
   *         with parse errors or warnings
   */
  public static int mainProgram(final String args[]) {
    final JJDocMain main = new JJDocMain();
    return main.doMain(args);
  }
  
  /**
   * The private method to call on a JJDocMain instance to exercise the parser from other Java
   * programs.<br>
   * It returns an error code, and passes the output file path to the caller through the last
   * argument.
   * 
   * @param args - the command line arguments
   * @return a return code, negative if errors, zero if generation ok, positive if generation ok but
   *         with parse errors or warnings
   */
  private int doMain(final String args[]) {
    context = new JJDocContext();
    jjdoc = new JJDoc();
    JavaCCGlobals.bannerLine("Documentation Generator", "0.1.4");
    
    JavaCCParser parser = null;
    if (args.length == 0) {
      help_message(context);
      return -1;
      //    } else {
      //      globals.info(context, "(type \"jjdoc\" with no arguments for help)");
    }
    
    final String lastArg = args[args.length - 1];
    if (Options.canArgBeAnOption(lastArg)) {
      jjdoc.error(context, "Last argument \"" + lastArg + "\" is not a filename or \"-\".");
      return -2;
    }
    
    for (int i = 0; i < (args.length - 1); i++) {
      if (!Options.canArgBeAnOption(args[i])) {
        jjdoc.error(context, "Argument \"" + args[i] + "\" must be an option setting.");
        return -4;
      }
      Options.processCmdLineOption(args[i]);
    }
    
    if (!context.getOutputFile().equals("") && !context.getOutputDirectory().equals("")) {
      jjdoc.warn(context, "Output directory setting ignored as output file name is set.");
    }
    
    File inDir = null;
    if (lastArg.equals("-")) {
      jjdoc.info(context, "Reading from standard input . . .");
      jjdoc.input_file = "standard input";
      jjdoc.output_file_path = "standard output";
      parser = new JavaCCParser(new DataInputStream(System.in));
    } else {
      jjdoc.info(context, "Reading from file " + lastArg + " . . .");
      try {
        final File inFile = new File(lastArg);
        if (!inFile.exists()) {
          jjdoc.error(context, "File " + lastArg + " not found.");
          return -8;
        }
        if (inFile.isDirectory()) {
          jjdoc.error(context, lastArg + " is a directory. Please use a valid file name.");
          return -16;
        }
        jjdoc.input_file = inFile.getName();
        inDir = inFile.getParentFile();
        jjdoc.input_directory = inDir.getPath();
        parser = new JavaCCParser(new BufferedReader(
            new InputStreamReader(new FileInputStream(lastArg), Options.getGrammarEncoding())));
      }
      catch (final SecurityException se) {
        jjdoc.error(context, "Security violation while trying to open " + lastArg + ".");
        return -32;
      }
      catch (final FileNotFoundException e) {
        jjdoc.error(context, "File " + lastArg + " not found.");
        return -64;
      }
      catch (final UnsupportedEncodingException e) {
        jjdoc.error(context,
            "Unsupported encoding " + Options.getGrammarEncoding() + " for file " + lastArg + ".");
        return -128;
      }
      catch (final Exception e) {
        jjdoc.error(context, "Encountered exception " + e.getMessage() + ".");
        e.printStackTrace();
        throw e;
      }
    }
    
    try {
      parser.javacc_input(context);
      jjdoc.generate(context);
      // pass output file name to caller through last argument
      args[args.length - 1] = jjdoc.output_file_path;
      //      globals.info(context,
      //          "Passing output file path " + args[args.length - 1] + " to caller.");
      
      // previous parameters errors / warnings are not counted in parse errors / warnings!
      if (context.errors().get_error_count() == 0) {
        if (context.errors().get_warning_count() == 0) {
          jjdoc.info(context, "Grammar documentation generated successfully in "
              + jjdoc.output_file_path + " (passed to caller in last argument).");
        } else {
          jjdoc.info(context,
              "Grammar documentation generated with 0 parse errors and "
                  + context.errors().get_warning_count() + " parse warnings in "
                  + jjdoc.output_file_path + " (passed to caller in last argument).");
        }
        return 0;
      } else {
        jjdoc.error(context, "Detected " + context.errors().get_error_count() + " parse errors and "
            + context.errors().get_warning_count() + " parse warnings.");
        return context.errors().get_error_count() == 0 ? 0 : 1;
      }
    }
    catch (final MetaParseException e) {
      jjdoc.error(context, e.toString());
      jjdoc.error(context, "Detected " + context.errors().get_error_count() + " parse errors and "
          + context.errors().get_warning_count() + " parse warnings.");
      return 2;
    }
    catch (final ParseException e) {
      jjdoc.error(context, e.toString());
      jjdoc.error(context, "Detected " + (context.errors().get_error_count() + 1)
          + " parse errors and " + context.errors().get_warning_count() + " parse warnings.");
      return 4;
    }
    catch (final Exception e) {
      jjdoc.error(context, "Encountered exception " + e.getMessage() + ".");
      e.printStackTrace();
      throw e;
    }
  }
}
