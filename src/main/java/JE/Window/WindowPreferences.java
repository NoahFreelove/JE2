package JE.Window;

import org.joml.Vector2i;

public class WindowPreferences {
    public Vector2i windowSize = new Vector2i(1920, 1080);
    public String windowTitle = "JE";
    public boolean windowResizable = false;

    public WindowPreferences(){}

    public WindowPreferences(Vector2i windowSize, String windowTitle, boolean windowResizable) {
        this.windowSize = windowSize;
        this.windowTitle = windowTitle;
        this.windowResizable = windowResizable;
    }
}
