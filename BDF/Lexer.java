package BDF;

import java.util.ArrayList;

public class Lexer {
    private String input;
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
        COMMENT,
        EOF
    }

    public Lexer(String input) {
        this.input = input;
        this.index = -1;
        this.currentChar = input.charAt(0);
    }

    public Token[] lex() {
        ArrayList<Token> tokens = new ArrayList<Token>();
        Token token = parseToken();
        while (token.getType() != Types.EOF) {
            tokens.add(token);
            token = parseToken();
        }
        return tokens.toArray(new Token[0]);
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

    private boolean isDate() {
        if(this.index + 9 >= this.input.length()){ return false; }
        if((this.input.charAt(this.index + 4) != '-') || (this.input.charAt(this.index + 7) != '-')){ return false; }
        if(isValidMonth() && isValidDay(this.input.indexOf((int) '-', this.index + 5))){ return true; }
        return false;
    }

    private boolean isValidMonth(){
        return Integer.parseInt(this.input.substring(this.index + 5, this.index + 7)) <= 12 && Integer.parseInt(this.input.substring(this.index + 5, this.index + 7)) >= 1;
    }

    private boolean isValidDay(int index){
        return Integer.parseInt(this.input.substring(index + 1)) <= 31 && Integer.parseInt(this.input.substring(index + 1)) >= 1;
    }

    private boolean isTimestamp(){
        int index = this.index;
        if(isValidHour() && isValidMinute() && isValidSecond()){ 
            this.index = index;
            return true;
        }
        this.index = index;
        return false;
    }

    private boolean isValidHour(){
        int hour = parseInteger();
        if(hour >= 0 && hour <= 24){
            return true;
        }
        return false;
    }

    private boolean isValidMinute(){
        if(!Character.isDigit(currentChar)){return false;}
        int minute = parseInteger();
        if(minute >= 0 && minute <= 59){
            return true;
        }
        return false;
    }

    private boolean isValidSecond(){
        if(!Character.isDigit(currentChar)){return false;}
        int second = parseInteger();
        if(second >= 0 && second <= 59){
            return true;
        }
        return false;
    }

    private boolean isDouble(){
        try{
            String doubleString = this.input.substring(this.index, this.input.indexOf(' ', this.index));
            if(!doubleString.contains(".")){ return false; }
            Double.parseDouble(doubleString);
            return true;
        } catch (NumberFormatException e){ return false; }
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

    private String parseDate(){
        StringBuilder result = new StringBuilder();
        while (Character.isDigit(this.currentChar) || this.currentChar == '-') {
            result.append(this.currentChar);
            consume();
        }
        return result.toString();
    }

    private String parseTimestamp(){
        StringBuilder result = new StringBuilder();
        while (Character.isDigit(this.currentChar) || this.currentChar == '-' || this.currentChar == ':' || this.currentChar == 'T' || this.currentChar == 'Z') {
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
            case '#':
                System.out.println("Binary");
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
                    while(this.currentChar != '\n' && this.currentChar != '\r'){
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

                System.out.println("Identifier");
                return new Token(Types.IDENTIFIER, parseIdentifier());
            default:
                if(isDigit(currentChar)){
                    if(isDate()){
                        System.out.println("Date");
                        return new Token(Types.DATE, parseDate());
                    } else if(isTimestamp()){
                        System.out.println("Timestamp");
                        return new Token(Types.TIMESTAMP, parseTimestamp());
                    } else if(isDouble()){
                        System.out.println("Double");
                        String doubleString = this.input.substring(this.index, this.input.indexOf(' ', this.index));
                        System.out.println(doubleString);
                        return new Token(Types.DOUBLE, Double.parseDouble(doubleString));
                    } else {
                        System.out.println("Integer");
                        return new Token(Types.INTEGER, parseInteger());
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
