package JE.Window;

import JE.Manager;
import JE.Utility.Watcher;

import static org.lwjgl.glfw.GLFW.glfwPollEvents;

public class DefaultPipeline extends Pipeline{
    @Override
    public void run() {
        runQueuedEvents();
        updateScene();
        renderObjects();
        renderUI();
        checkWatchers();
        pollEvents();
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
        glfwPollEvents();
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
