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
package org.javacc.jjtree;

public interface JJTreeParserVisitor {

  public Object visit(SimpleNode node, Object data);

  public Object visit(ASTGrammar node, Object data);

  public Object visit(ASTCompilationUnit node, Object data);

  public Object visit(ASTProductions node, Object data);

  public Object visit(ASTOptions node, Object data);

  public Object visit(ASTOptionBinding node, Object data);

  public Object visit(ASTJavacode node, Object data);

  public Object visit(ASTJavacodeBody node, Object data);

  public Object visit(ASTBNF node, Object data);

  public Object visit(ASTBNFDeclaration node, Object data);

  public Object visit(ASTBNFNodeScope node, Object data);

  public Object visit(ASTRE node, Object data);

  public Object visit(ASTTokenDecls node, Object data);

  public Object visit(ASTRESpec node, Object data);

  public Object visit(ASTBNFChoice node, Object data);

  public Object visit(ASTBNFSequence node, Object data);

  public Object visit(ASTBNFLookahead node, Object data);

  public Object visit(ASTExpansionNodeScope node, Object data);

  public Object visit(ASTBNFAction node, Object data);

  public Object visit(ASTBNFZeroOrOne node, Object data);

  public Object visit(ASTBNFTryBlock node, Object data);

  public Object visit(ASTBNFNonTerminal node, Object data);

  public Object visit(ASTBNFAssignment node, Object data);

  public Object visit(ASTBNFOneOrMore node, Object data);

  public Object visit(ASTBNFZeroOrMore node, Object data);

  public Object visit(ASTBNFParenthesized node, Object data);

  public Object visit(ASTREStringLiteral node, Object data);

  public Object visit(ASTRENamed node, Object data);

  public Object visit(ASTREReference node, Object data);

  public Object visit(ASTREEOF node, Object data);

  public Object visit(ASTREChoice node, Object data);

  public Object visit(ASTRESequence node, Object data);

  public Object visit(ASTREOneOrMore node, Object data);

  public Object visit(ASTREZeroOrMore node, Object data);

  public Object visit(ASTREZeroOrOne node, Object data);

  public Object visit(ASTRRepetitionRange node, Object data);

  public Object visit(ASTREParenthesized node, Object data);

  public Object visit(ASTRECharList node, Object data);

  public Object visit(ASTCharDescriptor node, Object data);

  public Object visit(ASTNodeDescriptor node, Object data);

  public Object visit(ASTNodeDescriptorExpression node, Object data);

  public Object visit(ASTPrimaryExpression node, Object data);
}
