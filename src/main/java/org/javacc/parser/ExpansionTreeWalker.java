/* Copyright (c) 2006, Sun Microsystems, Inc.
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
 *     * Neither the name of the Sun Microsystems, Inc. nor the names of its
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
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */

package org.javacc.parser;

import java.util.Iterator;

/**
 * A set of routines that walk down the Expansion tree in various ways.
 */
final class ExpansionTreeWalker {

  private ExpansionTreeWalker() {}

  /**
   * Visits the nodes of the tree rooted at "node" in pre-order. i.e., it
   * executes opObj.action first and then visits the children.
   */
  static void preOrderWalk(Expansion node, TreeWalkerOp opObj) {
    opObj.action(node);
    if (opObj.goDeeper(node)) {
      if (node instanceof Choice) {
        for (Iterator<Expansion> it = ((Choice) node).getChoices().iterator(); it.hasNext();) {
          ExpansionTreeWalker.preOrderWalk(it.next(), opObj);
        }
      } else if (node instanceof Sequence) {
        for (Iterator<Expansion> it = ((Sequence) node).units.iterator(); it.hasNext();) {
          ExpansionTreeWalker.preOrderWalk(it.next(), opObj);
        }
      } else if (node instanceof OneOrMore) {
        ExpansionTreeWalker.preOrderWalk(((OneOrMore) node).getExpansion(), opObj);
      } else if (node instanceof ZeroOrMore) {
        ExpansionTreeWalker.preOrderWalk(((ZeroOrMore) node).getExpansion(), opObj);
      } else if (node instanceof ZeroOrOne) {
        ExpansionTreeWalker.preOrderWalk(((ZeroOrOne) node).getExpansion(), opObj);
      } else if (node instanceof Lookahead) {
        Expansion nested_e = ((Lookahead) node).getLaExpansion();
        if (!((nested_e instanceof Sequence) && (((Sequence) nested_e).units.get(0) == node))) {
          ExpansionTreeWalker.preOrderWalk(nested_e, opObj);
        }
      } else if (node instanceof TryBlock) {
        ExpansionTreeWalker.preOrderWalk(((TryBlock) node).exp, opObj);
      } else if (node instanceof RChoice) {
        for (Iterator<RegularExpression> it = ((RChoice) node).getChoices().iterator(); it.hasNext();) {
          ExpansionTreeWalker.preOrderWalk(it.next(), opObj);
        }
      } else if (node instanceof RSequence) {
        for (Iterator<RegularExpression> it = ((RSequence) node).units.iterator(); it.hasNext();) {
          ExpansionTreeWalker.preOrderWalk(it.next(), opObj);
        }
      } else if (node instanceof ROneOrMore) {
        ExpansionTreeWalker.preOrderWalk(((ROneOrMore) node).regexpr, opObj);
      } else if (node instanceof RZeroOrMore) {
        ExpansionTreeWalker.preOrderWalk(((RZeroOrMore) node).regexpr, opObj);
      } else if (node instanceof RZeroOrOne) {
        ExpansionTreeWalker.preOrderWalk(((RZeroOrOne) node).regexpr, opObj);
      } else if (node instanceof RRepetitionRange) {
        ExpansionTreeWalker.preOrderWalk(((RRepetitionRange) node).regexpr, opObj);
      }
    }
  }

  /**
   * Visits the nodes of the tree rooted at "node" in post-order. i.e., it
   * visits the children first and then executes opObj.action.
   */
  static void postOrderWalk(Expansion node, TreeWalkerOp opObj) {
    if (opObj.goDeeper(node)) {
      if (node instanceof Choice) {
        for (Iterator<Expansion> it = ((Choice) node).getChoices().iterator(); it.hasNext();) {
          ExpansionTreeWalker.postOrderWalk(it.next(), opObj);
        }
      } else if (node instanceof Sequence) {
        for (Iterator<Expansion> it = ((Sequence) node).units.iterator(); it.hasNext();) {
          ExpansionTreeWalker.postOrderWalk(it.next(), opObj);
        }
      } else if (node instanceof OneOrMore) {
        ExpansionTreeWalker.postOrderWalk(((OneOrMore) node).getExpansion(), opObj);
      } else if (node instanceof ZeroOrMore) {
        ExpansionTreeWalker.postOrderWalk(((ZeroOrMore) node).getExpansion(), opObj);
      } else if (node instanceof ZeroOrOne) {
        ExpansionTreeWalker.postOrderWalk(((ZeroOrOne) node).getExpansion(), opObj);
      } else if (node instanceof Lookahead) {
        Expansion nested_e = ((Lookahead) node).getLaExpansion();
        if (!((nested_e instanceof Sequence) && (((Sequence) nested_e).units.get(0) == node))) {
          ExpansionTreeWalker.postOrderWalk(nested_e, opObj);
        }
      } else if (node instanceof TryBlock) {
        ExpansionTreeWalker.postOrderWalk(((TryBlock) node).exp, opObj);
      } else if (node instanceof RChoice) {
        for (Iterator<RegularExpression> it = ((RChoice) node).getChoices().iterator(); it.hasNext();) {
          ExpansionTreeWalker.postOrderWalk(it.next(), opObj);
        }
      } else if (node instanceof RSequence) {
        for (Iterator<RegularExpression> it = ((RSequence) node).units.iterator(); it.hasNext();) {
          ExpansionTreeWalker.postOrderWalk(it.next(), opObj);
        }
      } else if (node instanceof ROneOrMore) {
        ExpansionTreeWalker.postOrderWalk(((ROneOrMore) node).regexpr, opObj);
      } else if (node instanceof RZeroOrMore) {
        ExpansionTreeWalker.postOrderWalk(((RZeroOrMore) node).regexpr, opObj);
      } else if (node instanceof RZeroOrOne) {
        ExpansionTreeWalker.postOrderWalk(((RZeroOrOne) node).regexpr, opObj);
      } else if (node instanceof RRepetitionRange) {
        ExpansionTreeWalker.postOrderWalk(((RRepetitionRange) node).regexpr, opObj);
      }
    }
    opObj.action(node);
  }

}
