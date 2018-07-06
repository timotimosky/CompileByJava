package stone.ast;
import java.util.List;



//����ʽ
public class BinaryExpr extends ASTList {
	
	//���캯��
    public BinaryExpr(List<ASTree> c) { super(c); }
    
    //��ڵ�
    public ASTree left() { return child(0); }
    
    //������
    public String operator() {
        return ((ASTLeaf)child(1)).token().getText();
    }
    
    //�ҽڵ�
    public ASTree right() { return child(2); }
}