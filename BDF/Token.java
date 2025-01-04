package BDF;

import java.util.Date;

public class Token {
    private int num;
    private double dub;
    private String str;
    private boolean bool;
    private Date date;
    private ISOTimestamp timestamp;
    private Lexer.Types type;


    public Token(Lexer.Types type, int num) {
        this.num = num;
        this.type = type;
    }

    public Token(Lexer.Types type, double dub) {
        this.dub = dub;
        this.type = type;
    }

    public Token(Lexer.Types type, Date date){
        this.date = date;
        this.type = type;
    }

    public Token(Lexer.Types type, ISOTimestamp timestamp){
        this.timestamp = timestamp;
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
    public double getDub() { return this.dub; }
    public String getStr() { return this.str; }
    public boolean getBool() { return this.bool; }
    public Date getDate() { return this.date; }
    public ISOTimestamp getTimestamp() { return this.timestamp; }
    public Lexer.Types getType() { return this.type; }

    @Override
    public String toString(){
        switch (this.type) {
            case STRING:
                return "STRING: " + this.str;
            case INTEGER:
                return "INTEGER: " + Integer.toString(this.num);
            case DOUBLE:
                return "DOUBLE: " + Double.toString(this.dub);
            case BINARY:
                return "BINARY: " + this.str;
            case DATE:
                return "DATE: " + this.date.toString();
            case TIMESTAMP:
                return "TIMESTAMP: " + this.timestamp.toString();
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
            case SCHEMA_INSTRUCTION:
                return "SCHEMA_INSTRUCTION: " + this.str;
            case COMMENT:
                return "COMMENT: " + this.str;
            case EOF:
                return "EOF";
            default:
                return "";
        }
    }
}