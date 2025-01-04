package BDF;

import java.util.ArrayList;

public class Parser {
    private Lexer lexer;
    private ArrayList<Token> tokens;

    public Parser(Lexer lexer){
        this.lexer = lexer;
        this.tokens = lexer.lex();
    }

    public ArrayList<Token> getInput(){ return tokens; }
}