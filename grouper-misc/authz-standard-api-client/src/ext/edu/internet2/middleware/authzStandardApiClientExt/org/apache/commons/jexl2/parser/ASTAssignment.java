/* Generated By:JJTree: Do not edit this line. ASTAssignment.java Version 4.3 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=false,VISITOR=true,TRACK_TOKENS=false,NODE_PREFIX=AST,NODE_EXTENDS=,NODE_FACTORY=,SUPPORT_CLASS_VISIBILITY_PUBLIC=true */
package edu.internet2.middleware.authzStandardApiClientExt.org.apache.commons.jexl2.parser;

public
class ASTAssignment extends JexlNode {
  public ASTAssignment(int id) {
    super(id);
  }

  public ASTAssignment(Parser p, int id) {
    super(p, id);
  }


  /** Accept the visitor. **/
  public Object jjtAccept(ParserVisitor visitor, Object data) {
    return visitor.visit(this, data);
  }
}
/* JavaCC - OriginalChecksum=91b33c934f786119942ed55f5ddd5473 (do not edit this line) */