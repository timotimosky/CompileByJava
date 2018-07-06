package stone;
import java.io.IOException;
import java.io.LineNumberReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

//词法分析器
public class Lexer {
	
	//正则表达式，用于匹配单词，然后分割单词
    public static String regexPat
        = "\\s*((//.*)|([0-9]+)|(\"(\\\\\"|\\\\\\\\|\\\\n|[^\"])*\")"
          + "|[A-Z_a-z][A-Z_a-z0-9]*|==|<=|>=|&&|\\|\\||\\p{Punct})?";
    
    //将给定的正则表达式编译并赋予给Pattern类，Pattern是java.util.regex自带的
    private Pattern pattern = Pattern.compile(regexPat);
    
    //存储 语言对象（字符串、整形、标识符）
    private ArrayList<Token> queue = new ArrayList<Token>();
    //是否有下一个
    private boolean hasMore;
    //LineNumberReader是BufferedReader的子类，用来按行读取文本文件。
    private LineNumberReader reader;

    public Lexer(Reader r) {
        hasMore = true;
        reader = new LineNumberReader(r);
    }
    
    //读，用于构建语法树
    public Token read() throws ParseException {
        if (fillQueue(0))
            return queue.remove(0);
        else
            return Token.EOF;
    }
    public Token peek(int i) throws ParseException {
        if (fillQueue(i))
            return queue.get(i);
        else
            return Token.EOF; 
    }
    
    //填充 语言对象的队列
    //判断索引是否超过队列长度，如果超过，判断是否有后续可读，如果有，则读取
    private boolean fillQueue(int i) throws ParseException {
        while (i >= queue.size())
            if (hasMore)
                readLine();
            else
                return false;
        return true;
    }
    
    //readLine 与 addToken 是词法分析的核心部分，其他都只是起辅助作用，
  
    //从每一行中读取单词的方法
    protected void readLine() throws ParseException {
        String line;
        try {
            line = reader.readLine();
        } catch (IOException e) {
            throw new ParseException(e);
        }
        if (line == null) {
            hasMore = false;
            return;
        }
        //获取行列数
        int lineNo = reader.getLineNumber();
        //使用该 Matcher实例以编译的正则表达式为基础对目标字符串进行匹配工作，多个Matcher是可以共用一个Pattern对象的
        Matcher matcher = pattern.matcher(line);
        matcher.useTransparentBounds(true).useAnchoringBounds(false);
        int pos = 0;
        int endPos = line.length();
        while (pos < endPos) {
        	//通过 region 方法限定该对象检查匹配的范围
            matcher.region(pos, endPos);
            //通过 lookingAt 方法在检查范围内进行正则表达式匹配。
            if (matcher.lookingAt()) {
                addToken(lineNo, matcher);
                pos = matcher.end();
            }
            else
                throw new ParseException("bad token at line " + lineNo);
        }
        queue.add(new IdToken(lineNo, Token.EOL));
    }
    
    //
    protected void addToken(int lineNo, Matcher matcher) {
    	
    	//Matcher类: 
    	    //使用Matcher类,最重要的一个概念必须清楚:组(Group),在正则表达式中 ()定义了一个组
    	
    	//获取第一个组
        String m = matcher.group(1);
        if (m != null)
        {// if not a space
            if (matcher.group(2) == null) 
            { // if not a comment
                Token token;
                if (matcher.group(3) != null)
                    token = new NumToken(lineNo, Integer.parseInt(m));
                else if (matcher.group(4) != null)
                    token = new StrToken(lineNo, toStringLiteral(m));
                else
                    token = new IdToken(lineNo, m);
                queue.add(token);
            }
        }
    }
    
    protected String toStringLiteral(String s) {
        StringBuilder sb = new StringBuilder();
        int len = s.length() - 1;
        for (int i = 1; i < len; i++) 
        {
            char c = s.charAt(i);
            if (c == '\\' && i + 1 < len) 
            {
                int c2 = s.charAt(i + 1);
                if (c2 == '"' || c2 == '\\')
                    c = s.charAt(++i);
                else if (c2 == 'n') 
                {
                    ++i;
                    c = '\n';
                }
            }
            sb.append(c);
        }
        return sb.toString();
    }

    
    
    //NumToken、IdToken 与 StrToken 类是 Token 类的子类。它们分别对应不同类型的单词。
    
    protected static class NumToken extends Token {
        private int value;

        protected NumToken(int line, int v) {
            super(line);
            value = v;
        }
        public boolean isNumber() { return true; }
        public String getText() { return Integer.toString(value); }
        public int getNumber() { return value; }
    }

    protected static class IdToken extends Token {
        private String text; 
        protected IdToken(int line, String id) {
            super(line);
            text = id;
        }
        public boolean isIdentifier() { return true; }
        public String getText() { return text; }
    }

    protected static class StrToken extends Token {
        private String literal;
        StrToken(int line, String str) {
            super(line);
            literal = str;
        }
        public boolean isString() { return true; }
        public String getText() { return literal; }
    }
}
