package org.JE.JE2.IO.UserInput.Keyboard;

import org.JE.JE2.IO.UserInput.Keyboard.Combos.KeyCombo;
import org.JE.JE2.Window.UIHandler;
import org.JE.JE2.Window.Window;

import java.util.ArrayList;

import static org.lwjgl.glfw.GLFW.glfwGetClipboardString;
import static org.lwjgl.glfw.GLFW.glfwSetClipboardString;
import static org.lwjgl.nuklear.Nuklear.nk_input_unicode;
import static org.lwjgl.system.MemoryUtil.memAddress;
import static org.lwjgl.system.MemoryUtil.memCopy;

public class Keyboard {

    public static boolean ignoreCopyPasteKeys = false;

    private static final ArrayList<KeyPressedEvent> keyPressedEvents = new ArrayList<>();
    private static final ArrayList<KeyReleasedEvent> keyReleasedEvents = new ArrayList<>();

    public static void addKeyPressedEvent(KeyPressedEvent event){
        keyPressedEvents.add(event);
    }
    public static void addKeyReleasedEvent(KeyReleasedEvent event){
        keyReleasedEvents.add(event);
    }

    public static void removeKeyPressedEvent(KeyPressedEvent event){
        keyPressedEvents.remove(event);
    }
    public static void removeKeyReleasedEvent(KeyReleasedEvent event){
        keyReleasedEvents.remove(event);
    }

    public static void triggerKeyPressed(int code, int mods){
        keyPressedEvents.forEach(event -> event.invoke(code, mods));
        keyPressed(code);
        if(ignoreCopyPasteKeys)
            return;

        if((mods & 2) != 0){
            if(code == nameToCode("V")){
                pasteClipboard();
            }
        }
        if((mods & 2) != 0){
            if(code == nameToCode("C")){
                copyToClipboard("");
            }
        }
    }
    public static void triggerKeyReleased(int code, int mods){
        keyReleasedEvents.forEach(event -> event.invoke(code, mods));
        keyReleased(code);
    }
    public static void triggerKeyRepeat(int key, int mods) {
       triggerKeyPressed(key, mods);
    }

    public static void triggerKey(int code, int mods){
        triggerKeyPressed(code, mods);
        triggerKeyReleased(code, mods);
    }

    private static void onPaste(String str){
        if (str != null) {
            for (char c :
                    str.toCharArray()) {
                nk_input_unicode(UIHandler.nuklearContext, c);
            }
        }
    }

    public static void pasteClipboard(){
        onPaste(glfwGetClipboardString(Window.handle()));
    }

    public static void copyToClipboard(String str){
        glfwSetClipboardString(Window.handle(),str);

    }

    private static final boolean[] keys = new boolean[1024];

    private static void keyPressed(int code) {
        if(within(0,keys.length,code))
            keys[code] = true;
    }

    private static void keyReleased(int code) {
        if(within(0,keys.length,code))
            keys[code] = false;
    }

    public static boolean within(int min, int max, int v){
        return v >= min && v <= max;
    }

    public static boolean isKeyPressed(int code) {
        return keys[code];
    }

    public static boolean isComboPressed(KeyCombo combo){
        return combo.or(keys);
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
            case "F1" -> 290;
            case "F2" -> 291;
            case "F3" -> 292;
            case "F4" -> 293;
            case "F5" -> 294;
            case "F6" -> 295;
            case "F7" -> 296;
            case "F8" -> 297;
            case "F9" -> 298;
            case "F10" -> 299;
            case "F11" -> 300;
            case "F12" -> 301;
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
    public static String codeToName(int code) {
        return switch (code) {
            case 65 -> "A";
            case 66 -> "B";
            case 67 -> "C";
            case 68 -> "D";
            case 69 -> "E";
            case 70 -> "F";
            case 71 -> "G";
            case 72 -> "H";
            case 73 -> "I";
            case 74 -> "J";
            case 75 -> "K";
            case 76 -> "L";
            case 77 -> "M";
            case 78 -> "N";
            case 79 -> "O";
            case 80 -> "P";
            case 81 -> "Q";
            case 82 -> "R";
            case 83 -> "S";
            case 84 -> "T";
            case 85 -> "U";
            case 86 -> "V";
            case 87 -> "W";
            case 88 -> "X";
            case 89 -> "Y";
            case 90 -> "Z";
            case 48 -> "0";
            case 49 -> "1";
            case 50 -> "2";
            case 51 -> "3";
            case 52 -> "4";
            case 53 -> "5";
            case 54 -> "6";
            case 55 -> "7";
            case 56 -> "8";
            case 57 -> "9";
            case 32 -> "SPACE";
            case 10 -> "ENTER";
            case 27 -> "ESCAPE";
            case 8 -> "BACKSPACE";
            case 9 -> "TAB";
            case 20 -> "CAPS_LOCK";
            case 16 -> "SHIFT";
            case 17 -> "CTRL";
            case 18 -> "ALT";
            case 19 -> "PAUSE";
            case 33 -> "PAGE_UP";
            case 34 -> "PAGE_DOWN";
            case 35 -> "END";
            case 36 -> "HOME";
            case 263 -> "LEFT";
            case 265 -> "UP";
            case 262 -> "RIGHT";
            case 264 -> "DOWN";
            case 45 -> "INSERT";
            case 46 -> "DELETE";
            case 144 -> "NUM_LOCK";
            case 145 -> "SCROLL_LOCK";
            case 290 -> "F1";
            case 291 -> "F2";
            case 292 -> "F3";
            case 293 -> "F4";
            case 294 -> "F5";
            case 295 -> "F6";
            case 296 -> "F7";
            case 297 -> "F8";
            case 298 -> "F9";
            case 299 -> "F10";
            case 300 -> "F11";
            case 301 -> "F12";
            default -> "UNKNOWN";
        };
    }


}
