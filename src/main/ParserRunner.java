package main;

import core.BasicParser;
import core.Lexer;
import core.Token;
import exception.ParseException;
import test3.ASTree;

//������� ����
public class ParserRunner {
    public static void main(String[] args) throws ParseException {
    	
        Lexer l = new Lexer(new CodeDialog());
        BasicParser bp = new BasicParser();
        
        while (l.peek(0) != Token.EOF) {
            ASTree ast = bp.parse(l);
            System.out.println("=> " + ast.toString());
        }
    }
}
