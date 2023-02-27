package org.JE.JE2.IO.UserInput.Keyboard.Combos;

public class KeyCombo {
    int key1;
    int key2;

    public KeyCombo(int key1, int key2){
        this.key1 = key1;
        this.key2 = key2;
    }

    public boolean or(int key){
        return (key == key1 || key == key2);
    }

    public boolean or(boolean[] keys){
        return (keys[key1] || keys[key2]);
    }
}
