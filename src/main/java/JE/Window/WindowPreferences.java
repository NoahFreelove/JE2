package JE.Window;

import org.joml.Vector2i;

public class WindowPreferences {
    public Vector2i windowSize = new Vector2i(1280, 720);
    public String windowTitle = "JE2";
    public boolean windowResizable = false;
    public boolean vSync = true;

    public WindowPreferences(){}

    public WindowPreferences(Vector2i windowSize, String windowTitle, boolean windowResizable, boolean vSync) {
        if(windowSize.x <100 || windowSize.y < 100){
            windowSize.x = 100;
            windowSize.y = 100;
        }
        this.windowSize = windowSize;
        this.windowTitle = windowTitle;
        this.windowResizable = windowResizable;
        this.vSync = vSync;
    }
}
