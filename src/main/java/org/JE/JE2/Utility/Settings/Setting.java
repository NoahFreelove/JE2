package org.JE.JE2.Utility.Settings;

import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.Base64;

public class Setting<T> {
    private final ArrayList<SettingRunnable<T>> onChange = new ArrayList<>();
    private final String name;
    private T value;

    public Setting(String name, T value) {
        this.name = name;
        this.value = value;
    }

    public void setValue(T value) {
        this.value = value;
        onChange.forEach(runnable -> runnable.run(name, value));
    }
    public void setValueObject(Object val){
        // cast to T
        setValue((T) val);
    }
    public void addListener(SettingRunnable<T> runnable) {
        onChange.add(runnable);
    }

    public T getValue() {
        return value;
    }
    public String getName() {
        return name;
    }

    public void tryParse(String parse){
        if(value instanceof String){
            setValue((T) parse);
        }
        else if(value instanceof Integer){
            setValue((T) Integer.valueOf(parse));
        }
        else if(value instanceof Double){
            setValue((T) Double.valueOf(parse));
        }
        else if(value instanceof Float){
            setValue((T) Float.valueOf(parse));
        }
        else if(value instanceof Boolean){
            setValue((T) Boolean.valueOf(parse));
        }
        else {
            // last resort, it might be a serialized object
            setValueObject(deserialize(parse));
        }
    }

    private static Object deserialize(String input){
        try {
            byte[] bytes = Base64.getDecoder().decode(input);
            ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
            ObjectInputStream ois = new ObjectInputStream(bais);
            Object o = ois.readObject();
            ois.close();
            return o;
        }
        catch (Exception ignore){
            System.out.println("error deserializing: " + input);
            return new Object();
        }
    }
}
