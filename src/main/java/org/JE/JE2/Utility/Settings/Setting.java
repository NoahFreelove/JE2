package org.JE.JE2.Utility.Settings;

import java.util.ArrayList;

public class Setting<T> {
    private final ArrayList<SettingRunnable<T>> onChange = new ArrayList<>();
    private final String name;
    private T value;

    public Setting(String name, T value) {
        this.name = name;
        this.value = value;
    }

    public void setValue(T value) {
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
}
