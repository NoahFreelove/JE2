package org.JE;

import org.JE.Window.Window;
import org.JE.Window.WindowCloseReason;
import org.JE.Window.WindowPreferences;

public class Manager {

    private static WindowPreferences preferences = new WindowPreferences();

    public static void setWindowPreferences(WindowPreferences wp){
        preferences = wp;
        Window.onPreferenceUpdated(preferences);
    }

    /**
     * Creates window with specified settings
     * @param wp WindowPreferences. Width, Height, Title, Resizeable?
     */
    public static void Run(WindowPreferences wp){
        preferences = wp;
        Window.createWindow(preferences);
    }

    /**
     * Runs window with default settings.
     */
    public static void Start(){
        Run(preferences);
    }

    public static void Quit(){
        Window.CloseWindow(WindowCloseReason.WINDOW_CLOSE_USER_REQUEST);
        System.exit(WindowCloseReason.WINDOW_CLOSE_USER_REQUEST);
    }
}
