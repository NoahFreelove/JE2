package JE;

import JE.Input.KeyPressedEvent;
import JE.Input.KeyReleasedEvent;
import JE.Input.Keyboard;
import JE.Scene.Scene;
import JE.Window.Window;
import JE.Window.WindowCloseReason;
import JE.Window.WindowPreferences;
import org.joml.Vector2i;

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
        Start(new WindowPreferences());
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

    public static Scene getActiveScene(){return activeScene;}

    public static Vector2i getWindowSize(){
        return new Vector2i(preferences.windowSize.x(), preferences.windowSize.y());
    }

    public static void AddKeyPressedCallback(KeyPressedEvent e){
        Keyboard.keyPressedEvents.add(e);
    }

    public static void RemoveKeyPressedCallback(KeyPressedEvent e){
        Keyboard.keyPressedEvents.remove(e);
    }

    public static void AddKeyReleasedCallback(KeyReleasedEvent e){
        Keyboard.keyReleasedEvents.add(e);
    }

    public static void RemoveKeyReleasedCallback(KeyReleasedEvent e){
        Keyboard.keyReleasedEvents.remove(e);
    }
}
