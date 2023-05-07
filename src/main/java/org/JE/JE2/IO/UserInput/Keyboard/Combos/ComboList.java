package org.JE.JE2.IO.UserInput.Keyboard.Combos;

public record ComboList() {
    public static final KeyCombo UP = new KeyCombo(new int[]{87,265});
    public static final KeyCombo DOWN = new KeyCombo(new int[]{83,264});
    public static final KeyCombo LEFT = new KeyCombo(new int[]{65,263});
    public static final KeyCombo RIGHT = new KeyCombo(new int[]{68,262});
}
