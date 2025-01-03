package BDF;

public class Token {
    private int num;
    private double dub;
    private String str;
    private boolean bool;
    private Lexer.Types type;


    public Token(Lexer.Types type, int num) {
        this.num = num;
        this.type = type;
    }

    public Token(Lexer.Types type, double dub) {
        this.dub = dub;
        this.type = type;
    }

    public Token(Lexer.Types type, String str) {
        this.str = str;
        this.type = type;
    }

    public Token(Lexer.Types type, boolean bool) {
        this.bool = bool;
        this.type = type;
    }

    public int getNum() { return this.num; }
    public String getStr() { return this.str; }
    public boolean getBool() { return this.bool; }
    public Lexer.Types getType() { return this.type; }

    public String toString(){
        switch (this.type) {
            case STRING:
                return "STRING: " + this.str;
            case INTEGER:
                return "INTEGER: " + Integer.toString(this.num);
            case BOOLEAN:
                return "BOOLEAN: " + Boolean.toString(this.bool);
            case RBRACKET:
                return "]";
            case LBRACKET:
                return "[";
            case RPAREN:
                return ")";
            case LPAREN:
                return "(";
            case COMMA:
                return ",";
            case RCURLY:
                return "}";
            case LCURLY:
                return "{";
            case IDENTIFIER:
                return "IDENTIFIER: " + this.str;
            case COMMENT:
                return "COMMENT: " + this.str;
            case EOF:
                return "EOF";
            default:
                return "";
        }
    }
}