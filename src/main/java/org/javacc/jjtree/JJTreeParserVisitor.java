/*
 * Generated By:JavaCC: Do not edit this line. JJTreeParserVisitor.java Version
 * 4.1d1
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
/*
 * JavaCC - OriginalChecksum=236a0da55ea23f741ece2c8012f6d143 (do not edit this
 * line)
 */
