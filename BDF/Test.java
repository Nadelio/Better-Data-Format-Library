package BDF;

import java.io.File;


public class Test {
    public static void main(String[] args) {
        BDF bdf = new BDF(new File("test.bdf"), new File("test.schema.bdf"));
        bdf.printObject();
    }
}