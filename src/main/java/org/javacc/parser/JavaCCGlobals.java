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

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import org.javacc.Version;

/**
 * This class contains data created as the result of parsing and semanticizing a JavaCC input file.
 * This data is used by the back-ends of JavaCC as well as any other back-end of JavaCC related
 * tools such as JJTree.
 */
public class JavaCCGlobals {
  
  /** String that identifies the JavaCC generated files. */
  public static final String toolName = "JavaCC";
  
  /** Set to true if this file has been processed by JJTree. */
  public boolean jjtreeGenerated;
  
  /** The list of tools that have participated in generating the input grammar file. */
  public final List<String> toolNames;
  
  /**
   * This prints the banner line when the various tools are invoked.
   * 
   * @param fullName - the tool full name
   * @param ver - the tool version
   */
  public static void bannerLine(final String fullName, final String ver) {
    System.err.print("Java Compiler Compiler Version " + Version.fullVersion + " (" + fullName);
    if (!ver.equals("")) {
      System.err.print(" Version " + ver);
    }
    System.err.println(")");
  }
  
  /** The name of the parser class (what appears in PARSER_BEGIN and PARSER_END). */
  public String cu_name;
  
  /**
   * This is a list of tokens that appear after "PARSER_BEGIN(name)" all the way until (but not
   * including) the opening brace "{" of the class "name".
   */
  public final List<Token> cu_to_insertion_point_1;
  
  /**
   * This is the list of all tokens that appear after the tokens in "cu_to_insertion_point_1" and
   * until (but not including) the closing brace "}" of the class "name".
   */
  public final List<Token> cu_to_insertion_point_2;
  
  /**
   * This is the list of all tokens that appear after the tokens in "cu_to_insertion_point_2" and
   * until "PARSER_END(name)".
   */
  public final List<Token> cu_from_insertion_point_2;
  
  /**
   * A list of all grammar productions - normal and JAVACODE - in the order they appear in the input
   * file. Each entry here will be a subclass of "NormalProduction".
   */
  public final List<NormalProduction> bnfproductions;
  
  /**
   * A symbol table of all grammar productions - normal and JAVACODE. The symbol table is indexed by
   * the name of the left hand side non-terminal. Its contents are of type "NormalProduction".
   */
  public final Map<String, NormalProduction> production_table;
  
  /**
   * A mapping of lexical state strings to their integer internal representation. Integers are
   * stored as java.lang.Integer's.
   */
  final Hashtable<String, Integer> lexstate_S2I;
  
  /**
   * A mapping of the internal integer representations of lexical states to their strings. Integers
   * are stored as java.lang.Integer's.
   */
  public final Hashtable<Integer, String> lexstate_I2S;
  
  /** The declarations to be inserted into the TokenManager class. */
  public List<Token> token_mgr_decls;
  
  /**
   * The list of all TokenProductions from the input file. This list includes implicit
   * TokenProductions that are created for uses of regular expressions within BNF productions.
   */
  public final List<TokenProduction> rexprlist;
  
  /**
   * The total number of distinct tokens. This is therefore one more than the largest assigned token
   * ordinal.
   */
  public int tokenCount;
  
  /**
   * This is a symbol table that contains all named tokens (those that are defined with a label).
   * The index to the table is the image of the label and the contents of the table are of type
   * "RegularExpression".
   */
  final Map<String, RegularExpression> named_tokens_table;
  
  /**
   * Contains the same entries as "named_tokens_table", but this is an ordered list which is ordered
   * by the order of appearance in the input file.
   */
  public final List<RegularExpression> ordered_named_tokens;
  
  /**
   * A mapping of ordinal values (represented as objects of type "Integer") to the corresponding
   * labels (of type "String"). An entry exists for an ordinal value only if there is a labeled
   * token corresponding to this entry. If there are multiple labels representing the same ordinal
   * value, then only one label is stored.
   */
  public final Map<Integer, String> names_of_tokens;
  
  /**
   * A mapping of ordinal values (represented as objects of type "Integer") to the corresponding
   * RegularExpression's.
   */
  public final Map<Integer, RegularExpression> rexps_of_tokens;
  
  /**
   * This is a three-level symbol table that contains all simple tokens (those that are defined
   * using a single string (with or without a label). The index to the first level table is a
   * lexical state which maps to a second level hashtable. The index to the second level hashtable
   * is the string of the simple token converted to upper case, and this maps to a third level
   * hashtable. This third level hashtable contains the actual string of the simple token and maps
   * it to its RegularExpression.
   */
  final Hashtable<String, Hashtable<String, Hashtable<String, RegularExpression>>> simple_tokens_table;
  
  /*
   * maskindex, jj2index, maskVals are variables that are shared between ParseEngine and ParseGen.
   */
  @SuppressWarnings("javadoc") public int               maskindex;
  @SuppressWarnings("javadoc") public int               jj2index;
  @SuppressWarnings("javadoc") public final List<int[]> maskVals;
  
  @SuppressWarnings("javadoc") public boolean lookaheadNeeded;
  
  @SuppressWarnings("javadoc") public Action actForEof;
  @SuppressWarnings("javadoc") public String nextStateForEof;
  @SuppressWarnings("javadoc") public Token  otherLanguageDeclTokenBeg;
  @SuppressWarnings("javadoc") public Token  otherLanguageDeclTokenEnd;
  
  @SuppressWarnings("javadoc") public int cline;
  @SuppressWarnings("javadoc") public int ccol;
  
  /** Standard constructor. */
  public JavaCCGlobals() {
    jjtreeGenerated = false;
    toolNames = new ArrayList<>();
    cu_name = null;
    cu_to_insertion_point_1 = new ArrayList<>();
    cu_to_insertion_point_2 = new ArrayList<>();
    cu_from_insertion_point_2 = new ArrayList<>();
    bnfproductions = new ArrayList<>();
    production_table = new HashMap<>();
    lexstate_S2I = new Hashtable<>();
    lexstate_I2S = new Hashtable<>();
    token_mgr_decls = null;
    rexprlist = new ArrayList<>();
    tokenCount = 0;
    named_tokens_table = new HashMap<>();
    ordered_named_tokens = new ArrayList<>();
    otherLanguageDeclTokenBeg = null;
    otherLanguageDeclTokenEnd = null;
    names_of_tokens = new HashMap<>();
    rexps_of_tokens = new HashMap<>();
    simple_tokens_table = new Hashtable<>();
    maskindex = 0;
    jj2index = 0;
    maskVals = new ArrayList<>();
    cline = 0;
    ccol = 0;
    actForEof = null;
    nextStateForEof = null;
  }
  
  // Some general purpose utilities follow.
  
  /**
   * Returns the identifying string for the file name, given a toolname used to generate it.
   *
   * @param toolName - the tool name
   * @param fileName - the file name
   * @return the identifying string
   */
  public static String getIdString(final String toolName, final String fileName) {
    return JavaCCGlobals.getIdString(Collections.singletonList(toolName), fileName);
  }
  
  /**
   * Returns the identifying string for the file name, given a set of tool names that are used to
   * generate it.
   *
   * @param toolNames - the tool names
   * @param fileName - the file name
   * @return the identifying string
   */
  private static String getIdString(final List<String> toolNames, final String fileName) {
    final String id = String.format("Generated By:%s: Do not edit this line. %s",
        String.join("&", toolNames), fileName);
    if (id.length() <= 200) {
      return id;
    }
    System.out.println("Tool names too long.");
    throw new Error();
  }
  
  /**
   * @param toolName - the tool name
   * @param fileName - the file name
   * @return true if tool name passed is one of the tool names returned by getToolNames(fileName).
   */
  public static boolean isGeneratedBy(final String toolName, final String fileName) {
    final List<String> v = JavaCCGlobals.getToolNames(fileName);
    for (int i = 0; i < v.size(); i++) {
      if (toolName.equals(v.get(i))) {
        return true;
      }
    }
    return false;
  }
  
  /**
   * Returns a list of names of the tools that have been used to generate the given file.
   * 
   * @param fileName - the file name
   * @return the list of tool names
   */
  public static List<String> getToolNames(final String fileName) {
    final char[] buf = new char[256];
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
    }
    catch (final java.io.FileNotFoundException e1) {}
    catch (final java.io.IOException e2) {
      if (total > 0) {
        return JavaCCGlobals.makeToolNameList(new String(buf, 0, total));
      }
    } finally {
      if (stream != null) {
        try {
          stream.close();
        }
        catch (final Exception e3) {}
      }
    }
    
    return new ArrayList<>();
  }
  
  /**
   * Parses a "Generated By ..." given string to extract the tool names.
   * 
   * @param str - a string
   * @return the list of tool names
   */
  private static List<String> makeToolNameList(final String str) {
    final List<String> retVal = new ArrayList<>();
    
    int limit1 = str.indexOf('\n');
    if (limit1 == -1) {
      limit1 = 1000;
    }
    int limit2 = str.indexOf('\r');
    if (limit2 == -1) {
      limit2 = 1000;
    }
    final int limit = (limit1 < limit2) ? limit1 : limit2;
    
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
   * @param str - a string
   * @return the string with characters in some ranges escaped with the UnicodeEscape notation
   */
  public static String add_escapes(final String str) {
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
        final String s = "0000" + Integer.toString(ch, 16);
        retval += "\\u" + s.substring(s.length() - 4, s.length());
      } else {
        retval += ch;
      }
    }
    return retval;
  }
  
  /**
   * @param str - a string
   * @return the string with characters in some ranges escaped with the EscapeSequence notation and
   *         other with the UnicodeEscape notation.
   */
  public static String addUnicodeEscapes(final String str) {
    String retval = "";
    char ch;
    for (int i = 0; i < str.length(); i++) {
      ch = str.charAt(i);
      if (ch < 0x7f) {
        if (ch >= 0x20) {
          retval += ch;
        } else if (ch < 0x10) {
          retval += "\\u000" + Integer.toString(ch, 16);
        } else {
          retval += "\\u00" + Integer.toString(ch, 16);
        }
      } else {
        retval += "\\u0" + Integer.toString(ch, 16);
      }
    }
    return retval;
  }
}
