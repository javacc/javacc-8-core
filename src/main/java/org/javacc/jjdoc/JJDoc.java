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

import java.util.Iterator;
import java.util.List;
import org.javacc.parser.Action;
import org.javacc.parser.BNFProduction;
import org.javacc.parser.CharacterRange;
import org.javacc.parser.Choice;
import org.javacc.parser.CppCodeProduction;
import org.javacc.parser.Expansion;
import org.javacc.parser.JavaCCGlobals;
import org.javacc.parser.JavaCodeProduction;
import org.javacc.parser.Lookahead;
import org.javacc.parser.NonTerminal;
import org.javacc.parser.NormalProduction;
import org.javacc.parser.OneOrMore;
import org.javacc.parser.RCharacterList;
import org.javacc.parser.RChoice;
import org.javacc.parser.REndOfFile;
import org.javacc.parser.RJustName;
import org.javacc.parser.ROneOrMore;
import org.javacc.parser.RRepetitionRange;
import org.javacc.parser.RSequence;
import org.javacc.parser.RStringLiteral;
import org.javacc.parser.RZeroOrMore;
import org.javacc.parser.RZeroOrOne;
import org.javacc.parser.RegExprSpec;
import org.javacc.parser.RegularExpression;
import org.javacc.parser.Sequence;
import org.javacc.parser.SingleCharacter;
import org.javacc.parser.Token;
import org.javacc.parser.TokenProduction;
import org.javacc.parser.TryBlock;
import org.javacc.parser.ZeroOrMore;
import org.javacc.parser.ZeroOrOne;

/**
 * The JJDoc tool, which generates the document.
 */
public class JJDoc {
  
  /** The name of the input file. */
  String input_file;
  
  /** The name of the input file directory. */
  String input_directory;
  
  /** The full path name of the output file. */
  String output_file_path;
  
  /** The Generator to create output with. */
  Generator generator;
  
  /**
   * @param context - the context
   * @return the generator
   */
  Generator getGenerator(final JJDocContext context) {
    if (generator == null) {
      // not set by setter or previous call
      if (context.getText()) {
        generator = new TextGenerator(context, this);
      } else if (context.getBNF()) {
        generator = new BNFGenerator(context, this);
      } else if (context.getXText()) {
        generator = new XTextGenerator(context, this);
      } else if (context.getJCC()) {
        generator = new JCCGenerator(context, this);
      } else {
        generator = new HTMLGenerator(context, this);
      }
    } else {
      // set by setter or previous call: set it to a new instance 
      //  accordingly to the option setting
      // TODO check the reason of these oddities (type change and new instanciation)
      if (context.getText()) {
        if (generator instanceof HTMLGenerator) {
          generator = new TextGenerator(context, this);
        }
      } else if (context.getBNF()) {
        generator = new BNFGenerator(context, this);
      } else if (context.getXText()) {
        generator = new XTextGenerator(context, this);
      } else if (context.getJCC()) {
        generator = new JCCGenerator(context, this);
      } else {
        if (generator instanceof TextGenerator) {
          generator = new HTMLGenerator(context, this);
        }
      }
    }
    return generator;
  }
  
  /**
   * Performs the document generation.
   * 
   * @param context - The JJDoc context
   */
  void generate(final JJDocContext context) {
    generator = getGenerator(context);
    generator.documentStart();
    emitTokenProductions(context.globals().rexprlist, context);
    emitNormalProductions(context.globals().bnfproductions, context);
    generator.documentEnd();
  }
  
  @SuppressWarnings("javadoc")
  private void emitTokenProductions(final List<TokenProduction> prods, final JJDocContext context) {
    generator.tokensStart();
    for (final Iterator<TokenProduction> it = prods.iterator(); it.hasNext();) {
      final TokenProduction tp = it.next();
      // FIXME there are many empty productions here
      if (tp.firstToken != null) {
        emitTopLevelSpecialTokens(tp.firstToken, generator, context);
      }
      generator.handleTokenProduction(getStandardTokenProductionText(tp, context), tp);
    }
    generator.tokensEnd();
  }
  
  @SuppressWarnings("javadoc")
  private void emitTopLevelSpecialTokens(Token tok, final Generator gen,
      final JJDocContext context) {
    if (tok == null) {
      // Strange ...
      return;
    }
    tok = getPrecedingSpecialToken(tok);
    String s = "";
    if (tok != null) {
      context.globals().cline = tok.beginLine;
      context.globals().ccol = tok.beginColumn;
      while (tok != null) {
        s += tok.printTokenOnly(context.globals(), true);
        tok = tok.next;
      }
    }
    if (!s.equals("")) {
      gen.specialTokens(s);
    }
  }
  
  @SuppressWarnings("javadoc")
  private Token getPrecedingSpecialToken(final Token tok) {
    Token t = tok;
    while (t.specialToken != null) {
      t = t.specialToken;
    }
    return (t != tok) ? t : null;
  }
  
  @SuppressWarnings("javadoc")
  public String getStandardTokenProductionText(final TokenProduction tp,
      final JJDocContext context) {
    String token = "";
    if (tp.isExplicit) {
      if (tp.lexStates == null) {
        token += "<*> ";
      } else {
        token += "<";
        for (int i = 0; i < tp.lexStates.length; ++i) {
          token += tp.lexStates[i];
          if (i < (tp.lexStates.length - 1)) {
            token += ",";
          }
        }
        token += "> ";
      }
      token += TokenProduction.kindImage[tp.kind];
      if (tp.ignoreCase) {
        token += " [IGNORE_CASE]";
      }
      token += " : {\n";
      for (final Iterator<RegExprSpec> it2 = tp.respecs.iterator(); it2.hasNext();) {
        final RegExprSpec res = it2.next();
        
        token += emitRE(res.rexp, context);
        
        if (res.nsTok != null) {
          token += " : " + res.nsTok.image;
        }
        
        token += "\n";
        if (it2.hasNext()) {
          token += "| ";
        }
      }
      token += "}\n\n";
    }
    return token;
  }
  
  @SuppressWarnings("javadoc")
  private void emitNormalProductions(final List<NormalProduction> prods,
      final JJDocContext context) {
    generator.nonterminalsStart();
    for (final Iterator<NormalProduction> it = prods.iterator(); it.hasNext();) {
      final NormalProduction np = it.next();
      emitTopLevelSpecialTokens(np.getFirstToken(), generator, context);
      if (np instanceof BNFProduction) {
        generator.productionStart(np);
        if (np.getExpansion() instanceof Choice) {
          boolean first = true;
          final Choice c = (Choice) np.getExpansion();
          for (final Iterator<Expansion> expansionsIterator = c.getChoices()
              .iterator(); expansionsIterator.hasNext();) {
            final Expansion e = expansionsIterator.next();
            generator.expansionStart(e, first);
            emitExpansionTree(e, generator, context);
            generator.expansionEnd(e, first);
            first = false;
          }
        } else {
          generator.expansionStart(np.getExpansion(), true);
          emitExpansionTree(np.getExpansion(), generator, context);
          generator.expansionEnd(np.getExpansion(), true);
        }
        generator.productionEnd(np);
      } else if (np instanceof CppCodeProduction) {
        generator.cppcode((CppCodeProduction) np);
      } else if (np instanceof JavaCodeProduction) {
        generator.javacode((JavaCodeProduction) np);
      }
    }
    generator.nonterminalsEnd();
  }
  
  @SuppressWarnings("javadoc")
  private void emitExpansionTree(final Expansion exp, final Generator gen,
      final JJDocContext context) {
    // gen.text("[->" + exp.getClass().getName() + "]");
    if (exp instanceof Action) {
      emitExpansionAction((Action) exp, gen, context);
    } else if (exp instanceof Choice) {
      emitExpansionChoice((Choice) exp, gen, context);
    } else if (exp instanceof Lookahead) {
      emitExpansionLookahead((Lookahead) exp, gen, context);
    } else if (exp instanceof NonTerminal) {
      emitExpansionNonTerminal((NonTerminal) exp, gen, context);
    } else if (exp instanceof OneOrMore) {
      emitExpansionOneOrMore((OneOrMore) exp, gen, context);
    } else if (exp instanceof RegularExpression) {
      emitExpansionRegularExpression((RegularExpression) exp, gen, context);
    } else if (exp instanceof Sequence) {
      emitExpansionSequence((Sequence) exp, gen, context);
    } else if (exp instanceof TryBlock) {
      emitExpansionTryBlock((TryBlock) exp, gen, context);
    } else if (exp instanceof ZeroOrMore) {
      emitExpansionZeroOrMore((ZeroOrMore) exp, gen, context);
    } else if (exp instanceof ZeroOrOne) {
      emitExpansionZeroOrOne((ZeroOrOne) exp, gen, context);
    } else {
      error(context, "Oops: Unknown expansion type.");
    }
  }
  
  @SuppressWarnings("javadoc")
  private void emitExpansionAction(final Action a, final Generator gen,
      final JJDocContext context) {}
  
  @SuppressWarnings("javadoc")
  private void emitExpansionChoice(final Choice c, final Generator gen,
      final JJDocContext context) {
    for (final Iterator<Expansion> it = c.getChoices().iterator(); it.hasNext();) {
      final Expansion e = it.next();
      emitExpansionTree(e, gen, context);
      if (it.hasNext()) {
        gen.text(" | ");
      }
    }
  }
  
  @SuppressWarnings("javadoc")
  private void emitExpansionLookahead(final Lookahead l, final Generator gen,
      final JJDocContext context) {
    gen.lookAheadStart(l);
    gen.lookAheadEnd(l);
  }
  
  @SuppressWarnings("javadoc")
  private void emitExpansionNonTerminal(final NonTerminal nt, final Generator gen,
      final JJDocContext context) {
    gen.nonTerminalStart(nt);
    gen.text(nt.getName());
    gen.nonTerminalEnd(nt);
  }
  
  @SuppressWarnings("javadoc")
  private void emitExpansionOneOrMore(final OneOrMore o, final Generator gen,
      final JJDocContext context) {
    gen.text("( ");
    emitExpansionTree(o.getExpansion(), gen, context);
    gen.text(" )+");
  }
  
  @SuppressWarnings("javadoc")
  private void emitExpansionRegularExpression(final RegularExpression r, final Generator gen,
      final JJDocContext context) {
    final String reRendered = emitRE(r, context);
    if (!reRendered.equals("")) {
      gen.reStart(r);
      gen.text(reRendered);
      gen.reEnd(r);
    }
  }
  
  @SuppressWarnings("javadoc")
  private void emitExpansionSequence(final Sequence s, final Generator gen,
      final JJDocContext context) {
    boolean firstUnit = true;
    for (final Iterator<Expansion> it = s.units.iterator(); it.hasNext();) {
      final Expansion e = it.next();
      if (e instanceof Action) {
        continue;
      }
      if (!firstUnit) {
        gen.text(" ");
      }
      final boolean needParens = (e instanceof Choice) || (e instanceof Sequence);
      if (needParens) {
        gen.text("( ");
      }
      emitExpansionTree(e, gen, context);
      if (needParens) {
        gen.text(" )");
      }
      firstUnit = false;
    }
  }
  
  @SuppressWarnings("javadoc")
  private void emitExpansionTryBlock(final TryBlock t, final Generator gen,
      final JJDocContext context) {
    final boolean needParens = t.exp instanceof Choice;
    if (needParens) {
      gen.text("( ");
    }
    emitExpansionTree(t.exp, gen, context);
    if (needParens) {
      gen.text(" )");
    }
  }
  
  @SuppressWarnings("javadoc")
  private void emitExpansionZeroOrMore(final ZeroOrMore z, final Generator gen,
      final JJDocContext context) {
    gen.text("( ");
    emitExpansionTree(z.getExpansion(), gen, context);
    gen.text(" )*");
  }
  
  @SuppressWarnings("javadoc")
  private void emitExpansionZeroOrOne(final ZeroOrOne z, final Generator gen,
      final JJDocContext context) {
    gen.text("( ");
    emitExpansionTree(z.getExpansion(), gen, context);
    gen.text(" )?");
  }
  
  @SuppressWarnings("javadoc")
  public String emitRE(final RegularExpression re, final JJDocContext context) {
    String returnString = "";
    final boolean hasLabel = !re.label.equals("");
    final boolean justName = re instanceof RJustName;
    final boolean eof = re instanceof REndOfFile;
    final boolean isString = re instanceof RStringLiteral;
    final boolean toplevelRE = (re.tpContext != null);
    final boolean needBrackets = justName || eof || hasLabel || (!isString && toplevelRE);
    if (needBrackets) {
      returnString += "<";
      if (!justName) {
        if (re.private_rexp) {
          returnString += "#";
        }
        if (hasLabel) {
          returnString += re.label;
          returnString += ": ";
        }
      }
    }
    if (re instanceof RCharacterList) {
      final RCharacterList cl = (RCharacterList) re;
      if (cl.negated_list) {
        returnString += "~";
      }
      returnString += "[";
      for (final Iterator<Expansion> it = cl.descriptors.iterator(); it.hasNext();) {
        final Object o = it.next();
        if (o instanceof SingleCharacter) {
          returnString += "\"";
          final char s[] = {
              ((SingleCharacter) o).ch
          };
          returnString += JavaCCGlobals.add_escapes(new String(s));
          returnString += "\"";
        } else if (o instanceof CharacterRange) {
          returnString += "\"";
          final char s[] = {
              ((CharacterRange) o).getLeft()
          };
          returnString += JavaCCGlobals.add_escapes(new String(s));
          returnString += "\"-\"";
          s[0] = ((CharacterRange) o).getRight();
          returnString += JavaCCGlobals.add_escapes(new String(s));
          returnString += "\"";
        } else {
          error(context, "Oops: unknown character list element type.");
        }
        if (it.hasNext()) {
          returnString += ",";
        }
      }
      returnString += "]";
    } else if (re instanceof RChoice) {
      final RChoice c = (RChoice) re;
      for (final Iterator<RegularExpression> it = c.getChoices().iterator(); it.hasNext();) {
        final RegularExpression sub = it.next();
        returnString += emitRE(sub, context);
        if (it.hasNext()) {
          returnString += " | ";
        }
      }
    } else if (re instanceof REndOfFile) {
      returnString += "EOF";
    } else if (re instanceof RJustName) {
      final RJustName jn = (RJustName) re;
      returnString += jn.label;
    } else if (re instanceof ROneOrMore) {
      final ROneOrMore om = (ROneOrMore) re;
      returnString += "(";
      returnString += emitRE(om.regexpr, context);
      returnString += ")+";
    } else if (re instanceof RSequence) {
      final RSequence s = (RSequence) re;
      for (final Iterator<RegularExpression> it = s.units.iterator(); it.hasNext();) {
        final RegularExpression sub = it.next();
        boolean needParens = false;
        if (sub instanceof RChoice) {
          needParens = true;
        }
        if (needParens) {
          returnString += "(";
        }
        returnString += emitRE(sub, context);
        if (needParens) {
          returnString += ")";
        }
        if (it.hasNext()) {
          returnString += " ";
        }
      }
    } else if (re instanceof RStringLiteral) {
      final RStringLiteral sl = (RStringLiteral) re;
      returnString += ("\"" + JavaCCGlobals.add_escapes(sl.image) + "\"");
    } else if (re instanceof RZeroOrMore) {
      final RZeroOrMore zm = (RZeroOrMore) re;
      returnString += "(";
      returnString += emitRE(zm.regexpr, context);
      returnString += ")*";
    } else if (re instanceof RZeroOrOne) {
      final RZeroOrOne zo = (RZeroOrOne) re;
      returnString += "(";
      returnString += emitRE(zo.regexpr, context);
      returnString += ")?";
    } else if (re instanceof RRepetitionRange) {
      final RRepetitionRange zo = (RRepetitionRange) re;
      returnString += "(";
      returnString += emitRE(zo.regexpr, context);
      returnString += ")";
      returnString += "{";
      if (zo.hasMax) {
        returnString += zo.min;
        returnString += ",";
        returnString += zo.max;
      } else {
        returnString += zo.min;
      }
      returnString += "}";
    } else {
      error(context, "Oops: Unknown regular expression type.");
    }
    if (needBrackets) {
      returnString += ">";
    }
    return returnString;
  }
  
  /**
   * Logs informational messages.
   * 
   * @param context - the context
   * @param message - the message to log
   */
  public void info(final JJDocContext context, final String message) {
    getGenerator(context).info(message);
  }
  
  /**
   * Logs warning messages.
   *
   * @param context - the context
   * @param message - the message to log
   */
  public void warn(final JJDocContext context, final String message) {
    getGenerator(context).warn(message);
  }
  
  /**
   * Logs error messages.
   *
   * @param context - the context
   * @param message - the message to log
   */
  public void error(final JJDocContext context, final String message) {
    getGenerator(context).error(message);
  }
  
}
