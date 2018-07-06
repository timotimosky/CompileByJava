package expr;

import java.util.List;

import test3.ASTList;
import test3.ASTree;

//¸ººÅ
public class NegativeExpr extends ASTList {
    public NegativeExpr(List<ASTree> c) { super(c); }
    public ASTree operand() { return child(0); }
    public String toString() {
        return "-" + operand();
    }
}
