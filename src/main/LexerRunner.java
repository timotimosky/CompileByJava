package main;

import core.Lexer;
import core.Token;
import exception.ParseException;

//´Ê×é½âÎö ²âÊÔ
public class LexerRunner {
    public static void main(String[] args) throws ParseException {
        Lexer l = new Lexer(new CodeDialog());
        for (Token t; (t = l.read()) != Token.EOF; )
            System.out.println("=> " + t.getText());
    }
}
