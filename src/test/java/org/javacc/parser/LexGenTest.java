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

import java.io.StringReader;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import junit.framework.TestCase;
import org.javacc.jjtree.DefaultJJTreeVisitor;
import org.javacc.jjtree.JJTreeContext;

/**
 * Tests the tokenizer data generated for a grammar with more than one lexical state, where some
 * tokens are declared in all of them.
 */
public final class LexGenTest extends TestCase {

  private static final String GRAMMAR =
      "options { STATIC = false; }\n"
          + "PARSER_BEGIN(P) public class P {} PARSER_END(P)\n"
          + "<*> SKIP : { \" \"\n"
          + "  | < COMMENT: \"/*\" (~[\"*\"])* \"*\" (\"*\" | ~[\"*\",\"/\"] (~[\"*\"])* \"*\")* \"/\" > }\n"
          + "<*> TOKEN : { < DIV: \"/\" > | < DOT: \".\" > : DOT_ID }\n"
          + "<NEVER> TOKEN : { < X: \"x\" > }\n"
          + "<DOT_ID> TOKEN : { < DOT_IDENTIFIER: ([\"a\"-\"z\",\"0\"-\"9\"])+ > : DEFAULT }\n"
          + "<DEFAULT> TOKEN : { < IDENTIFIER: ([\"a\"-\"z\"])+ > | < FLOAT: \".\" ([\"0\"-\"9\"])+ > }\n"
          + "void S() : {} { (<IDENTIFIER> | <FLOAT> | <DIV> | <DOT> <DOT_IDENTIFIER>)* <EOF> }\n";

  /**
   * The code generator the lexer generator asks for: found through the service declared in the
   * test resources, as the core has none of its own; generating the tokenizer data only needs its
   * name.
   */
  public static final class Generator implements CodeGenerator {
    @Override
    public String getName() {
      return "Java";
    }

    @Override
    public boolean generateHelpers(
        final Context context,
        final CodeGeneratorSettings settings,
        final TokenizerData tokenizerData) {
      return true;
    }

    @Override
    public TokenCodeGenerator getTokenCodeGenerator(final Context context) {
      return null;
    }

    @Override
    public TokenManagerCodeGenerator getTokenManagerCodeGenerator(final Context context) {
      return null;
    }

    @Override
    public ParserCodeGenerator getParserCodeGenerator(final Context context) {
      return null;
    }

    @Override
    public DefaultJJTreeVisitor getJJTreeCodeGenerator(final JJTreeContext context) {
      return null;
    }
  }

  private TokenizerData data;

  @Override
  public void setUp() throws Exception {
    final Context context = Main.reInitAll();
    Options.set(Options.NUO__INTERPRETER_MODE, true);
    new JavaCCParser(new StringReader(GRAMMAR)).javacc_input(context);
    Semanticize.start(context);
    data = new LexGen(context).generateTokenizerData(true, false);
    assertEquals(0, context.errors().get_error_count());
  }

  private int lexState(final String name) {
    for (int i = 0; i < data.lexStateNames.length; i++) {
      if (name.equals(data.lexStateNames[i])) {
        return i;
      }
    }
    throw new IllegalArgumentException(name);
  }

  private int kind(final String label) {
    for (final Map.Entry<Integer, String> entry : data.labels.entrySet()) {
      if (label.equals(entry.getValue())) {
        return entry.getKey();
      }
    }
    throw new IllegalArgumentException(label);
  }

  /**
   * Matches the longest token at the beginning of an input, the way a token manager does: a string
   * literal first, then the NFA from the start state of that literal in the lexical state.
   *
   * @return the label of the token followed by its image
   */
  private String match(final String lexStateName, final String input) {
    final int lexState = lexState(lexStateName);
    int kind = Integer.MAX_VALUE;
    int length = 0;
    int pos = 0;
    int start = data.initialStates.get(lexState);
    final int key = lexState << 16 | input.charAt(0);
    final List<String> literals = data.literalSequence.get(key);
    if (literals != null) {
      for (int i = 0; i < literals.size(); i++) {
        if (input.startsWith(literals.get(i))) {
          kind = data.literalKinds.get(key).get(i);
          length = pos = literals.get(i).length();
          start = data.getNfaStartState(lexState, kind);
          break;
        }
      }
    }
    Set<Integer> states = new HashSet<>();
    if (start != -1) {
      states.add(start);
      if (data.nfa.get(start).compositeStates != null) {
        states.addAll(data.nfa.get(start).compositeStates);
      }
    }
    while (!states.isEmpty() && pos < input.length()) {
      final char c = input.charAt(pos++);
      final Set<Integer> nextStates = new HashSet<>();
      int matched = Integer.MAX_VALUE;
      for (final int state : states) {
        final TokenizerData.NfaState nfaState = data.nfa.get(state);
        assertNotNull("no state " + state, nfaState);
        if (nfaState.characters.contains(c)) {
          matched = Math.min(matched, nfaState.kind);
          nextStates.addAll(nfaState.nextStates);
        }
      }
      if (matched != Integer.MAX_VALUE) {
        kind = matched;
        length = pos;
      }
      states = nextStates;
    }
    return (kind == Integer.MAX_VALUE ? null : data.labels.get(kind))
        + " "
        + input.substring(0, length);
  }

  /** The states of a lexical state must not move to a state that does not exist. */
  public void testNextStatesExist() {
    for (final Map.Entry<Integer, TokenizerData.NfaState> entry : data.nfa.entrySet()) {
      final Set<Integer> nextStates = new HashSet<>(entry.getValue().nextStates);
      if (entry.getValue().compositeStates != null) {
        nextStates.addAll(entry.getValue().compositeStates);
      }
      for (final int next : nextStates) {
        assertTrue(
            "state " + entry.getKey() + " moves to missing state " + next,
            data.nfa.containsKey(next));
      }
    }
  }

  /** A token declared in all lexical states is matched the same way in each of them. */
  public void testTokenOfAllLexicalStates() {
    for (final String lexState : data.lexStateNames) {
      assertEquals("DIV /", match(lexState, "/ a"));
      assertEquals("COMMENT /**/", match(lexState, "/**/ a"));
      assertEquals("COMMENT /* a * b ** c **/", match(lexState, "/* a * b ** c **/ a */"));
    }
  }

  /**
   * A string literal declared in all lexical states starts a longer token in the lexical states
   * that token is declared in, and only in them.
   */
  public void testLiteralStartsTokenOfOneLexicalState() {
    assertEquals("FLOAT .5", match("DEFAULT", ".5"));
    assertEquals("DOT .", match("DEFAULT", ".a"));
    assertEquals("DOT .", match("DOT_ID", ".5"));
    assertEquals("DOT .", match("NEVER", ".5"));
    final int dot = kind("DOT");
    assertTrue(data.getNfaStartState(lexState("DEFAULT"), dot) != -1);
    assertEquals(-1, data.getNfaStartState(lexState("DOT_ID"), dot));
    assertEquals(-1, data.getNfaStartState(lexState("NEVER"), dot));
  }
}
