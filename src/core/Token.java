package core;

import java.util.HashSet;

import exception.StoneException;

public abstract class Token {
	
    public static final Token EOF = new Token(Type.Sign,-1){};  // end of file
    public static final String EOL = "\\n";          // end of line
    private int lineNumber; //所在的代码行数

    protected Token(Type mtype,int line) {
        lineNumber = line;
        type = mtype;
    }
     
    
    public static enum Type
    {
    	//目前只用这三个
    	Identifier,
    	Number,
    	String,
    	//下面的备用 	
    	Keyword,  Sign, Annotation,
		RegEx, Space, NewLine, EndSymbol;
    	
    }
    
    public final Type type;
    
    public int getLineNumber() { return lineNumber; }
    public int getNumber() { throw new StoneException("not number token"); }
    public String getText() { return ""; }
    
    
    //加入一个关键词列表
    private static final HashSet<String> keywordsSet = new HashSet<>();
	
    static {
    	keywordsSet.add("if");
    	keywordsSet.add("when");
    	keywordsSet.add("elsif");
    	keywordsSet.add("else");
    	keywordsSet.add("while");
    	keywordsSet.add("begin");
    	keywordsSet.add("until");
    	keywordsSet.add("for");
    	keywordsSet.add("do");
    	keywordsSet.add("try");
    	keywordsSet.add("catch");
    	keywordsSet.add("finally");
    	keywordsSet.add("end");
    	keywordsSet.add("def");
    	keywordsSet.add("var");
    	keywordsSet.add("this");
    	keywordsSet.add("null");
    	keywordsSet.add("throw");
    	keywordsSet.add("break");
    	keywordsSet.add("continue");
    	keywordsSet.add("return");
    	keywordsSet.add("operator");
    }
}
