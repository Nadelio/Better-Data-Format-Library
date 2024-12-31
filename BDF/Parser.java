package BDF;

public class Parser {
    private Lexer lexer;
    private Token[] tokens;

    public Parser(Lexer lexer){
        this.lexer = lexer;
        this.tokens = lexer.lex();
    }
}