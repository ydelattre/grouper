/* Generated By:JJTree: Do not edit this line. ASTLENode.java Version 4.3 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=false,VISITOR=true,TRACK_TOKENS=false,NODE_PREFIX=AST,NODE_EXTENDS=,NODE_FACTORY=,SUPPORT_CLASS_VISIBILITY_PUBLIC=true */
package edu.internet2.middleware.grouperClientExt.org.apache.commons.jexl2.parser;

public
class ASTLENode extends JexlNode {
  public ASTLENode(int id) {
    super(id);
  }

  public ASTLENode(Parser p, int id) {
    super(p, id);
  }


  /** Accept the visitor. **/
  public Object jjtAccept(ParserVisitor visitor, Object data) {
    return visitor.visit(this, data);
  }
}
/* JavaCC - OriginalChecksum=8544c3346cafe42bf5dd4f037c2f2141 (do not edit this line) */