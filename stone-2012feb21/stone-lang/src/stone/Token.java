package stone;

//基础的语言对象
public abstract class Token {
	//文件结尾
    public static final Token EOF = new Token(-1){}; 
    //换行符
    public static final String EOL = "\\n";       
    //所在的代码行数
    private int lineNumber;

    protected Token(int line) {
        lineNumber = line;
    }
    public int getLineNumber() { return lineNumber; }
    public boolean isIdentifier() { return false; }
    public boolean isNumber() { return false; }
    public boolean isString() { return false; }
    public int getNumber() { throw new StoneException("not number token"); }
    public String getText() { return ""; }
}
