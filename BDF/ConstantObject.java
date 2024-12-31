package BDF;

import java.util.HashMap;
import java.util.Map;

public class ConstantObject {
    private String name;
    private Map<String, Object> fields = new HashMap<String, Object>();

    public ConstantObject(String name, HashMap<String, Object> fields){
        this.name = name;
        this.fields = fields;
    }

    public String getName(){ return name; }
    public Map<String, Object> getFields(){ return fields; }
}