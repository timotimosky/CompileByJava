package statement;

import java.util.List;

import test3.ASTList;
import test3.ASTree;


//While statement, while”Ôæ‰
public class WhileStmnt extends ASTList {
    public WhileStmnt(List<ASTree> c) { super(c); }
    public ASTree condition() { return child(0); }
    public ASTree body() { return child(1); }
    public String toString() {
        return "(while " + condition() + " " + body() + ")";
    }
}