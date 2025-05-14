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

public class Pair<A, B> {
  private A first;
  private B second;

  public Pair(final A first, final B second) {
    super();
    this.first = first;
    this.second = second;
  }

  @Override
  public int hashCode() {
    final int hashFirst = first != null ? first.hashCode() : 0;
    final int hashSecond = second != null ? second.hashCode() : 0;

    return (hashFirst + hashSecond) * hashSecond + hashFirst;
  }

  @Override
  public boolean equals(final Object other) {
    if (other instanceof Pair) {
      final Pair<?, ?> otherPair = (Pair<?, ?>) other;
      return ((this.first == otherPair.first
              || (this.first != null
                  && otherPair.first != null
                  && this.first.equals(otherPair.first)))
          && (this.second == otherPair.second
              || (this.second != null
                  && otherPair.second != null
                  && this.second.equals(otherPair.second))));
    }

    return false;
  }

  @Override
  public String toString() {
    return "(" + first + ", " + second + ")";
  }

  public A getFirst() {
    return first;
  }

  public void setFirst(final A first) {
    this.first = first;
  }

  public B getSecond() {
    return second;
  }

  public void setSecond(final B second) {
    this.second = second;
  }
}
