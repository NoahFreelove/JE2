package org.JE.JE2.IO.UserInput.Keyboard.Combos;

public record KeyCombo(int[] keys) {

    public boolean or(boolean[] inputKeys) {
        for (int key : keys) {
            if (inputKeys[key])
                return true;
        }
        return false;
    }

    public boolean and(boolean[] inputKeys){
        for (int key : keys) {
            if (!inputKeys[key])
                return false;
        }
        return true;
    }
}
