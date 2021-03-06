package stone.ast;
import java.util.List;



//表达式
public class BinaryExpr extends ASTList {
	
	//构造函数
    public BinaryExpr(List<ASTree> c) { super(c); }
    
    //左节点
    public ASTree left() { return child(0); }
    
    //操作符
    public String operator() {
        return ((ASTLeaf)child(1)).token().getText();
    }
    
    //右节点
    public ASTree right() { return child(2); }
}
