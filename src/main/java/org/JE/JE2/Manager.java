package org.JE.JE2;

import org.JE.JE2.IO.UserInput.Keyboard.KeyPressedEvent;
import org.JE.JE2.IO.UserInput.Keyboard.KeyReleasedEvent;
import org.JE.JE2.IO.UserInput.Keyboard.Keyboard;
import org.JE.JE2.Rendering.Camera;
import org.JE.JE2.Scene.Scene;
import org.JE.JE2.Window.Window;
import org.JE.JE2.Window.WindowCloseReason;
import org.JE.JE2.Window.WindowPreferences;
import org.joml.Vector2i;
import org.joml.Vector4i;

import java.util.ArrayList;

public class Manager {

    private static WindowPreferences preferences = new WindowPreferences();
    private static Scene activeScene = new Scene();
    public static ArrayList<Scene> buildScenes = new ArrayList<>();

    public static Camera getMainCamera(){
        return activeScene.mainCamera();
    }
    private static Scene queuedScene;
    public static void setWindowPreferences(WindowPreferences wp){
        preferences = wp;
        Window.onPreferenceUpdated(preferences);
    }

    /**
     * Creates window with specified settings
     * @param wp WindowPreferences. Width, Height, Title, Resizeable?
     */
    public static void start(WindowPreferences wp){
        preferences = wp;
        Window.createWindow(preferences);
    }

    /**
     * Runs window with "default" settings.
     */
    public static void run(){
        start(new WindowPreferences());
    }

    public static void quit(){
        Window.closeWindow(WindowCloseReason.WINDOW_CLOSE_USER_REQUEST);
        System.exit(WindowCloseReason.WINDOW_CLOSE_USER_REQUEST);
    }

    public static void setScene(Scene s){
        activeScene.unload(activeScene, s);
        activeScene = s;
        s.start();
    }

    public static void setScene(int i){
        Scene newScene = buildScenes.get(i);
        activeScene.unload(activeScene, newScene);
        activeScene = newScene;
        newScene.start();
    }

    public static void setScene(Scene s, boolean waitFrame){
        if(waitFrame)
        {
            queuedScene = s;
            Window.queuedScene = true;
        }
        else {
            setScene(s);
        }

    }
    public static void setQueuedScene(){
        activeScene.unload(activeScene, queuedScene);
        activeScene = queuedScene;
        queuedScene.start();
    }

    public static void queueGLFunction(Runnable r){
        Window.queueGLFunction(r);
    }

    public static Scene activeScene(){return activeScene;}

    public static Vector2i getWindowSize(){
        return new Vector2i(preferences.windowSize.x(), preferences.windowSize.y());
    }

    public static void addKeyPressedCallback(KeyPressedEvent e){
        Keyboard.keyPressedEvents.add(e);
    }

    public static void removeKeyPressedCallback(KeyPressedEvent e){
        Keyboard.keyPressedEvents.remove(e);
    }

    public static void addKeyReleasedCallback(KeyReleasedEvent e){
        Keyboard.keyReleasedEvents.add(e);
    }

    public static void removeKeyReleasedCallback(KeyReleasedEvent e){
        Keyboard.keyReleasedEvents.remove(e);
    }

    public static void setWindowSize(Vector2i size){
        preferences.windowSize = size;
        //defaultViewport = new Vector4i(defaultViewport.x(), defaultViewport.y(), size.x, size.y);
        Window.onPreferenceUpdated(preferences);
    }
    public static float deltaTime(){
        return Window.deltaTime();
    }
    public static int getFPS(){
        return (int)(1/Window.deltaTime());
    }

    public static Vector4i defaultViewport(){
        return new Vector4i(0,0,preferences.windowSize.x(),preferences.windowSize.y());
    }
    public static void addBuildScene(Scene s){
        buildScenes.add(s);
    }
}
