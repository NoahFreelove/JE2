package JE.IO.UserInput;


public interface KeyPressedEvent {
    /**
     * @param key - key pressed
     * @param mods - modifiers (ex. ctrl,shift,alt)
     */
    void invoke(int key, int mods);
}
