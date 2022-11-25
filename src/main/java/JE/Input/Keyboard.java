package JE.Input;

import java.util.ArrayList;

public class Keyboard {

    public static ArrayList<KeyPressedEvent> keyPressedEvents = new ArrayList<>();
    public static ArrayList<KeyReleasedEvent> keyReleasedEvents = new ArrayList<>();

    private static final boolean[] keys = new boolean[1024];

    public static void keyPressed(int code) {
        if(code >= keys.length)
            return;
        keys[code] = true;
    }

    public static void keyReleased(int code) {
        if(code >= keys.length)
            return;
        keys[code] = false;
    }

    public static boolean isKeyPressed(int code) {
        return keys[code];
    }

    public static int nameToCode(String keyName) {
        keyName = keyName.toUpperCase();
        return switch (keyName) {
            case "A" -> 65;
            case "B" -> 66;
            case "C" -> 67;
            case "D" -> 68;
            case "E" -> 69;
            case "F" -> 70;
            case "G" -> 71;
            case "H" -> 72;
            case "I" -> 73;
            case "J" -> 74;
            case "K" -> 75;
            case "L" -> 76;
            case "M" -> 77;
            case "N" -> 78;
            case "O" -> 79;
            case "P" -> 80;
            case "Q" -> 81;
            case "R" -> 82;
            case "S" -> 83;
            case "T" -> 84;
            case "U" -> 85;
            case "V" -> 86;
            case "W" -> 87;
            case "X" -> 88;
            case "Y" -> 89;
            case "Z" -> 90;
            case "0" -> 48;
            case "1" -> 49;
            case "2" -> 50;
            case "3" -> 51;
            case "4" -> 52;
            case "5" -> 53;
            case "6" -> 54;
            case "7" -> 55;
            case "8" -> 56;
            case "9" -> 57;
            case "SPACE" -> 32;
            case "ENTER" -> 10;
            case "ESCAPE" -> 27;
            case "BACKSPACE" -> 8;
            case "TAB" -> 9;
            case "CAPS_LOCK" -> 20;
            case "SHIFT" -> 16;
            case "CTRL" -> 17;
            case "ALT" -> 18;
            case "PAUSE" -> 19;
            case "PAGE_UP" -> 33;
            case "PAGE_DOWN" -> 34;
            case "END" -> 35;
            case "HOME" -> 36;
            case "LEFT" -> 263;
            case "UP" -> 265;
            case "RIGHT" -> 262;
            case "DOWN" -> 264;
            case "INSERT" -> 45;
            case "DELETE" -> 46;
            case "NUM_LOCK" -> 144;
            case "SCROLL_LOCK" -> 145;
            case "F1" -> 112;
            case "F2" -> 113;
            case "F3" -> 114;
            case "F4" -> 115;
            case "F5" -> 116;
            case "F6" -> 117;
            case "F7" -> 118;
            case "F8" -> 119;
            case "F9" -> 120;
            case "F10" -> 121;
            case "F11" -> 122;
            case "F12" -> 123;
            case "NUMPAD0" -> 96;
            case "NUMPAD1" -> 97;
            case "NUMPAD2" -> 98;
            case "NUMPAD3" -> 99;
            case "NUMPAD4" -> 100;
            case "NUMPAD5" -> 101;
            case "NUMPAD6" -> 102;
            case "NUMPAD7" -> 103;
            case "NUMPAD8" -> 104;
            case "NUMPAD9" -> 105;
            default -> -1;
        };
    }
}
