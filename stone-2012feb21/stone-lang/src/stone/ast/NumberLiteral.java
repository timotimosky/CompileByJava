package stone.ast;
import stone.Token;

//���εĽڵ�
public class NumberLiteral extends ASTLeaf {
	
    public NumberLiteral(Token t) { super(t); }
    public int value() { return token().getNumber(); }
}
