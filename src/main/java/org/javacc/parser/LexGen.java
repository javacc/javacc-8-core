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
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/** Generate lexer. */
public class LexGen {

  public static final String DEFAULT_STATE = "DEFAULT";
  private final Context context;

  // Hashtable of vectors
  private Hashtable<String, List<TokenProduction>> allTpsForState = new Hashtable<>();

  private int[] kinds;
  private int maxOrdinal = 1;
  private String[] newLexState;
  private Action[] actions;
  private Hashtable<String, NfaState> initStates = new Hashtable<>();
  private int totalNumStates;
  private int maxLexStates;

  private NfaState[] singlesToSkip;
  private long[] toMore;
  private long[] toSkip;
  private long[] toSpecial;
  private long[] toToken;
  private int defaultLexState;
  private RegularExpression[] rexprs;
  private int[] initMatch;
  private boolean[] canLoop;
  private boolean[] canReachOnMore;
  private boolean[] hasNfa;
  private NfaState initialState;

  public LexGen(final Context context) {
    this.context = context;
    allTpsForState = new Hashtable<>();
    kinds = null;
    maxOrdinal = 1;
    newLexState = null;
    actions = null;
    initStates = new Hashtable<>();
    totalNumStates = 0;
    maxLexStates = 0;
    singlesToSkip = null;
    toMore = null;
    toSkip = null;
    toSpecial = null;
    toToken = null;
    defaultLexState = 0;
    rexprs = null;
    initMatch = null;
    canLoop = null;
    canReachOnMore = null;
    hasNfa = null;
    initialState = null;
  }

  private LexerContext BuildLexStatesTable(final boolean unicodeWarning) {
    final LexerContext lexerContext = new LexerContext(context);
    lexerContext.unicodeWarningGiven = unicodeWarning;
    final Iterator<TokenProduction> it = context.globals().rexprlist.iterator();
    TokenProduction tp;
    int i;

    final String[] tmpLexStateName = new String[context.globals().lexstate_I2S.size()];
    while (it.hasNext()) {
      tp = it.next();
      final List<RegExprSpec> respecs = tp.respecs;
      List<TokenProduction> tps;

      for (i = 0; i < tp.lexStates.length; i++) {
        if ((tps = allTpsForState.get(tp.lexStates[i])) == null) {
          tmpLexStateName[maxLexStates++] = tp.lexStates[i];
          allTpsForState.put(tp.lexStates[i], tps = new ArrayList<>());
        }

        tps.add(tp);
      }

      if ((respecs == null) || (respecs.size() == 0)) {
        continue;
      }

      RegularExpression re;
      for (i = 0; i < respecs.size(); i++) {
        if (maxOrdinal <= (re = respecs.get(i).rexp).ordinal) {
          maxOrdinal = re.ordinal + 1;
        }
      }
    }

    kinds = new int[maxOrdinal];
    toSkip = new long[(maxOrdinal / 64) + 1];
    toSpecial = new long[(maxOrdinal / 64) + 1];
    toMore = new long[(maxOrdinal / 64) + 1];
    toToken = new long[(maxOrdinal / 64) + 1];
    toToken[0] = 1L;
    actions = new Action[maxOrdinal];
    actions[0] = context.globals().actForEof;
    initStates = new Hashtable<>();
    lexerContext.canMatchAnyChar = new int[maxLexStates];
    canLoop = new boolean[maxLexStates];
    singlesToSkip = new NfaState[maxLexStates];

    for (i = 0; i < maxLexStates; i++) {
      lexerContext.canMatchAnyChar[i] = -1;
    }

    hasNfa = new boolean[maxLexStates];
    lexerContext.mixed = new boolean[maxLexStates];
    initMatch = new int[maxLexStates];
    newLexState = new String[maxOrdinal];
    newLexState[0] = context.globals().nextStateForEof;
    lexerContext.lexStates = new int[maxOrdinal];
    lexerContext.ignoreCase = new boolean[maxOrdinal];
    rexprs = new RegularExpression[maxOrdinal];
    lexerContext.allImages = new String[maxOrdinal];
    canReachOnMore = new boolean[maxLexStates];
    return lexerContext;
  }

  private int GetIndex(final String name, final String[] lexStateNames) {
    for (int i = 0; i < lexStateNames.length; i++) {
      if ((lexStateNames[i] != null) && lexStateNames[i].equals(name)) {
        return i;
      }
    }

    throw new Error(); // Should never come here
  }

  public TokenizerData generateTokenizerData(
      final boolean generateDataOnly, final boolean unicodeWarning) {

    if (!Options.getBuildTokenManager()
        || Options.getUserTokenManager()
        || (context.errors().get_error_count() > 0)) {
      return new TokenizerData();
    }

    final CodeGenerator codeGenerator = context.getCodeGenerator();
    final List<RegularExpression> choices = new ArrayList<>();
    final LexerContext lexerContext = BuildLexStatesTable(unicodeWarning);
    boolean ignoring = false;

    final TokenizerData tokenizerData = new TokenizerData();
    final String[] lexStateNames = tokenizerData.lexStateNames = new String[maxLexStates];
    for (final int l : context.globals().lexstate_I2S.keySet()) {
      lexStateNames[l] = context.globals().lexstate_I2S.get(l);
    }

    for (int k = 0; k < lexStateNames.length; k++) {
      int startState = -1;
      lexerContext.clear();
      final String key = lexStateNames[k];
      final int lexStateIndex = GetIndex(key, lexStateNames);
      lexerContext.lexStateIndex = lexStateIndex;
      final List<TokenProduction> allTps = allTpsForState.get(key);
      initStates.put(key, initialState = new NfaState(lexerContext));
      ignoring = false;
      singlesToSkip[lexStateIndex] = new NfaState(lexerContext);
      if (key.equals(LexGen.DEFAULT_STATE)) {
        defaultLexState = lexStateIndex;
      }

      for (int i = 0; i < allTps.size(); i++) {
        final TokenProduction tp = allTps.get(i);
        final int kind = tp.kind;
        final boolean ignore = tp.ignoreCase;
        final List<RegExprSpec> rexps = tp.respecs;

        if (i == 0) {
          ignoring = ignore;
        }

        for (int j = 0; j < rexps.size(); j++) {
          final RegExprSpec respec = rexps.get(j);
          lexerContext.curRE = respec.rexp;
          final int curKind = lexerContext.curKind = lexerContext.curRE.ordinal;
          rexprs[curKind] = lexerContext.curRE;
          lexerContext.lexStates[curKind] = lexerContext.lexStateIndex;
          lexerContext.ignoreCase[curKind] = ignore;

          if (lexerContext.curRE.private_rexp) {
            kinds[curKind] = -1;
            continue;
          }

          if (!Options.getNoDfa()
              && ignoring == ignore
              && (lexerContext.curRE instanceof RStringLiteral)
              && !((RStringLiteral) lexerContext.curRE).image.equals("")) {
            ((RStringLiteral) lexerContext.curRE).GenerateDfa(curKind, lexerContext);
            if ((i != 0)
                && !lexerContext.mixed[lexStateIndex]
                // how can this happen as above ignoring == ignore is true?
                && (ignoring != ignore)) {
              lexerContext.mixed[lexStateIndex] = true;
            }
          } else if (lexerContext.curRE.CanMatchAnyChar()) {
            if ((lexerContext.canMatchAnyChar[lexStateIndex] == -1)
                || (lexerContext.canMatchAnyChar[lexStateIndex] > curKind)) {
              lexerContext.canMatchAnyChar[lexStateIndex] = curKind;
            }
          } else {
            if (lexerContext.curRE instanceof RChoice) {
              choices.add(lexerContext.curRE);
            }
            final Nfa nfa = lexerContext.curRE.GenerateNfa(ignore, lexerContext);
            nfa.end.isFinal = true;
            nfa.end.kind = curKind;
            initialState.AddMove(nfa.start);
          }

          if (kinds.length < curKind) {
            final int[] tmp = new int[curKind + 1];
            System.arraycopy(kinds, 0, tmp, 0, kinds.length);
            kinds = tmp;
          }
          kinds[curKind] = kind;

          if ((respec.nextState != null)
              && !respec.nextState.equals(lexStateNames[lexStateIndex])) {
            newLexState[curKind] = respec.nextState;
          }

          if ((respec.act != null)
              && (respec.act.getActionTokens() != null)
              && (respec.act.getActionTokens().size() > 0)) {
            actions[curKind] = respec.act;
          }

          switch (kind) {
            case TokenProduction.SPECIAL:
              toSpecial[curKind / 64] |= 1L << (curKind % 64);
              toSkip[curKind / 64] |= 1L << (curKind % 64);
              break;
            case TokenProduction.SKIP:
              toSkip[curKind / 64] |= 1L << (curKind % 64);
              break;
            case TokenProduction.MORE:
              toMore[curKind / 64] |= 1L << (curKind % 64);
              if (newLexState[curKind] != null) {
                canReachOnMore[GetIndex(newLexState[curKind], lexStateNames)] = true;
              } else {
                canReachOnMore[lexStateIndex] = true;
              }
              break;
            case TokenProduction.TOKEN:
              toToken[curKind / 64] |= 1L << (curKind % 64);
              break;
          } // end          switch (kind)
          //
        } // end        for (int j = 0; j < rexps.size(); j++)
        //
      } // end      for (int i = 0; i < allTps.size(); i++) {

      // Generate a static block for initializing the nfa transitions
      NfaState.ComputeClosures(lexerContext);

      for (int i = 0; i < initialState.epsilonMoves.size(); i++) {
        initialState.epsilonMoves.elementAt(i).GenerateCode();
      }

      if (hasNfa[lexStateIndex] = (lexerContext.generatedStates != 0)) {
        initialState.GenerateCode();
        startState = initialState.GenerateInitMoves();
      }

      if ((initialState.kind != Integer.MAX_VALUE) && (initialState.kind != 0)) {
        if ((initMatch[lexStateIndex] == 0) || (initMatch[lexStateIndex] > initialState.kind)) {
          initMatch[lexStateIndex] = initialState.kind;
        }
      } else if (initMatch[lexStateIndex] == 0) {
        initMatch[lexStateIndex] = Integer.MAX_VALUE;
      }

      RStringLiteral.FillSubString(lexerContext);

      if (hasNfa[lexStateIndex] && !lexerContext.mixed[lexStateIndex]) {
        RStringLiteral.GenerateNfaStartStates(initialState, lexerContext);
      }

      RStringLiteral.UpdateStringLiteralData(totalNumStates, lexerContext);
      NfaState.UpdateNfaData(
          totalNumStates,
          startState,
          lexStateIndex,
          lexerContext.canMatchAnyChar[lexStateIndex],
          lexerContext);
      assert (lexerContext.generatedStates == lexerContext.statesForLexicalState.get(k).size());
      totalNumStates += lexerContext.generatedStates;
      //
    } // end    for (int k = 0; k < lexStateNames.length; k++)

    for (int i = 0; i < choices.size(); i++) {
      ((RChoice) choices.get(i)).CheckUnmatchability(lexerContext.lexStates, lexerContext.context);
    }

    CheckEmptyStringMatch(lexerContext, tokenizerData);

    tokenizerData.setParserName(context.globals().cu_name);
    NfaState.BuildTokenizerData(tokenizerData, lexerContext);
    RStringLiteral.BuildTokenizerData(tokenizerData, lexerContext);

    final int[] newLexStateIndices = new int[maxOrdinal];
    final List<Token> decls = context.globals().token_mgr_decls;
    final StringBuilder tmDeclsSb = new StringBuilder(decls == null ? 0 : 8 * decls.size());
    if ((decls != null) && (decls.size() > 0)) {
      for (final Token token : decls) {
        if (token.specialToken != null) {
          Token t = token;
          while (t.specialToken != null) {
            t = t.specialToken;
          }
          while (t != null) {
            tmDeclsSb.append(t.image);
            t = t.next;
          }
        }
        tmDeclsSb.append(token.image);
      }
    }
    tokenizerData.setDecls(tmDeclsSb.toString());
    final Map<Integer, String> actionStrings = new HashMap<>();
    for (int i = 0; i < maxOrdinal; i++) {
      if (newLexState[i] == null) {
        newLexStateIndices[i] = -1;
      } else {
        newLexStateIndices[i] = GetIndex(newLexState[i], lexStateNames);
      }
      // For java, we have this but for other languages, eventually we will simply have a string.
      final Action act = actions[i];
      if (act == null) {
        continue;
      }
      final List<Token> actionTokens = act.getActionTokens();
      final StringBuilder actionSb = new StringBuilder(8 * actionTokens.size());
      for (final Token actionTk : actionTokens) {
        if (actionTk.specialToken != null) {
          actionSb.append(actionTk.specialToken.image);
        }
        actionSb.append(actionTk.image);
      }
      actionStrings.put(i, actionSb.toString());
    }
    tokenizerData.setDefaultLexState(defaultLexState);
    tokenizerData.updateMatchInfo(
        actionStrings,
        newLexStateIndices,
        toSkip,
        toSpecial,
        toMore,
        toToken,
        lexerContext.allImages);
    final Map<Integer, String> labels = new HashMap<>();
    final Map<Integer, RegularExpression> rexps_of_tokens = context.globals().rexps_of_tokens;
    final String[] images = new String[rexps_of_tokens.size() + 1];
    for (final Integer o : rexps_of_tokens.keySet()) {
      final RegularExpression re = rexps_of_tokens.get(o);
      final String label = re.label;
      if ((label != null) && (label.length() > 0)) {
        labels.put(o, label);
      }
      if (re instanceof RStringLiteral) {
        images[o] = ((RStringLiteral) re).image;
      } else if (!re.label.equals("")) {
        images[o] = "<" + re.label + ">";
      } else {
        if (re.tpContext.kind == TokenProduction.TOKEN) {
          context
              .errors()
              .warning(
                  re, "Consider giving this non-string token a label for better error reporting.");
        }
        images[o] = "<token of kind " + re.ordinal + ">";
      }
    }
    tokenizerData.setLabelsAndImages(context.globals().names_of_tokens, images);
    tokenizerData.setInitMatch(initMatch);

    if (generateDataOnly) {
      return tokenizerData;
    }

    final TokenManagerCodeGenerator gen = codeGenerator.getTokenManagerCodeGenerator(context);
    final CodeGeneratorSettings settings = CodeGeneratorSettings.of(Options.getOptions());
    gen.generateCode(settings, tokenizerData);
    gen.finish(settings, tokenizerData);
    return tokenizerData;
  }

  private void CheckEmptyStringMatch(
      final LexerContext lexerContext, final TokenizerData tokenizerData) {
    int i, j, k, len;
    final boolean[] seen = new boolean[maxLexStates];
    final boolean[] done = new boolean[maxLexStates];
    String cycle;
    String reList;

    Outer:
    for (i = 0; i < maxLexStates; i++) {
      if (done[i]
          || (initMatch[i] == 0)
          || (initMatch[i] == Integer.MAX_VALUE)
          || (lexerContext.canMatchAnyChar[i] != -1)) {
        continue;
      }

      done[i] = true;
      len = 0;
      cycle = "";
      reList = "";

      for (k = 0; k < maxLexStates; k++) {
        seen[k] = false;
      }

      j = i;
      seen[i] = true;
      cycle += tokenizerData.lexStateNames[j] + "-->";
      while (newLexState[initMatch[j]] != null) {
        cycle += newLexState[initMatch[j]];
        if (seen[j = GetIndex(newLexState[initMatch[j]], tokenizerData.lexStateNames)]) {
          break;
        }

        cycle += "-->";
        done[j] = true;
        seen[j] = true;
        if ((initMatch[j] == 0)
            || (initMatch[j] == Integer.MAX_VALUE)
            || (lexerContext.canMatchAnyChar[j] != -1)) {
          continue Outer;
        }
        if (len != 0) {
          reList += "; ";
        }
        reList +=
            "line "
                + rexprs[initMatch[j]].getLine()
                + ", column "
                + rexprs[initMatch[j]].getColumn();
        len++;
      }

      if (newLexState[initMatch[j]] == null) {
        cycle += tokenizerData.lexStateNames[lexerContext.lexStates[initMatch[j]]];
      }

      for (k = 0; k < maxLexStates; k++) {
        canLoop[k] |= seen[k];
      }

      if (len == 0) {
        context
            .errors()
            .warning(
                rexprs[initMatch[i]],
                "Regular expression"
                    + ((rexprs[initMatch[i]].label.equals(""))
                        ? ""
                        : (" for " + rexprs[initMatch[i]].label))
                    + " can be matched by the empty string (\"\") in lexical state "
                    + tokenizerData.lexStateNames[i]
                    + ". This can result in an endless loop of "
                    + "empty string matches.");
      } else {
        context
            .errors()
            .warning(
                rexprs[initMatch[i]],
                "Regular expression"
                    + ((rexprs[initMatch[i]].label.equals(""))
                        ? ""
                        : (" for " + rexprs[initMatch[i]].label))
                    + " can be matched by the empty string (\"\") in lexical state "
                    + tokenizerData.lexStateNames[i]
                    + ". This regular expression along with the "
                    + "regular expressions at "
                    + reList
                    + " forms the cycle \n   "
                    + cycle
                    + "\ncontaining regular expressions with empty matches."
                    + " This can result in an endless loop of empty string matches.");
      }
    }
  }
}
