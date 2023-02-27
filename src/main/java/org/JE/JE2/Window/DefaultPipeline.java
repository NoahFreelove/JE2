package org.JE.JE2.Window;

import org.JE.JE2.Manager;
import org.JE.JE2.Utility.Watcher;

public class DefaultPipeline extends Pipeline{
    @Override
    public void run() {
        runQueuedEvents();
        updateScene();
        renderObjects();
        renderUI();
        checkWatchers();
    }

    @Override
    public void renderObjects() {
        Manager.activeScene().world.gameObjects.forEach((gameObject) ->{
            if(gameObject == null)
                return;
            gameObject.preRender();

            if(gameObject.getRenderer() != null)
            {
                gameObject.getRenderer().Render(gameObject,0, Manager.getMainCamera());
            }
        });
    }

    @Override
    public void renderUI() {
        UIHandler.renderNuklear();
    }

    @Override
    public void runQueuedEvents() {
        Window.actionQueue.forEach(Runnable::run);
        Window.actionQueue.clear();
    }

    @Override
    public void pollEvents() {
    }

    @Override
    public void updateScene() {
        Manager.activeScene().update();
    }

    @Override
    public void checkWatchers() {
        Manager.activeScene().watchers.forEach(Watcher::invoke);
    }
}
