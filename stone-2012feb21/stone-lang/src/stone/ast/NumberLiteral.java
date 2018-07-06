package stone.ast;
import stone.Token;

//整形的节点
public class NumberLiteral extends ASTLeaf {
	
    public NumberLiteral(Token t) { super(t); }
    public int value() { return token().getNumber(); }
}
