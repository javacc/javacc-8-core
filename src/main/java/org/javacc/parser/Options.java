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
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.TreeSet;
import org.javacc.utils.OptionInfo;
import org.javacc.utils.OptionType;

/** A class with static state that stores all options information. */
public class Options {

  /** Limit subclassing. */
  public Options() {}

  /*
   * NUO = non user option: these are options that are not directly set by the user, but indirectly
   * via some configuration of other user of internal options
   */

  public static final String NUO__INTERPRETER = "INTERPRETER_MODE";
  public static final String NUO__HAS_NAMESPACE = "HAS_NAMESPACE";
  public static final String NUO__LEGACY_EXCEPTION_HANDLING = "LEGACY_EXCEPTION_HANDLING";
  public static final String NUO__NAMESPACE_CLOSE = "NAMESPACE_CLOSE";
  public static final String NUO__NAMESPACE_OPEN = "NAMESPACE_OPEN";
  public static final String NUO__PARSER_NAME = "PARSER_NAME";
  public static final String NUO__PARSER_NAME_UPPER_CASE = "PARSER_NAME_UPPER_CASE";

  /*
   * UO = user option: these are options that are set by the user
   */
  // TODO refaire une passe sur l'utilisation dans les templates!!!
  /** java */
  public static final String UO__BUILD_PARSER = "BUILD_PARSER";

  /** java, csharp, cpp */
  public static final String UO__BUILD_TOKEN_MANAGER = "BUILD_TOKEN_MANAGER";

  /** java, cpp */
  public static final String UO__CACHE_TOKENS = "CACHE_TOKENS";

  /** core only */
  static final String UO__CHOICE_AMBIGUITY_CHECK = "CHOICE_AMBIGUITY_CHECK";

  /** core only */
  static final String UO__CODE_GENERATOR = "CODE_GENERATOR";

  /** core only */
  static final String UO__COMMON_TOKEN_ACTION = "COMMON_TOKEN_ACTION";

  /** java, csharp, cpp */
  public static final String UO__DEBUG_LOOKAHEAD = "DEBUG_LOOKAHEAD";

  /** java, csharp, cpp */
  public static final String UO__DEBUG_PARSER = "DEBUG_PARSER";

  /** core only */
  static final String UO__DEBUG_TOKEN_MANAGER = "DEBUG_TOKEN_MANAGER";

  /** java, cpp */
  public static final String UO__DEPTH_LIMIT = "DEPTH_LIMIT";

  /** java, csharp, cpp */
  public static final String UO__ERROR_REPORTING = "ERROR_REPORTING";

  /** core only */
  public static final String UO__FORCE_LA_CHECK = "FORCE_LA_CHECK";

  // TODO deprecate / remove
  /** not used jdk 1.5 */
  static final String UO__GENERATE_ANNOTATIONS = "GENERATE_ANNOTATIONS";

  // TODO remove ?
  /** java */
  static final String UO__GENERATE_BOILERPLATE = "GENERATE_BOILERPLATE";

  // TODO deprecate / remove
  /** not used jdk 1.4 */
  static final String UO__GENERATE_CHAINED_EXCEPTION = "GENERATE_CHAINED_EXCEPTION";

  // TODO deprecate / remove
  /** not used jdk 1.5 */
  static final String UO__GENERATE_GENERICS = "GENERATE_GENERICS";

  // TODO deprecate / remove
  /** not used jdk 1.5 */
  static final String UO__GENERATE_STRING_BUILDER = "GENERATE_STRING_BUILDER";

  /** core only */
  static final String UO__GRAMMAR_ENCODING = "GRAMMAR_ENCODING";

  /** java, csharp, cpp */
  public static final String UO__IGNORE_ACTIONS = "IGNORE_ACTIONS";

  /** core only */
  static final String UO__IGNORE_CASE = "IGNORE_CASE";

  // TODO deprecate replace table driven remove
  /** java */
  public static final String UO__JAVA_TEMPLATE_TYPE = "JAVA_TEMPLATE_TYPE";

  // TODO see for csharp / cpp equivalent
  /** java */
  public static final String UO__JAVA_UNICODE_ESCAPE = "JAVA_UNICODE_ESCAPE";

  // TODO deprecate remove
  /** core only */
  public static final String UO__JDK_VERSION = "JDK_VERSION";

  /** java */
  public static final String UO__KEEP_LINE_COLUMN = "KEEP_LINE_COLUMN";

  public static final String UO__LOOKAHEAD = "LOOKAHEAD";

  /** java, csharp, cpp */
  public static final String UO__NO_DFA = "NO_DFA";

  /** core only */
  static final String UO__OTHER_AMBIGUITY_CHECK = "OTHER_AMBIGUITY_CHECK";

  /** java, csharp, cpp */
  public static final String UO__OUTPUT_DIRECTORY = "OUTPUT_DIRECTORY";

  /** core only */
  public static final String UO__SANITY_CHECK = "SANITY_CHECK";

  /** java, csharp, cpp */
  public static final String UO__STATIC = "STATIC";

  /** java, csharp, cpp */
  public static final String UO__SUPPORT_CLASS_VISIBILITY_PUBLIC =
      "SUPPORT_CLASS_VISIBILITY_PUBLIC";

  /** java */
  public static final String UO__TOKEN_EXTENDS = "TOKEN_EXTENDS";

  /** java, csharp */
  public static final String UO__TOKEN_MANAGER_SUPER_CLASS = "TOKEN_MANAGER_SUPER_CLASS";

  /** java, csharp, cpp */
  public static final String UO__TOKEN_MANAGER_USES_PARSER = "TOKEN_MANAGER_USES_PARSER";

  // TODO see for csharp / cpp equivalent
  /** core only */
  public static final String UO__UNICODE_INPUT = "UNICODE_INPUT";

  /** java */
  public static final String UO__USER_CHAR_STREAM = "USER_CHAR_STREAM";

  /** java, cpp */
  public static final String UO__USER_TOKEN_MANAGER = "USER_TOKEN_MANAGER";

  /** cpp */
  public static final String UO__LIBRARY = "LIBRARY";

  /** csharp, cpp */
  public static final String UO__NAMESPACE = "NAMESPACE";

  /** cpp */
  public static final String UO__PARSER_INCLUDE = "PARSER_INCLUDE";

  /** cpp */
  public static final String UO__STACK_LIMIT = "STACK_LIMIT";

  // TODO add getter
  /** cpp */
  public static final String UO__STOP_ON_FIRST_ERROR = "STOP_ON_FIRST_ERROR";

  /** cpp */
  public static final String UO__TOKEN_CLASS = "TOKEN_CLASS";

  /** cpp */
  public static final String UO__TOKEN_CONSTANTS_INCLUDE = "TOKEN_CONSTANTS_INCLUDE";

  /** cpp */
  public static final String UO__TOKEN_CONSTANTS_NAMESPACE = "TOKEN_CONSTANTS_NAMESPACE";

  /** nowhere */
  public static final String UO__TOKEN_FACTORY = "TOKEN_FACTORY";

  /** cpp */
  public static final String UO__TOKEN_INCLUDE = "TOKEN_INCLUDE";

  /** cpp */
  public static final String UO__TOKEN_MANAGER_INCLUDE = "TOKEN_MANAGER_INCLUDE";

  /** cpp */
  public static final String UO__TOKEN_NAMESPACE = "TOKEN_NAMESPACE";

  /** cpp */
  public static final String UO__CPP_USE_ARRAY = "CPP_USE_ARRAY";

  /*
   * UOV = user option value: these are some values of options that are set by the user
   */

  /**
   * 2013/07/22 -- GWT Compliant Output -- no external dependencies on GWT, but generated code adds
   * loose coupling to IO, for 6.1 release, this is opt-in, moving forward to 7.0, after thorough
   * testing, this will likely become the default option with classic being deprecated
   */
  public static final String UOV__JAVA_TEMPLATE_TYPE__MODERN = "modern";

  /**
   * The old style of Java code generation (tight coupling of code to Java IO classes - not GWT
   * compatible)
   */
  public static final String UOV__JAVA_TEMPLATE_TYPE__CLASSIC = "classic";

  /**
   * The (lexicographically ordered) (and unmodifiable) set of all possible (user & internal)
   * options with their default values.
   */
  private static final Set<OptionInfo> legalOptions;

  private static final Integer ZERO_0 = Integer.valueOf(0);
  private static final Integer ONE_1 = Integer.valueOf(1);
  private static final Integer TWO_2 = Integer.valueOf(2);
  private static final OptionType OT_BOOLEAN = OptionType.BOOLEAN;
  private static final OptionType OT_INTEGER = OptionType.INTEGER;
  private static final OptionType OT_STRING = OptionType.STRING;

  /* Initialize the userOptions set */
  // TODO see why some options are not included
  static {
    final TreeSet<OptionInfo> ts = new TreeSet<>();

    ts.add(new OptionInfo(NUO__HAS_NAMESPACE, OT_BOOLEAN, Boolean.FALSE));
    //    ts.add(new OptionInfo(NUO__INTERPRETER, OT_BOOLEAN, Boolean.FALSE));
    ts.add(new OptionInfo(NUO__LEGACY_EXCEPTION_HANDLING, OT_BOOLEAN, Boolean.TRUE)); // classic
    //    ts.add(new OptionInfo(NUO__NAMESPACE_CLOSE, OT_BOOLEAN, Boolean.FALSE));
    //    ts.add(new OptionInfo(NUO__NAMESPACE_OPEN, OT_BOOLEAN, Boolean.FALSE));
    //    ts.add(new OptionInfo(NUO__PARSER_NAME, OT_STRING, ""));
    //    ts.add(new OptionInfo(NUO__PARSER_NAME_UPPER_CASE, OT_BOOLEAN, Boolean.FALSE));
    ts.add(new OptionInfo(UO__BUILD_PARSER, OT_BOOLEAN, Boolean.TRUE));
    ts.add(new OptionInfo(UO__BUILD_TOKEN_MANAGER, OT_BOOLEAN, Boolean.TRUE));
    ts.add(new OptionInfo(UO__CACHE_TOKENS, OT_BOOLEAN, Boolean.FALSE));
    ts.add(new OptionInfo(UO__CHOICE_AMBIGUITY_CHECK, OT_INTEGER, TWO_2));
    ts.add(new OptionInfo(UO__CODE_GENERATOR, OT_STRING, ""));
    ts.add(new OptionInfo(UO__COMMON_TOKEN_ACTION, OT_BOOLEAN, Boolean.FALSE));
    ts.add(new OptionInfo(UO__CPP_USE_ARRAY, OT_BOOLEAN, Boolean.FALSE));
    ts.add(new OptionInfo(UO__DEBUG_LOOKAHEAD, OT_BOOLEAN, Boolean.FALSE));
    ts.add(new OptionInfo(UO__DEBUG_PARSER, OT_BOOLEAN, Boolean.FALSE));
    ts.add(new OptionInfo(UO__DEBUG_TOKEN_MANAGER, OT_BOOLEAN, Boolean.FALSE));
    ts.add(new OptionInfo(UO__DEPTH_LIMIT, OT_INTEGER, ZERO_0));
    ts.add(new OptionInfo(UO__ERROR_REPORTING, OT_BOOLEAN, Boolean.TRUE));
    ts.add(new OptionInfo(UO__FORCE_LA_CHECK, OT_BOOLEAN, Boolean.FALSE));
    ts.add(new OptionInfo(UO__GENERATE_ANNOTATIONS, OT_BOOLEAN, Boolean.FALSE));
    ts.add(new OptionInfo(UO__GENERATE_BOILERPLATE, OT_BOOLEAN, Boolean.TRUE));
    ts.add(new OptionInfo(UO__GENERATE_CHAINED_EXCEPTION, OT_BOOLEAN, Boolean.FALSE));
    ts.add(new OptionInfo(UO__GENERATE_GENERICS, OT_BOOLEAN, Boolean.FALSE));
    ts.add(new OptionInfo(UO__GENERATE_STRING_BUILDER, OT_BOOLEAN, Boolean.FALSE));
    ts.add(new OptionInfo(UO__GRAMMAR_ENCODING, OT_STRING, ""));
    ts.add(new OptionInfo(UO__IGNORE_ACTIONS, OT_BOOLEAN, Boolean.FALSE));
    ts.add(new OptionInfo(UO__IGNORE_CASE, OT_BOOLEAN, Boolean.FALSE));
    ts.add(new OptionInfo(UO__JAVA_TEMPLATE_TYPE, OT_STRING, UOV__JAVA_TEMPLATE_TYPE__CLASSIC));
    ts.add(new OptionInfo(UO__JAVA_UNICODE_ESCAPE, OT_BOOLEAN, Boolean.FALSE));
    ts.add(new OptionInfo(UO__JDK_VERSION, OT_STRING, "1.5"));
    ts.add(new OptionInfo(UO__KEEP_LINE_COLUMN, OT_BOOLEAN, Boolean.TRUE));
    ts.add(new OptionInfo(UO__LIBRARY, OT_STRING, ""));
    ts.add(new OptionInfo(UO__LOOKAHEAD, OT_INTEGER, ONE_1));
    ts.add(new OptionInfo(UO__NAMESPACE, OT_STRING, ""));
    ts.add(new OptionInfo(UO__NO_DFA, OT_BOOLEAN, Boolean.FALSE));
    ts.add(new OptionInfo(UO__OTHER_AMBIGUITY_CHECK, OT_INTEGER, ONE_1));
    ts.add(new OptionInfo(UO__OUTPUT_DIRECTORY, OT_STRING, "."));
    ts.add(new OptionInfo(UO__PARSER_INCLUDE, OT_STRING, ""));
    ts.add(new OptionInfo(UO__SANITY_CHECK, OT_BOOLEAN, Boolean.TRUE));
    ts.add(new OptionInfo(UO__STACK_LIMIT, OT_STRING, ""));
    ts.add(new OptionInfo(UO__STATIC, OT_BOOLEAN, Boolean.TRUE));
    ts.add(new OptionInfo(UO__STOP_ON_FIRST_ERROR, OT_BOOLEAN, Boolean.FALSE));
    ts.add(new OptionInfo(UO__SUPPORT_CLASS_VISIBILITY_PUBLIC, OT_BOOLEAN, Boolean.TRUE));
    ts.add(new OptionInfo(UO__TOKEN_CLASS, OT_STRING, ""));
    ts.add(new OptionInfo(UO__TOKEN_CONSTANTS_INCLUDE, OT_STRING, ""));
    ts.add(new OptionInfo(UO__TOKEN_CONSTANTS_NAMESPACE, OT_STRING, ""));
    ts.add(new OptionInfo(UO__TOKEN_EXTENDS, OT_STRING, ""));
    ts.add(new OptionInfo(UO__TOKEN_FACTORY, OT_STRING, ""));
    ts.add(new OptionInfo(UO__TOKEN_INCLUDE, OT_STRING, ""));
    ts.add(new OptionInfo(UO__TOKEN_MANAGER_INCLUDE, OT_STRING, ""));
    //    ts.add(new OptionInfo(UO__TOKEN_MANAGER_SUPER_CLASS, OT_STRING, ""));
    ts.add(new OptionInfo(UO__TOKEN_MANAGER_USES_PARSER, OT_BOOLEAN, Boolean.FALSE));
    ts.add(new OptionInfo(UO__TOKEN_NAMESPACE, OT_STRING, ""));
    ts.add(new OptionInfo(UO__UNICODE_INPUT, OT_BOOLEAN, Boolean.FALSE));
    ts.add(new OptionInfo(UO__USER_CHAR_STREAM, OT_BOOLEAN, Boolean.FALSE));
    ts.add(new OptionInfo(UO__USER_TOKEN_MANAGER, OT_BOOLEAN, Boolean.FALSE));

    legalOptions = Collections.unmodifiableSet(ts);
  }

  /**
   * The map of option names (Strings) to values (Integer, Boolean, String as Object) defining the
   * (user & internal) resulting options.<br>
   * Loaded from {@link #legalOptions}, then from the grammar file, then from the command line
   * arguments.<br>
   * NOT-THREAD-SAFE.
   */
  public static Map<String, Object> resOptions = null;

  /**
   * Keep track of what options were set from command line arguments. We use this to see if the
   * options set from the command line and the ones set in the grammar files clash in any way.<br>
   * NOT-THREAD-SAFE.
   */
  private static Set<String> cmdLineSettings = null;

  /**
   * Keep track of what options were set from the grammar file. We use this to see if the options
   * set from the command line and the ones set in the grammar files clash in any way.<br>
   * NOT-THREAD-SAFE.
   */
  private static Set<String> grammarFileSettings = null;

  /** Initialize map and sets */
  public static void init() {
    resOptions = new HashMap<>();
    cmdLineSettings = new HashSet<>();
    grammarFileSettings = new HashSet<>();

    legalOptions.forEach(op -> resOptions.put(op.getName(), op.getDefault()));

    // now in legalOptions
    //    final Object jtt = resOptions.get(UO__JAVA_TEMPLATE_TYPE);
    //    final boolean isClassic = UOV__JAVA_TEMPLATE_TYPE__CLASSIC.equals(jtt);
    //    resOptions.put(NUO__LEGACY_EXCEPTION_HANDLING, isClassic);
  }

  /** Convenience method to retrieve integer options. */
  public static int intValue(final String option) {
    return ((Integer) resOptions.get(option)).intValue();
  }

  /** Convenience method to retrieve boolean options. */
  public static boolean booleanValue(final String option) {
    final Object o = resOptions.get(option);
    return (o != null) && ((Boolean) o).booleanValue();
  }

  /** Convenience method to retrieve string options. */
  public static String stringValue(final String option) {
    return (String) resOptions.get(option);
  }

  /** Convenience method to retrieve string list options. */
  @SuppressWarnings("unchecked")
  public static List<String> stringListValue(final String option) {
    final Object object = resOptions.get(option);
    if (object instanceof List<?>) {
      return (List<String>) object;
    }
    return null;
  }

  /**
   * Returns a copy of the resulting options.<br>
   * NOT-THREAD-SAFE.
   */
  public static Map<String, Object> getOptions() {
    return new HashMap<>(resOptions);
  }

  /**
   * Returns a string representation of the specified options.<br>
   * Used when, for example, generating Token.java to record the JavaCC options that were used to
   * generate the file.
   *
   * @param options - the options of interest, eg {"UO__STATIC", "UO__CACHE_TOKENS"}
   * @return the string representation of the options, eg "STATIC=true, CACHE_TOKENS=false"
   */
  public static String fmtOptionsArray(final String[] options) {
    final StringBuilder sb = new StringBuilder(32);
    for (final String opt : options) {
      sb.append(opt);
      sb.append('=');
      sb.append(resOptions.get(opt));
      sb.append(",");
    }
    sb.setLength(sb.length() - 1);
    return sb.toString();
  }

  /**
   * Determine if a given command line argument might be an option flag. Command line options start
   * with a dash&nbsp;(-).
   *
   * @param arg - The command line argument to examine
   * @return True when the argument looks like an option flag, false otherwise
   */
  public static boolean canArgBeAnOption(final String arg) {
    return (arg != null) && (arg.length() > 1) && (arg.charAt(0) == '-');
  }

  /**
   * Helper method to handle (hard coded) cases where the value(s) of an option has changed over
   * time.<br>
   * If the user has supplied an option with the old value, it will be converted to the new value.
   *
   * @param name - The option name
   * @param oldValue - The old value
   * @return The new value
   */
  private static Object upgradeValue(final String name, Object oldValue) {
    if (name.equalsIgnoreCase("NODE_FACTORY") && (oldValue.getClass() == Boolean.class)) {
      if (((Boolean) oldValue).booleanValue()) {
        oldValue = "*";
      } else {
        oldValue = "";
      }
    }
    return oldValue;
  }

  /**
   * Process a single grammar file option: parsed and stored in the resulting map and the grammar
   * file set.
   *
   * @param nameloc - the name location (token)
   * @param valueloc - the value location (token)
   * @param name - the name
   * @param value - the value
   * @param context - the generation context
   */
  public static void processGrammarFileOption(
      final Object nameloc,
      final Object valueloc,
      final String name,
      Object value,
      final Context context) {

    final String ucName = name.toUpperCase();
    final JavaCCErrors e = context.errors();
    if (!resOptions.containsKey(ucName)) {
      e.warning(nameloc, "Bad option name \"" + name + "\".  Will be ignored.");
      return;
    }

    final Object existingValue = resOptions.get(ucName);
    value = upgradeValue(name, value);

    if (existingValue != null) {

      final boolean isIndirectProperty = ucName.equalsIgnoreCase(NUO__LEGACY_EXCEPTION_HANDLING);

      Object object = null;
      if (value instanceof List) {
        object = ((List<?>) value).get(0);
      } else {
        object = value;
      }
      final boolean isValidInteger =
          (object instanceof Integer) && (((Integer) value).intValue() <= 0);
      if (isIndirectProperty || (existingValue.getClass() != object.getClass()) || isValidInteger) {
        e.warning(
            valueloc, "Bad option value \"" + value + "\" for \"" + name + "\".  Will be ignored.");
        return;
      }

      if (grammarFileSettings.contains(ucName)) {
        e.warning(nameloc, "Duplicate option setting for \"" + name + "\".  Will be ignored.");
        return;
      }

      if (cmdLineSettings.contains(ucName)) {
        if (!existingValue.equals(value)) {
          e.warning(
              nameloc,
              "Command line setting of \"" + name + "\" modifies option value in grammar file.");
        }
        return;
      }
    }

    resOptions.put(ucName, value);
    grammarFileSettings.add(ucName);

    // Special cases

    if (ucName.equalsIgnoreCase(UO__JAVA_TEMPLATE_TYPE)) {
      final String templateType = (String) value;
      if (!isValidJavaTemplateType(templateType)) {
        e.warning(
            valueloc,
            "Bad option value \""
                + value
                + "\" for \""
                + name
                + "\".  Will be ignored. Valid options are: "
                + getAllValidJavaTemplateTypes());
        return;
      }
      final boolean isLegacy = UOV__JAVA_TEMPLATE_TYPE__CLASSIC.equals(templateType);
      resOptions.put(NUO__LEGACY_EXCEPTION_HANDLING, isLegacy);
    } else if (ucName.equalsIgnoreCase(UO__NAMESPACE)) {
      processNamespaceOption((String) value);
    }
  }

  private static String getAllValidJavaTemplateTypes() {
    return Arrays.toString(
        supportedJavaTemplateTypes.toArray(new String[supportedJavaTemplateTypes.size()]));
  }

  /**
   * Process a single command-line option: parsed and stored in the resulting map and the command
   * line set;.
   *
   * @param arg - the command line argument to be an option
   */
  public static void processCmdLineOption(final String arg) {
    // Look for the first ":" or "=", which will separate the option name from its value (if any).
    final String s = arg.charAt(0) == '-' ? arg.substring(1) : arg;
    final int index1 = s.indexOf('=');
    final int index2 = s.indexOf(':');
    final int index;
    if (index1 < 0) {
      index = index2;
    } else if (index2 < 0) {
      index = index1;
    } else if (index1 < index2) {
      index = index1;
    } else {
      index = index2;
    }

    String name;
    Object value;
    if (index < 0) {
      name = s.toUpperCase();
      if (resOptions.containsKey(name)) {
        value = Boolean.TRUE;
      } else if ((name.length() > 2) && (name.charAt(0) == 'N') && (name.charAt(1) == 'O')) {
        value = Boolean.FALSE;
        name = name.substring(2);
      } else {
        System.out.println("Warning: Bad option \"" + arg + "\".  Will be ignored.");
        return;
      }
    } else {
      name = s.substring(0, index).toUpperCase();
      if (s.substring(index + 1).equalsIgnoreCase("TRUE")) {
        value = Boolean.TRUE;
      } else if (s.substring(index + 1).equalsIgnoreCase("FALSE")) {
        value = Boolean.FALSE;
      } else {
        try {
          final int i = Integer.parseInt(s.substring(index + 1));
          if (i <= 0) {
            System.out.println("Warning: Bad option value in \"" + arg + "\".  Will be ignored.");
            return;
          }
          value = Integer.valueOf(i);
        } catch (final NumberFormatException e) {
          value = s.substring(index + 1);
          if (s.length() > (index + 2)) {
            // i.e., there is space for two '"'s in value
            if ((s.charAt(index + 1) == '"') && (s.charAt(s.length() - 1) == '"')) {
              // remove the two '"'s.
              value = s.substring(index + 2, s.length() - 1);
            }
          }
        }
      }
    }

    if (!resOptions.containsKey(name)) {
      System.out.println("Warning: Bad option \"" + arg + "\".  Will be ignored.");
      return;
    }
    final Object valOrig = resOptions.get(name);
    if (value.getClass() != valOrig.getClass()) {
      System.out.println("Warning: Bad option value in \"" + arg + "\".  Will be ignored.");
      return;
    }
    if (cmdLineSettings.contains(name)) {
      System.out.println("Warning: Duplicate option setting \"" + arg + "\".  Will be ignored.");
      return;
    }

    value = upgradeValue(name, value);

    resOptions.put(name, value);
    cmdLineSettings.add(name);
    if (name.equalsIgnoreCase(UO__NAMESPACE)) {
      processNamespaceOption((String) value);
    }
  }

  /**
   * Checks for conflicting combinations of options and changes the weakest.
   *
   * @param context - the code generation context
   */
  public static void normalize(final Context context) {
    if (getDebugLookahead() && !getDebugParser()) {
      if (cmdLineSettings.contains(UO__DEBUG_PARSER)
          || grammarFileSettings.contains(UO__DEBUG_PARSER)) {
        context
            .errors()
            .warning(
                "True setting of option DEBUG_LOOKAHEAD overrides "
                    + "false setting of option DEBUG_PARSER.");
      }
      resOptions.put(UO__DEBUG_PARSER, Boolean.TRUE);
    }

    // Now set the "GENERATE" options from the supplied (or default) JDKversion

    resOptions.put(UO__GENERATE_CHAINED_EXCEPTION, Boolean.valueOf(jdkVersionAtLeast(1.4)));
    resOptions.put(UO__GENERATE_GENERICS, Boolean.valueOf(jdkVersionAtLeast(1.5)));
    resOptions.put(UO__GENERATE_STRING_BUILDER, Boolean.valueOf(jdkVersionAtLeast(1.5)));
    resOptions.put(UO__GENERATE_ANNOTATIONS, Boolean.valueOf(jdkVersionAtLeast(1.5)));
  }

  /**
   * Find the build parser option value.
   *
   * @return The build parser option value
   */
  public static boolean getBuildParser() {
    return booleanValue(UO__BUILD_PARSER);
  }

  /**
   * Find the build token manager option value.
   *
   * @return The build token manager option value
   */
  public static boolean getBuildTokenManager() {
    return booleanValue(UO__BUILD_TOKEN_MANAGER);
  }

  /**
   * Find the cache tokens option value.
   *
   * @return The cache tokens option value
   */
  public static boolean getCacheTokens() {
    return booleanValue(UO__CACHE_TOKENS);
  }

  /**
   * Find the the char stream name.
   *
   * @return The char stream name
   */
  public static String getCharStreamName() {
    if (getUserCharStream()) {
      return "CharStream";
    } else {
      return getJavaUnicodeEscape() ? "JavaCharStream" : "SimpleCharStream";
    }
  }

  /**
   * Find the choice ambiguity check option value.
   *
   * @return The choice ambiguity check option value
   */
  static int getChoiceAmbiguityCheck() {
    return intValue(UO__CHOICE_AMBIGUITY_CHECK);
  }

  /**
   * Find the code generator option value.
   *
   * @return The code generator option value
   */
  public static String getCodeGenerator() {
    final String retVal = stringValue(UO__CODE_GENERATOR);
    return booleanValue(NUO__INTERPRETER) || retVal.equals("") ? null : retVal;
  }

  /**
   * Find the common token action option value.
   *
   * @return The common token action option value
   */
  static boolean getCommonTokenAction() {
    return booleanValue(UO__COMMON_TOKEN_ACTION);
  }

  /**
   * Find the use array option value.
   *
   * @return The use array option value
   */
  public static boolean getCppUseArray() {
    return booleanValue(UO__CPP_USE_ARRAY);
  }

  /**
   * Find the debug lookahead option value.
   *
   * @return The debug lookahead option value
   */
  public static boolean getDebugLookahead() {
    return booleanValue(UO__DEBUG_LOOKAHEAD);
  }

  /**
   * Find the debug token manager option value.
   *
   * @return The debug token manager option value
   */
  static boolean getDebugTokenManager() {
    return booleanValue(UO__DEBUG_TOKEN_MANAGER);
  }

  /**
   * Find the debug parser option value.
   *
   * @return The debug parser option value
   */
  public static boolean getDebugParser() {
    return booleanValue(UO__DEBUG_PARSER);
  }

  /**
   * Find the depth limit option value.
   *
   * @return The depth limit option value
   */
  public static int getDepthLimit() {
    return intValue(UO__DEPTH_LIMIT);
  }

  /**
   * Find the error reporting option value.
   *
   * @return The error reporting option value
   */
  public static boolean getErrorReporting() {
    return booleanValue(UO__ERROR_REPORTING);
  }

  /**
   * Find the force lookahead check option value.
   *
   * @return The force lookahead option value
   */
  static boolean getForceLaCheck() {
    return booleanValue(UO__FORCE_LA_CHECK);
  }

  /**
   * Find the generate annotations option value.
   *
   * @return The generate annotations option value
   */
  static boolean getGenerateAnnotations() {
    return booleanValue(UO__GENERATE_ANNOTATIONS);
  }

  /**
   * Find the generate boilerplate code option value.
   *
   * @return The generate boilerplate code option value
   */
  public static boolean getGenerateBoilerplateCode() {
    return booleanValue(UO__GENERATE_BOILERPLATE);
  }

  /**
   * Find the generate chained exception option value.
   *
   * @return The generate chained exception option value
   */
  public static boolean getGenerateChainedException() {
    return booleanValue(UO__GENERATE_CHAINED_EXCEPTION);
  }

  /**
   * Find the generate generics option value.
   *
   * @return The generate generics option value
   */
  public static boolean getGenerateGenerics() {
    return booleanValue(UO__GENERATE_GENERICS);
  }

  /**
   * Find the generate StringBuilder option value.
   *
   * @return The generate StringBuilder option value
   */
  static boolean getGenerateStringBuilder() {
    return booleanValue(UO__GENERATE_STRING_BUILDER);
  }

  /**
   * Find the file encoding, which will be the grammar encoding option value if set, otherwise the
   * file.encoding system property.
   *
   * @return The file encoding (e.g, UTF-8, ISO_8859-1, MacRoman)
   */
  public static String getGrammarEncoding() {
    if (stringValue(UO__GRAMMAR_ENCODING).equals("")) {
      return System.getProperties().getProperty("file.encoding");
    } else {
      return stringValue(UO__GRAMMAR_ENCODING);
    }
  }

  /**
   * Find the ignore case option value.
   *
   * @return The ignore case option value
   */
  public static boolean getIgnoreCase() {
    return booleanValue(UO__IGNORE_CASE);
  }

  /**
   * Find the JDK version option value.
   *
   * @return The jdk version option value
   */
  public static String getJdkVersion() {
    return stringValue(UO__JDK_VERSION);
  }

  /**
   * Find the keep line column option value.
   *
   * @return The keep line column option value
   */
  public static boolean getKeepLineColumn() {
    return booleanValue(UO__KEEP_LINE_COLUMN);
  }

  public static String getJavaTemplateType() {
    return stringValue(UO__JAVA_TEMPLATE_TYPE);
  }

  /**
   * Find the Java unicode escape option value.
   *
   * @return The Java unicode escape option value
   */
  public static boolean getJavaUnicodeEscape() {
    return booleanValue(UO__JAVA_UNICODE_ESCAPE);
  }

  /**
   * Find the legacy exception handling option value.
   *
   * <p>Since 6.1 JavaCC throws subclasses of {@link RuntimeException} rather than {@link Error} (by
   * default), as {@link Error} typically lead to the closing down of the parent VM and are only to
   * be used in extreme circumstances (failure of parsing is generally not regarded as such).<br>
   * If this option is set to true then {@link Error}s will be thrown (for compatibility with older
   * .jj files), and if set to false then {@link RuntimeException} will be thrown.
   *
   * @return legacy exception handling option value
   */
  public static boolean getLegacyExceptionHandling() {
    return booleanValue(NUO__LEGACY_EXCEPTION_HANDLING);
  }

  /**
   * Find the library option value.
   *
   * @return The library option value
   */
  public static String getLibrary() {
    return stringValue(UO__LIBRARY);
  }

  /**
   * Find the global lookahead option value.
   *
   * @return The global lookahead option value
   */
  public static int getLookahead() {
    return intValue(UO__LOOKAHEAD);
  }

  /**
   * Find the namespace option value.
   *
   * @return The namespace option value
   */
  public static String getNamespace() {
    return stringValue(UO__NAMESPACE);
  }

  /**
   * Find the has namespace option value.
   *
   * @return The has namespace option value
   */
  public static boolean hasNamespace() {
    return booleanValue(NUO__HAS_NAMESPACE);
  }

  /**
   * Find the no dfa option value.
   *
   * @return The no dfa option value
   */
  public static boolean getNoDfa() {
    return booleanValue(UO__NO_DFA);
  }

  public static Pair<String, String> getOpenCloseNamespace(final String namespace) {
    Pair<String, String> pair = null;
    if (namespace.length() > 0) {
      final StringTokenizer st = new StringTokenizer(namespace, "::");
      String opening = st.nextToken() + " {";
      String closing = "}";
      while (st.hasMoreTokens()) {
        opening = opening + "\nnamespace " + st.nextToken() + " {";
        closing = closing + "\n}";
      }
      pair = new Pair<String, String>(opening, closing);
    }
    return pair;
  }

  /**
   * Find the other ambiguity check option value.
   *
   * @return The other ambiguity check option value
   */
  static int getOtherAmbiguityCheck() {
    return intValue(UO__OTHER_AMBIGUITY_CHECK);
  }

  /**
   * Find the output directory option value.
   *
   * @return The output directory option value
   */
  public static File getOutputDirectory() {
    return new File(stringValue(UO__OUTPUT_DIRECTORY));
  }

  /**
   * Find the parser include option value.
   *
   * @return The parser include option value
   */
  public static String getParserInclude() {
    return stringValue(UO__PARSER_INCLUDE);
  }

  /**
   * Find the sanity check option value.
   *
   * @return The sanity check option value
   */
  public static boolean getSanityCheck() {
    return booleanValue(UO__SANITY_CHECK);
  }

  /**
   * Find the stack limit option value.
   *
   * @return The stack limit option value, as a string (empty string if limit is 0)
   */
  public static String getStackLimit() {
    final String limit = stringValue(UO__STACK_LIMIT);
    return limit.equals("O") ? "" : limit;
  }

  /**
   * Find the stop on first error option value.
   *
   * @return The stop on first error option value, as a string (empty string if limit is 0)
   */
  public static boolean getStopOnFirstError() {
    return booleanValue(UO__STOP_ON_FIRST_ERROR);
  }

  /**
   * Find the static option value.
   *
   * @return The static option value
   */
  public static boolean getStatic() {
    return booleanValue(UO__STATIC);
  }

  /**
   * Find the support class visibility public option value.
   *
   * @return The support class visibility public option value
   */
  public static boolean getSupportClassVisibilityPublic() {
    return booleanValue(UO__SUPPORT_CLASS_VISIBILITY_PUBLIC);
  }

  /**
   * Find the token class option value.
   *
   * @return The token class option value
   */
  public static String getTokenClass() {
    return stringValue(UO__TOKEN_CLASS);
  }

  /**
   * Find the token constants include option value.
   *
   * @return The token constants include option value
   */
  public static String getTokenConstantsInclude() {
    return stringValue(UO__TOKEN_CONSTANTS_INCLUDE);
  }

  /**
   * Find the token constants namespace option value.
   *
   * @return The token constants namespace option value
   */
  public static String getTokenConstantsNamespace() {
    return stringValue(UO__TOKEN_CONSTANTS_NAMESPACE);
  }

  /**
   * Find the Token superclass option value.
   *
   * @return The Token superclass option value
   */
  public static String getTokenExtends() {
    return stringValue(UO__TOKEN_EXTENDS);
  }

  /**
   * Find the Token factory class option value.
   *
   * @return The Token factory class option value
   */
  public static String getTokenFactory() {
    return stringValue(UO__TOKEN_FACTORY);
  }

  /**
   * Find the token include option value.
   *
   * @return The token include option value
   */
  public static String getTokenInclude() {
    return stringValue(UO__TOKEN_INCLUDE);
  }

  /**
   * Find the token manager include option value.
   *
   * @return The token manager include option value
   */
  public static String getTokenManagerInclude() {
    return stringValue(UO__TOKEN_MANAGER_INCLUDE);
  }

  /**
   * Find the token manager uses parser option value.
   *
   * @return The token manager uses parser option value;
   */
  public static boolean getTokenManagerUsesParser() {
    return booleanValue(UO__TOKEN_MANAGER_USES_PARSER) && !getStatic();
  }

  /**
   * Find the token namespace option value.
   *
   * @return The token namespace option value
   */
  public static String getTokenNamespace() {
    return stringValue(UO__TOKEN_NAMESPACE);
  }

  /**
   * Find the unicode input option value.
   *
   * @return The unicode input option value
   */
  public static boolean getUnicodeInput() {
    return booleanValue(UO__UNICODE_INPUT);
  }

  /**
   * Find the user token manager option value.
   *
   * @return The user token manager value
   */
  public static boolean getUserTokenManager() {
    return booleanValue(UO__USER_TOKEN_MANAGER);
  }

  /**
   * Find the user char stream option value.
   *
   * @return The user char stream option value
   */
  public static boolean getUserCharStream() {
    return booleanValue(UO__USER_CHAR_STREAM);
  }

  /**
   * Determine if the output language is at least the specified version.
   *
   * @param version - the version to check against. E.g. <code>1.5</code>
   * @return true if the output version is at least the specified version, false otherwise
   */
  private static boolean jdkVersionAtLeast(final double version) {
    final double jdkVersion = Double.parseDouble(getJdkVersion());
    // Comparing doubles is safe here, as it is two simple assignments.
    return jdkVersion >= version;
  }

  private static final Set<String> supportedJavaTemplateTypes = new HashSet<>();

  static {
    supportedJavaTemplateTypes.add(UOV__JAVA_TEMPLATE_TYPE__CLASSIC);
    supportedJavaTemplateTypes.add(UOV__JAVA_TEMPLATE_TYPE__MODERN);
  }

  private static boolean isValidJavaTemplateType(final String type) {
    return type == null ? false : supportedJavaTemplateTypes.contains(type.toLowerCase());
  }

  public static void set(final String optionName, final Object optionValue) {
    resOptions.put(optionName, optionValue);
  }

  static void setStringOption(final String optionName, final String optionValue) {
    resOptions.put(optionName, optionValue);
    if (optionName.equalsIgnoreCase(UO__NAMESPACE)) {
      processNamespaceOption(optionValue);
    }
    if (optionName.equalsIgnoreCase(NUO__PARSER_NAME)) {
      setStringOption(NUO__PARSER_NAME_UPPER_CASE, stringValue(NUO__PARSER_NAME).toUpperCase());
    }
  }

  private static void processNamespaceOption(final String optionValue) {
    final Pair<String, String> pair = getOpenCloseNamespace(optionValue);
    if (pair != null) {
      resOptions.put(NUO__HAS_NAMESPACE, Boolean.TRUE);
      resOptions.put(NUO__NAMESPACE_OPEN, pair.getFirst());
      resOptions.put(NUO__NAMESPACE_CLOSE, pair.getSecond());
    }
  }

  public static boolean doesTokenManagerRequireParserAccess() {
    return getTokenManagerUsesParser() && !getStatic();
  }

  /**
   * Get all the user options.
   *
   * @return the set of user options
   */
  static Set<OptionInfo> getUserOptions() {
    return legalOptions;
  }

  /** Return true if the given string is null or of length 0, false otherwise */
  static boolean isNullOrEmpty(final String s) {
    return s == null ? true : s.isEmpty();
  }

  /** Return false if the given string is null or of length 0, false otherwise */
  static boolean isNotEmpty(final String s) {
    return s == null ? false : !s.isEmpty();
  }
}
