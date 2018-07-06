package expr;

import java.util.List;

import test3.ASTList;
import test3.ASTree;

//非终结符：基本元素
public class PrimaryExpr extends ASTList {
	
    public PrimaryExpr(List<ASTree> c) { super(c); }
    
    public static ASTree create(List<ASTree> c) 
    {
        return c.size() == 1 ? c.get(0) : new PrimaryExpr(c);
    }
}
