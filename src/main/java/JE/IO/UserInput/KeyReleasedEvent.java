package JE.IO.UserInput;

public interface KeyReleasedEvent {
    /**
     * @param key - key released
     * @param mods - modifiers (ex. ctrl,shift,alt)
     */
    void invoke(int key, int mods);
}
