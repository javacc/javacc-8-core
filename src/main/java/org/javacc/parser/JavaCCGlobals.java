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

package org.javacc.parser;

import org.javacc.Version;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

/**
 * This package contains data created as a result of parsing and semanticizing a
 * JavaCC input file. This data is what is used by the back-ends of JavaCC as
 * well as any other back-end of JavaCC related tools such as JJTree.
 */
public class JavaCCGlobals {

  /**
   * String that identifies the JavaCC generated files.
   */
  public static final String toolName = "JavaCC";

  /**
   * Set to true if this file has been processed by JJTree.
   */
  public boolean             jjtreeGenerated;

  /**
   * The list of tools that have participated in generating the input grammar
   * file.
   */
  public final List<String>  toolNames;

  /**
   * This prints the banner line when the various tools are invoked. This takes
   * as argument the tool's full name and its version.
   */
  public static void bannerLine(String fullName, String ver) {
    System.out.print("Java Compiler Compiler Version " + Version.fullVersion + " (" + fullName);
    if (!ver.equals("")) {
      System.out.print(" Version " + ver);
    }
    System.out.println(")");
  }

  /**
   * The name of the parser class (what appears in PARSER_BEGIN and PARSER_END).
   */
  public String                                                                    cu_name;

  /**
   * This is a list of tokens that appear after "PARSER_BEGIN(name)" all the way
   * until (but not including) the opening brace "{" of the class "name".
   */
  public final List<Token>                                                         cu_to_insertion_point_1;

  /**
   * This is the list of all tokens that appear after the tokens in
   * "cu_to_insertion_point_1" and until (but not including) the closing brace
   * "}" of the class "name".
   */
  public final List<Token>                                                         cu_to_insertion_point_2;

  /**
   * This is the list of all tokens that appear after the tokens in
   * "cu_to_insertion_point_2" and until "PARSER_END(name)".
   */
  public final List<Token>                                                         cu_from_insertion_point_2;

  /**
   * A list of all grammar productions - normal and JAVACODE - in the order they
   * appear in the input file. Each entry here will be a subclass of
   * "NormalProduction".
   */
  public final List<NormalProduction>                                              bnfproductions;

  /**
   * A symbol table of all grammar productions - normal and JAVACODE. The symbol
   * table is indexed by the name of the left hand side non-terminal. Its
   * contents are of type "NormalProduction".
   */
  public final Map<String, NormalProduction>                                       production_table;

  /**
   * A mapping of lexical state strings to their integer internal
   * representation. Integers are stored as java.lang.Integer's.
   */
  final Hashtable<String, Integer>                                                 lexstate_S2I;

  /**
   * A mapping of the internal integer representations of lexical states to
   * their strings. Integers are stored as java.lang.Integer's.
   */
  public final Hashtable<Integer, String>                                          lexstate_I2S;

  /**
   * The declarations to be inserted into the TokenManager class.
   */
  public List<Token>                                                               token_mgr_decls;

  /**
   * The list of all TokenProductions from the input file. This list includes
   * implicit TokenProductions that are created for uses of regular expressions
   * within BNF productions.
   */
  public final List<TokenProduction>                                               rexprlist;

  /**
   * The total number of distinct tokens. This is therefore one more than the
   * largest assigned token ordinal.
   */
  public int                                                                       tokenCount;

  /**
   * This is a symbol table that contains all named tokens (those that are
   * defined with a label). The index to the table is the image of the label and
   * the contents of the table are of type "RegularExpression".
   */
  final Map<String, RegularExpression>                                             named_tokens_table;

  /**
   * Contains the same entries as "named_tokens_table", but this is an ordered
   * list which is ordered by the order of appearance in the input file.
   */
  public final List<RegularExpression>                                             ordered_named_tokens;

  /**
   * A mapping of ordinal values (represented as objects of type "Integer") to
   * the corresponding labels (of type "String"). An entry exists for an ordinal
   * value only if there is a labeled token corresponding to this entry. If
   * there are multiple labels representing the same ordinal value, then only
   * one label is stored.
   */
  public final Map<Integer, String>                                                names_of_tokens;

  /**
   * A mapping of ordinal values (represented as objects of type "Integer") to
   * the corresponding RegularExpression's.
   */
  public final Map<Integer, RegularExpression>                                     rexps_of_tokens;

  /**
   * This is a three-level symbol table that contains all simple tokens (those
   * that are defined using a single string (with or without a label). The index
   * to the first level table is a lexical state which maps to a second level
   * hashtable. The index to the second level hashtable is the string of the
   * simple token converted to upper case, and this maps to a third level
   * hashtable. This third level hashtable contains the actual string of the
   * simple token and maps it to its RegularExpression.
   */
  final Hashtable<String, Hashtable<String, Hashtable<String, RegularExpression>>> simple_tokens_table;


  /**
   * maskindex, jj2index, maskVals are variables that are shared between
   * ParseEngine and ParseGen.
   */
  public int               maskindex;
  public int               jj2index;
  public boolean           lookaheadNeeded;
  public final List<int[]> maskVals;


  public Action actForEof;
  public String nextStateForEof;
  public Token  otherLanguageDeclTokenBeg;
  public Token  otherLanguageDeclTokenEnd;


  public JavaCCGlobals() {
    this.jjtreeGenerated = false;
    this.toolNames = new ArrayList<>();
    this.cu_name = null;
    this.cu_to_insertion_point_1 = new ArrayList<>();
    this.cu_to_insertion_point_2 = new ArrayList<>();
    this.cu_from_insertion_point_2 = new ArrayList<>();
    this.bnfproductions = new ArrayList<>();
    this.production_table = new HashMap<>();
    this.lexstate_S2I = new Hashtable<>();
    this.lexstate_I2S = new Hashtable<>();
    this.token_mgr_decls = null;
    this.rexprlist = new ArrayList<>();
    this.tokenCount = 0;
    this.named_tokens_table = new HashMap<>();
    this.ordered_named_tokens = new ArrayList<>();
    this.otherLanguageDeclTokenBeg = null;
    this.otherLanguageDeclTokenEnd = null;
    this.names_of_tokens = new HashMap<>();
    this.rexps_of_tokens = new HashMap<>();
    this.simple_tokens_table = new Hashtable<>();
    this.maskindex = 0;
    this.jj2index = 0;
    this.maskVals = new ArrayList<>();
    this.cline = 0;
    this.ccol = 0;
    this.actForEof = null;
    this.nextStateForEof = null;
  }


  // Some general purpose utilities follow.

  /**
   * Returns the identifying string for the file name, given a toolname used to
   * generate it.
   *
   * @param toolName
   * @param fileName
   */
  public static String getIdString(String toolName, String fileName) {
    return JavaCCGlobals.getIdString(Collections.singletonList(toolName), fileName);
  }

  /**
   * Returns the identifying string for the file name, given a set of tool names
   * that are used to generate it.
   */
  private static String getIdString(List<String> toolNames, String fileName) {
    String id = String.format("Generated By:%s: Do not edit this line. %s", String.join("&", toolNames), fileName);
    if (id.length() <= 200) {
      return id;
    }
    System.out.println("Tool names too long.");
    throw new Error();
  }

  /**
   * Returns true if tool name passed is one of the tool names returned by
   * getToolNames(fileName).
   */
  public static boolean isGeneratedBy(String toolName, String fileName) {
    List<String> v = JavaCCGlobals.getToolNames(fileName);
    for (int i = 0; i < v.size(); i++) {
      if (toolName.equals(v.get(i))) {
        return true;
      }
    }
    return false;
  }

  private static List<String> makeToolNameList(String str) {
    List<String> retVal = new ArrayList<>();

    int limit1 = str.indexOf('\n');
    if (limit1 == -1) {
      limit1 = 1000;
    }
    int limit2 = str.indexOf('\r');
    if (limit2 == -1) {
      limit2 = 1000;
    }
    int limit = (limit1 < limit2) ? limit1 : limit2;

    String tmp;
    if (limit == 1000) {
      tmp = str;
    } else {
      tmp = str.substring(0, limit);
    }

    if (tmp.indexOf(':') == -1) {
      return retVal;
    }

    tmp = tmp.substring(tmp.indexOf(':') + 1);

    if (tmp.indexOf(':') == -1) {
      return retVal;
    }

    tmp = tmp.substring(0, tmp.indexOf(':'));

    int i = 0, j = 0;

    while ((j < tmp.length()) && ((i = tmp.indexOf('&', j)) != -1)) {
      retVal.add(tmp.substring(j, i));
      j = i + 1;
    }

    if (j < tmp.length()) {
      retVal.add(tmp.substring(j));
    }

    return retVal;
  }

  /**
   * Returns a List of names of the tools that have been used to generate the
   * given file.
   */
  public static List<String> getToolNames(String fileName) {
    char[] buf = new char[256];
    java.io.FileReader stream = null;
    int read, total = 0;

    try {
      stream = new java.io.FileReader(fileName);

      for (;;) {
        if ((read = stream.read(buf, total, buf.length - total)) != -1) {
          if ((total += read) == buf.length) {
            break;
          }
        } else {
          break;
        }
      }

      return JavaCCGlobals.makeToolNameList(new String(buf, 0, total));
    } catch (java.io.FileNotFoundException e1) {} catch (java.io.IOException e2) {
      if (total > 0) {
        return JavaCCGlobals.makeToolNameList(new String(buf, 0, total));
      }
    } finally {
      if (stream != null) {
        try {
          stream.close();
        } catch (Exception e3) {}
      }
    }

    return new ArrayList<>();
  }

  public static String add_escapes(String str) {
    String retval = "";
    char ch;
    for (int i = 0; i < str.length(); i++) {
      ch = str.charAt(i);
      if (ch == '\b') {
        retval += "\\b";
      } else if (ch == '\t') {
        retval += "\\t";
      } else if (ch == '\n') {
        retval += "\\n";
      } else if (ch == '\f') {
        retval += "\\f";
      } else if (ch == '\r') {
        retval += "\\r";
      } else if (ch == '\"') {
        retval += "\\\"";
      } else if (ch == '\'') {
        retval += "\\\'";
      } else if (ch == '\\') {
        retval += "\\\\";
      } else if ((ch < 0x20) || (ch > 0x7e)) {
        String s = "0000" + Integer.toString(ch, 16);
        retval += "\\u" + s.substring(s.length() - 4, s.length());
      } else {
        retval += ch;
      }
    }
    return retval;
  }

  public static String addUnicodeEscapes(String str) {
    String retval = "";
    char ch;
    for (int i = 0; i < str.length(); i++) {
      ch = str.charAt(i);
      if ((ch < 0x20)
          || (ch > 0x7e /* || ch == '\\' -- cba commented out 20140305 */)) {
        String s = "0000" + Integer.toString(ch, 16);
        retval += "\\u" + s.substring(s.length() - 4, s.length());
      } else {
        retval += ch;
      }
    }
    return retval;
  }

  public int cline, ccol;
}
