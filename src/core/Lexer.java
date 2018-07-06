package core;

import java.io.IOException;
import java.io.LineNumberReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import exception.ParseException;

//�ʷ�����
public class Lexer {
	
    public static String regexPat
        = "\\s*((//.*)|([0-9]+)|"
        		+ "(\"(\\\\\"|\\\\\\\\|\\\\n|[^\"])*\")"
          + "|[A-Za-z][A-Za-z0-9]*|==|<=|>=|&&|\\|\\||\\p{Punct})?";
    
    //����ƥ��
    private Pattern pattern = Pattern.compile(regexPat);
    
    //��¼���������
    private ArrayList<Token> queue = new ArrayList<Token>();
    //�Ƿ�����һ������
    private boolean hasMore;
    //java�Դ��İ�������ȡ�ı�����
    private LineNumberReader reader;
    //����
    public Lexer(Reader r) {
        hasMore = true;
        reader = new LineNumberReader(r);
    }
    
    //�Ӵ��������ȡ��һ������
    public Token read() throws ParseException {
        if (fillQueue(0))
            return queue.remove(0);
        else
            return Token.EOF;
    }
    
    //Ԥ����һ�����飬���Ӵ��������ɾ��
    public Token peek(int i) throws ParseException {
        if (fillQueue(i))
            return queue.get(i);
        else
            return Token.EOF;
    }
    
    //ѭ����ȡ�ı���Ȼ������������
    private boolean fillQueue(int i) throws ParseException {
        while (i >= queue.size())
            if (hasMore)
                readLine();
            else
                return false;
        return true;
    }
    
    //��ȡ�ı���Ȼ������������
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
        int lineNo = reader.getLineNumber(); //��������
        
        //ʹ������ƥ��
        Matcher matcher = pattern.matcher(line);//���Խ����������������ģʽƥ�䡣
        matcher.useTransparentBounds(true).useAnchoringBounds(false); //�����߽�
        
        int pos = 0;//��ʼ
        int endPos = line.length();//����
        
        while (pos < endPos) 
        {
            matcher.region(pos, endPos);
            if (matcher.lookingAt())//���Խ��������д�ͷ��ʼ���ģʽƥ�䡣
            {
                addToken(lineNo, matcher);
                pos = matcher.end();
            }
            else
            	throw new ParseException("bad token at line " + lineNo);
        }
        
        queue.add(new IdToken(lineNo, Token.EOL)); //��󣬼�����ֹ��
    }
    
    //����������������
    protected void addToken(int lineNo, Matcher matcher) {
    	
    	//�����ж�
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
    
    //ת��string��ɾ��˫���� 
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
                    c = s.charAt(++i); //ȡString����һ��char
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

    
    //��������
    protected static class NumToken extends Token {
        private int value;
        protected NumToken(int line, int v) {
            super(Type.Number,line);
            value = v;
        }
        public String getText() { return Integer.toString(value); }
        public int getNumber() { return value; }
    }

    //��ʶ������
    protected static class IdToken extends Token {
        private String text;
        protected IdToken(int line, String id) {
            super(Type.Identifier,line);
            text = id;
        }

        public String getText() { return text; }
    }

    //�ַ�������
    protected static class StrToken extends Token {
        private String literal;
        StrToken(int line, String str) {
            super(Type.String,line);
            literal = str;
        }
        public String getText() { return literal; }
    }
}
