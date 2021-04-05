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

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Describes expansions where one of many choices is taken (c1|c2|...).
 */

public class Choice extends Expansion {

  /**
   * The list of choices of this expansion unit. Each List component will narrow
   * to ExpansionUnit.
   */
  private final List<Expansion> choices = new ArrayList<>();

  public Choice() {}

  public Choice(Token token) {
    setLine(token.beginLine);
    setColumn(token.beginColumn);
  }

  public Choice(Expansion expansion) {
    setLine(expansion.getLine());
    setColumn(expansion.getColumn());
    getChoices().add(expansion);
  }

  /**
   * @return the choices
   */
  public final List<Expansion> getChoices() {
    return choices;
  }

  @Override
  public StringBuffer dump(int indent, Set<Expansion> alreadyDumped) {
    StringBuffer buffer = super.dump(indent, alreadyDumped);
    if (alreadyDumped.contains(this)) {
      return buffer;
    }

    alreadyDumped.add(this);
    getChoices().forEach(e -> buffer.append(Expansion.eol).append(e.dump(indent + 1, alreadyDumped)));
    return buffer;
  }
}
