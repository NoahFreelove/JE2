package org.JE.JE2.Scene.SceneLoading;

import org.JE.JE2.IO.UserInput.Keyboard.Keyboard;
import org.JE.JE2.IO.UserInput.Mouse.Mouse;
import org.JE.JE2.Manager;
import org.JE.JE2.Scene.Scene;
import org.JE.JE2.UI.UIElements.Label;
import org.JE.JE2.UI.UIElements.Style.Color;
import org.JE.JE2.UI.UIObjects.UIWindow;
import org.joml.Vector2f;

public class StaticLoadSequence extends LoadingSequence {
    public UIWindow screen = new UIWindow("Loading" + Math.random(),0);
    public long forceLoadTimeMs = 0;

    public StaticLoadSequence(SceneLoadType loadType, UIWindow screen) {
        super(loadType);
        this.screen = screen;
    }

    public StaticLoadSequence(SceneLoadType loadType){
        super(loadType);
        screen.setPos(new Vector2f(0,0));
        screen.setSize(new Vector2f(Manager.getWindowSize().x(), Manager.getWindowSize().y()));
        //screen.setBackgroundColor(Color.BLACK);
        screen.addElement(new Label("Loading..."));
    }

    private StaticLoadSequence(){
        super(new SceneLoadType());
    }

    @Override
    public void initiate() {
        loadedScene = new Scene();
        Manager.activeScene().getWorld().UI.add(0,screen);
        Keyboard.disableGameInput = true;
        Mouse.disableGameInput = true;
        super.initiate();
    }

    @Override
    public void warmupObjects() {
        loadSceneData();
        next();
    }

    @Override
    public void finalization() {
        Keyboard.disableGameInput = false;
        Mouse.disableGameInput = false;
        next();
    }

    @Override
    public void sceneSwap() {
        if(forceLoadTimeMs>0)
        {
            try {
                Thread.sleep(forceLoadTimeMs);
                Manager.activeScene().getWorld().UI.remove(screen);
                Manager.setScene(loadedScene);
                next();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        else{
            Manager.activeScene().getWorld().UI.remove(screen);
            Manager.setScene(loadedScene);
            next();
        }
    }

    @Override
    public void sceneStart() {
        next();
    }

    @Override
    public void cancel() {
        Manager.activeScene().getWorld().UI.remove(screen);
        Keyboard.disableGameInput = false;
        Mouse.disableGameInput = false;
        super.cancel();
    }
}
