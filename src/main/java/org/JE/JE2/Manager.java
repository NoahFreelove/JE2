package org.JE.JE2;

import org.JE.JE2.Rendering.Camera;
import org.JE.JE2.Scene.SceneLoading.LoadingSequence;
import org.JE.JE2.Scene.Scene;
import org.JE.JE2.UI.UIScaler;
import org.JE.JE2.Utility.Time;
import org.JE.JE2.Window.GLAgent;
import org.JE.JE2.Window.Window;
import org.JE.JE2.Window.WindowCloseReason;
import org.JE.JE2.Window.WindowPreferences;
import org.joml.Vector2i;
import org.joml.Vector4f;

import java.util.ArrayList;

public class Manager {

    private static WindowPreferences preferences = new WindowPreferences();
    private static Scene activeScene;
    private static LoadingSequence activeLoadingSequence;

    public static ArrayList<Scene> buildScenes = new ArrayList<>();

    private static Vector4f defaultViewport = new Vector4f(0,0,1920,1080);
    static{
        activeScene = new Scene(0);
        activeScene.name = "Default Scene";
        buildScenes.add(activeScene);
    }
    public static Camera getMainCamera(){
        return activeScene.mainCamera();
    }
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
        defaultViewport = new Vector4f(0,0,wp.windowSize.x(),wp.windowSize.y());
        Window.createWindow(preferences);
    }

    /**
     * Runs window with settings that should generally work for most screens and apps
     */
    public static void run(){
        start(new WindowPreferences());
    }

    public static void quit(){
        Window.closeWindow(WindowCloseReason.USER_REQUEST);
        System.exit(WindowCloseReason.USER_REQUEST);
    }

    public static void setScene(Scene s){
        activeScene.unload(activeScene, s);
        activeScene = s;
        activeScene.start();
    }

    public static void setScene(Scene s, Runnable postFirstFrame){
        activeScene.unload(activeScene, s);
        activeScene = s;
        s.start();
        Window.newScene = true;
        Window.firstFrameRunnable = postFirstFrame;
    }

    public static void setScene(int i){
        Scene newScene = buildScenes.get(i);
        activeScene.unload(activeScene, newScene);
        activeScene = newScene;
        newScene.start();
    }

    public static void initiateLoadingSequence(LoadingSequence sequence){
        if(activeLoadingSequence !=null)
        {
            activeLoadingSequence.cancel();
        }
        activeLoadingSequence = sequence;
        activeLoadingSequence.initiate();
    }

    public static void loadAndSetNextScene(LoadingSequence sequence){
        initiateLoadingSequence(sequence);
    }

    public static GLAgent queueGLFunction(Runnable r){
        return Window.queueGLFunction(r);
    }
    public static Scene activeScene(){return activeScene;}

    public static Vector2i getWindowSize(){
        return new Vector2i(preferences.windowSize.x(), preferences.windowSize.y());
    }

    public static int getFPS(){
        return (int)(1/ Time.deltaTimeIndependent());
    }

    public static Vector4f defaultViewport(){
        return defaultViewport;
    }

    public static void indexScene(Scene s){
        s.buildIndex = buildScenes.size();
        buildScenes.add(s);
    }

    public static void indexAndSet(Scene s){
        indexScene(s);
        setScene(buildScenes.size()-1);
    }

    public static void onWindowSizeChange(int width, int height){
        preferences.windowSize = new Vector2i(width,height);
        UIScaler.setNewRes(width,height);
        defaultViewport = new Vector4f(0,0,width,height);
    }
}
