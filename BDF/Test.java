package BDF;

import java.io.File;
import java.util.HashMap;


public class Test {
    public static void main(String[] args) {
        BDF bdf = new BDF(new File("test.bdf"));
        bdf.printObject();
        HashMap<String, Object> fields = new HashMap<String, Object>();
        BDFArray<String> array = new BDFArray<String>(new String[]{"LOTR", "Harry Potter"});
        fields.put("books", array);
        fields.put("name", "Bob");
        fields.put("age", 20);
        ConstantObject co = new ConstantObject("bobperson", fields);
    }
}