package test3;

import java.util.ArrayList;
import java.util.Iterator;

import core.Token;

//Ҷ��
public class ASTLeaf extends ASTree {
	
    public ASTLeaf(Token t) { token = t; }
	
    private static ArrayList<ASTree> empty = new ArrayList<ASTree>();
    
    //����
    protected Token token;
    
    //����
    public ASTree child(int i) { throw new IndexOutOfBoundsException(); }
    //����������Ĭ��Ϊ0.���Ϊ1����������д�ú���
    public int numChildren() { return 0; }
    
    //������
    public Iterator<ASTree> children() { return empty.iterator(); }
    
    //ֵ
    public String toString() { return token.getText(); }
    
    //���ڵĴ�������
    public String location() { return "at line " + token.getLineNumber(); }
    
    //��Ҷ�Ӱ����ĵ���
    public Token token() { return token; }
}
