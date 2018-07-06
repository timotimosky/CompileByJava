package expr;

import java.util.List;

import test3.ASTList;
import test3.ASTree;

//���ս��������Ԫ��
public class PrimaryExpr extends ASTList {
	
    public PrimaryExpr(List<ASTree> c) { super(c); }
    
    public static ASTree create(List<ASTree> c) 
    {
        return c.size() == 1 ? c.get(0) : new PrimaryExpr(c);
    }
}
