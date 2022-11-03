package JE;

import JE.Scene.Scene;
import JE.Window.Window;
import JE.Window.WindowCloseReason;
import JE.Window.WindowPreferences;

public class Manager {

    private static WindowPreferences preferences = new WindowPreferences();
    private static Scene activeScene = new Scene();

    public static void setWindowPreferences(WindowPreferences wp){
        preferences = wp;
        Window.onPreferenceUpdated(preferences);
    }

    /**
     * Creates window with specified settings
     * @param wp WindowPreferences. Width, Height, Title, Resizeable?
     */
    public static void Start(WindowPreferences wp){
        preferences = wp;
        Window.createWindow(preferences);

    }

    /**
     * Runs window with default settings.
     */
    public static void Run(){
        if(!Window.hasInit)
            return;
    }

    public static void Quit(){
        Window.CloseWindow(WindowCloseReason.WINDOW_CLOSE_USER_REQUEST);
        System.exit(WindowCloseReason.WINDOW_CLOSE_USER_REQUEST);
    }

    public static void SetScene(Scene s){
        activeScene = s;
        s.start();
    }

    public static void QueueGLFunction(Runnable r){
        Window.QueueGLFunction(r);
    }


}
