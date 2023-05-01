package org.JE.JE2.Window;

import org.JE.JE2.Manager;
import org.JE.JE2.Utility.Watcher;

import static org.lwjgl.opengl.GL11.GL_DEPTH_TEST;
import static org.lwjgl.opengl.GL11.glDisable;

public class DefaultPipeline extends Pipeline{
    @Override
    public void run() {
        runQueuedEvents();
        updateScene();
        renderObjects();
        //postProcess();
        renderUI();
        checkWatchers();
    }

    @Override
    public void renderObjects() {
        Manager.activeScene().world.gameObjects.forEach((gameObject) ->{
            if(gameObject == null)
                return;
            if(!gameObject.active())
                return;

            if(gameObject.getRenderer() != null)
            {
                if(!gameObject.getRenderer().getActive())
                    return;
                gameObject.getRenderer().Render(gameObject,0, Manager.getMainCamera());
            }
        });
    }

    @Override
    public void postProcess() {
        //renderObjects();
        glDisable(GL_DEPTH_TEST);
        Manager.activeScene().world.gameObjects.forEach((gameObject) -> {
            if(gameObject == null)
                return;
            gameObject.scriptPostRender();
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
