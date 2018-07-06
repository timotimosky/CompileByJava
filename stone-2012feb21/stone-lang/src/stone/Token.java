package stone;

//���������Զ���
public abstract class Token {
	//�ļ���β
    public static final Token EOF = new Token(-1){}; 
    //���з�
    public static final String EOL = "\\n";       
    //���ڵĴ�������
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