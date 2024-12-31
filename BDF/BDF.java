package BDF;

import java.io.File;
import java.nio.file.Files;

public class BDF{
    private File input;
    private BDFAST data;
    private Lexer lexer;
    private Parser parser;
    
    public BDF(File input){
        this.input = input;
        String inputString = getAsString(input);
        this.lexer = new Lexer(inputString);
        this.parser = new Parser(lexer);
    }

    public BDF(BDFAST data){ this.data = data; }

    private String getAsString(File file){
        try{
            byte[] bytes = Files.readAllBytes(file.toPath());
            return new String(bytes);
        } catch (Exception e){
            e.printStackTrace();
            return "";
        }
    }

    public void printObject(){
        Token[] tokens = lexer.lex();
        for (Token token : tokens){ System.out.println(token.toString()); }
    }
}