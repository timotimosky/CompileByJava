package statement;

import java.util.List;

import test3.ASTList;
import test3.ASTree;


//IF statement, if”Ôæ‰
public class IfStmnt extends ASTList
{
    public IfStmnt(List<ASTree> c) { super(c); }
    public ASTree condition() { return child(0); }
    public ASTree thenBlock() { return child(1); }
    public ASTree elseBlock() {
        return numChildren() > 2 ? child(2) : null;
    }
    public String toString() {
        return "(if " + condition() + " " + thenBlock()
                 + " else " + elseBlock() + ")";
    }
}
