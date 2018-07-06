package expr;

import java.util.List;

import test3.ASTLeaf;
import test3.ASTList;
import test3.ASTree;

//非终结符：表达式
public class BinaryExpr extends ASTList {
    public BinaryExpr(List<ASTree> c) { super(c); }
    public ASTree left() { return child(0); }
    public String operator() {
        return ((ASTLeaf)child(1)).token().getText();
    }
    public ASTree right() { return child(2); }
}
