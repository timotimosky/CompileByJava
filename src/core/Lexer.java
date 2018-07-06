package core;

import java.io.IOException;
import java.io.LineNumberReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import exception.ParseException;

//词法分析
public class Lexer {
	
    public static String regexPat
        = "\\s*((//.*)|([0-9]+)|"
        		+ "(\"(\\\\\"|\\\\\\\\|\\\\n|[^\"])*\")"
          + "|[A-Za-z][A-Za-z0-9]*|==|<=|>=|&&|\\|\\||\\p{Punct})?";
    
    //正则匹配
    private Pattern pattern = Pattern.compile(regexPat);
    
    //记录词组的链表
    private ArrayList<Token> queue = new ArrayList<Token>();
    //是否有下一个词组
    private boolean hasMore;
    //java自带的按行来读取文本的类
    private LineNumberReader reader;
    //构造
    public Lexer(Reader r) {
        hasMore = true;
        reader = new LineNumberReader(r);
    }
    
    //从词组队列里取出一个词组
    public Token read() throws ParseException {
        if (fillQueue(0))
            return queue.remove(0);
        else
            return Token.EOF;
    }
    
    //预读下一个词组，不从词组队列里删除
    public Token peek(int i) throws ParseException {
        if (fillQueue(i))
            return queue.get(i);
        else
            return Token.EOF;
    }
    
    //循环读取文本，然后填充词组链表
    private boolean fillQueue(int i) throws ParseException {
        while (i >= queue.size())
            if (hasMore)
                readLine();
            else
                return false;
        return true;
    }
    
    //读取文本，然后填充词组链表
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
        int lineNo = reader.getLineNumber(); //所在行数
        
        //使用正则匹配
        Matcher matcher = pattern.matcher(line);//尝试将整个输入序列与该模式匹配。
        matcher.useTransparentBounds(true).useAnchoringBounds(false); //启动边界
        
        int pos = 0;//开始
        int endPos = line.length();//结束
        
        while (pos < endPos) 
        {
            matcher.region(pos, endPos);
            if (matcher.lookingAt())//尝试将输入序列从头开始与该模式匹配。
            {
                addToken(lineNo, matcher);
                pos = matcher.end();
            }
            else
            	throw new ParseException("bad token at line " + lineNo);
        }
        
        queue.add(new IdToken(lineNo, Token.EOL)); //最后，加上终止符
    }
    
    //将词组加入词组链表
    protected void addToken(int lineNo, Matcher matcher) {
    	
    	//括号判断
        String m = matcher.group(1);
        if (m != null)
        {
            if (matcher.group(2) == null) 
            {
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
    
    //转换string，删除双引号 
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
                    c = s.charAt(++i); //取String的下一个char
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

    
    //整数词组
    protected static class NumToken extends Token {
        private int value;
        protected NumToken(int line, int v) {
            super(Type.Number,line);
            value = v;
        }
        public String getText() { return Integer.toString(value); }
        public int getNumber() { return value; }
    }

    //标识符词组
    protected static class IdToken extends Token {
        private String text;
        protected IdToken(int line, String id) {
            super(Type.Identifier,line);
            text = id;
        }

        public String getText() { return text; }
    }

    //字符串词组
    protected static class StrToken extends Token {
        private String literal;
        StrToken(int line, String str) {
            super(Type.String,line);
            literal = str;
        }
        public String getText() { return literal; }
    }
}
