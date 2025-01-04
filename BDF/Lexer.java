package BDF;

import java.util.ArrayList;
import java.util.Date;

public class Lexer {
    private String input;
    private String schema;
    private int index;
    private char currentChar;

    public enum Types{
        STRING,
        INTEGER,
        DOUBLE,
        BOOLEAN,
        BINARY,
        DATE,
        TIMESTAMP,
        RBRACKET,
        LBRACKET,
        RPAREN,
        LPAREN,
        COMMA,
        RCURLY,
        LCURLY,
        IDENTIFIER,
        SCHEMA_INSTRUCTION,
        COMMENT,
        EOF
    }

    public Lexer(String input) {
        this.input = input;
        this.index = -1;
        this.currentChar = input.charAt(0);
    }

    public Lexer(String input, String schema) {
        this.input = input;
        this.schema = schema;
        this.index = -1;
        this.currentChar = input.charAt(0);
    }

    public ArrayList<Token> lex() {
        ArrayList<Token> tokens = new ArrayList<Token>();
        Token token = parseToken();
        while (token.getType() != Types.EOF) {
            tokens.add(token);
            token = parseToken();
        }
        return tokens;
    }

    public ArrayList<Token> lexSchema(){
        if(this.schema == null){ throw new Error("No schema provided"); }
        
        String input = this.input;
        this.input = this.schema;
        ArrayList<Token> tokens = new ArrayList<Token>();

        Token token = parseToken();
        while (token.getType() != Types.EOF) {
            tokens.add(token);
            token = parseToken();
        }

        this.input = input;
        return tokens;
    }

    private void consume() {
        this.index++;
        if (this.index >= this.input.length()) {
            this.currentChar = '\0';
        } else {
            this.currentChar = this.input.charAt(this.index);
        }
    }

    private void skipWhitespace() {
        while (this.currentChar == ' ' || this.currentChar == '\t' || this.currentChar == '\n' || this.currentChar == '\r') {
            consume();
        }
    }

    private void match(char c) {
        if (this.currentChar == c) {
            consume();
        } else {
            throw new Error("Expecting " + c + "; found " + this.currentChar);
        }
    }

    private boolean isDigit(char c) {
        return c >= '0' && c <= '9' || c == '-';
    }

    private boolean isBinaryCharacter(char c) {
        return (c >= '0' && c <= '1') || (c >= 'A' && c <= 'F');
    }

    private String parseIdentifier() {
        StringBuilder result = new StringBuilder();
        while (Character.isLetterOrDigit(this.currentChar) || this.currentChar == '_' || this.currentChar == '-') {
            result.append(this.currentChar);
            consume();
        }
        return result.toString();
    }

    private String parseString() {
        match('"');
        StringBuilder result = new StringBuilder();
        while (this.currentChar != '"') {
            result.append(this.currentChar);
            consume();
        }
        match('"');
        return result.toString();
    }

    private int parseInteger() {
        StringBuilder result = new StringBuilder();
        while (Character.isDigit(this.currentChar)) {
            result.append(this.currentChar);
            consume();
        }
        return Integer.parseInt(result.toString());
    }

    private String parseDecimal() {
        StringBuilder result = new StringBuilder();
        while (Character.isDigit(this.currentChar)) {
            result.append(this.currentChar);
            consume();
        }
        return result.toString();
    }

    private Token parseToken(){
        skipWhitespace();
        if(this.currentChar == '\0'){ return new Token(Types.EOF, "\0"); }

        switch (this.currentChar) {
            case '"':
                return new Token(Types.STRING, parseString());
            case ']':
                consume();
                return new Token(Types.RBRACKET, "]");
            case '[':
                consume();
                return new Token(Types.LBRACKET, "[");
            case ')':
                consume();
                return new Token(Types.RPAREN, ")");
            case '(':
                consume();
                return new Token(Types.LPAREN, "(");
            case ',':
                consume();
                return new Token(Types.COMMA, ",");
            case '}':
                consume();
                return new Token(Types.RCURLY, "}");
            case '{':
                consume();
                return new Token(Types.LCURLY, "{");
            case '$':
                consume();
                return new Token(Types.SCHEMA_INSTRUCTION, parseIdentifier());
            case '#':
                consume();
                StringBuilder r = new StringBuilder();
                while(isBinaryCharacter(this.currentChar)){
                    r.append(this.currentChar);
                    consume();
                }
                return new Token(Types.BINARY, r.toString());
            case '/':
                consume();
                if(this.currentChar == '/'){
                    consume();
                    StringBuilder result = new StringBuilder();
                    while(this.currentChar != '\n' && this.currentChar != '\r' && this.currentChar != '\0' && this.index < this.input.length()){
                        result.append(this.currentChar);
                        consume();
                    }
                    return new Token(Types.COMMENT, result.toString());
                } else if(this.currentChar == '*'){
                    consume();
                    StringBuilder result = new StringBuilder();
                    while(this.currentChar != '*' && this.input.charAt(this.index + 1) != '/'){
                        result.append(this.currentChar);
                        consume();
                    }
                    consume();
                    consume();
                    return new Token(Types.COMMENT, result.toString());
                }
                return new Token(Types.IDENTIFIER, parseIdentifier());
            default:
                if(isDigit(currentChar)){
                    int num = parseInteger();
                    switch(currentChar){
                        case '-':
                            consume();
                            int month = parseInteger();
                            if(currentChar == '-'){
                                consume();
                                int day = parseInteger();
                                return new Token(Types.DATE, new Date(num, month, day));
                            } else {
                                throw new Error("Invalid date format @ " + this.index);
                            }
                        case ':':
                            consume();
                            int minutes = parseInteger();
                            if(currentChar == ':'){
                                consume();
                                int seconds = parseInteger();
                                return new Token(Types.TIMESTAMP, new ISOTimestamp(num, minutes, seconds));
                            }
                            return new Token(Types.INTEGER, num);
                        case '.':
                            consume();
                            String decimal = parseDecimal();
                            String d = num + "." + decimal;
                            return new Token(Types.DOUBLE, Double.parseDouble(d));
                        default:
                            return new Token(Types.INTEGER, num);
                    }
                } else if(currentChar == 't' || currentChar == 'f'){
                    String bool = parseIdentifier();
                    if(bool.equals("true")){
                        return new Token(Types.BOOLEAN, true);
                    } else if(bool.equals("false")){
                        return new Token(Types.BOOLEAN, false);
                    }
                    return new Token(Types.IDENTIFIER, parseIdentifier());
                }
                return new Token(Types.IDENTIFIER, parseIdentifier());
        }
        
    }
}
