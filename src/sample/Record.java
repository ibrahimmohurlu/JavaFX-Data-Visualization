package sample;

import java.util.Collection;
import java.util.HashMap;

public class Record implements Comparable<Record> {
    private String[] keys = {"Year", "Name", "Country", "Value", "Category"};
    private HashMap<String, String> fields = new HashMap<>();

    Record() {

    }

    Record(String[] fields) {
        for (int i = 0; i < keys.length; i++) {
            this.fields.put(keys[i], fields[i]);
        }

    }

    public void setFields(String key, String val) {
        fields.put(key, val);
    }

    public void printFieldsLength() {
        System.out.println(fields.size());
    }

    public String getVal(String key) {
        return fields.get(key);
    }

    public int getHashMapSize() {
        return fields.size();
    }

    public Collection<String> getValues() {
        return fields.values();
    }

    public Collection<String> getKeys() {
        return fields.keySet();
    }

    public void printValues() {
        Collection<String> rec = fields.values();
        for (String i : rec) {
            System.out.println(i);
        }
    }


    @Override
    public int compareTo(Record o) {
        int result;
        int value = Integer.parseInt(this.fields.get("Value"));
        int otherValue = Integer.parseInt(o.fields.get("Value"));
        if (value > otherValue) {
            result = 1;
        } else if (value < otherValue) {
            result = -1;
        } else {
            result = 0;
        }
        return result;
    }
}


