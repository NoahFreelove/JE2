package JE.IO.UserInput.Keyboard;

public interface KeyReleasedEvent {
    /**
     * @param key - key released
     * @param mods - modifiers (ex. ctrl,shift,alt)
     */
    void invoke(int key, int mods);
}
