package test3;

import java.util.ArrayList;
import java.util.Iterator;

import core.Token;

//叶子
public class ASTLeaf extends ASTree {
	
    public ASTLeaf(Token t) { token = t; }
	
    private static ArrayList<ASTree> empty = new ArrayList<ASTree>();
    
    //词组
    protected Token token;
    
    //儿子
    public ASTree child(int i) { throw new IndexOutOfBoundsException(); }
    //儿子数量，默认为0.如果为1，由子类重写该函数
    public int numChildren() { return 0; }
    
    //迭代器
    public Iterator<ASTree> children() { return empty.iterator(); }
    
    //值
    public String toString() { return token.getText(); }
    
    //所在的代码行数
    public String location() { return "at line " + token.getLineNumber(); }
    
    //该叶子包含的单词
    public Token token() { return token; }
}
