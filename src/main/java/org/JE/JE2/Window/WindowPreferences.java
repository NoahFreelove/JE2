package org.JE.JE2.Window;

import org.joml.Vector2i;

public class WindowPreferences {
    public Vector2i windowSize = new Vector2i(1280, 720);
    public String windowTitle = "JE2";
    public boolean windowResizable = false;
    public boolean vSync = true;
    public boolean initializeNuklear = true;
    public boolean waitForInit = false;
    public WindowPreferences(){}

    public WindowPreferences(Vector2i windowSize, String windowTitle, boolean windowResizable, boolean vSync) {
        this(windowSize.x, windowSize.y, windowTitle, windowResizable, vSync);
    }

    public WindowPreferences(int xSize, int ySize, String windowTitle){
        this(xSize, ySize, windowTitle, false, true);
    }

    public WindowPreferences(int xSize, int ySize, String windowTitle, boolean windowResizable, boolean vSync){
        this(xSize, ySize, windowTitle, windowResizable, vSync, true);
    }

    public WindowPreferences(int xSize, int ySize, String windowTitle, boolean windowResizable, boolean vSync, boolean initializeNuklear){
        if(xSize <100 || ySize < 100){
            xSize = 100;
            ySize = 100;
        }
        this.windowSize = new Vector2i(xSize, ySize);
        this.windowTitle = windowTitle;
        this.windowResizable = windowResizable;
        this.vSync = vSync;
        this.initializeNuklear = initializeNuklear;
    }
}
