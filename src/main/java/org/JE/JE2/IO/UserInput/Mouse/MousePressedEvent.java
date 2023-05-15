package org.JE.JE2.IO.UserInput.Mouse;

public interface MousePressedEvent {
    /**
     * @param button - button pressed (0 - right, 1 - left, 2 - middle)
     * @param mods - modifiers (ex. ctrl,shift,alt)
     */
    void invoke(MouseButton button, int mods);
}
