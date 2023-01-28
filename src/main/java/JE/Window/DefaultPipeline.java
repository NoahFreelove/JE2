package JE.Window;

import JE.Manager;
import JE.Objects.Gizmos.Gizmo;
import JE.Utility.Watcher;

import static org.lwjgl.glfw.GLFW.glfwPollEvents;

public class DefaultPipeline extends Pipeline{
    @Override
    public void run() {
        runQueuedEvents();
        updateScene();
        renderObjects();
        renderGUI();
        checkWatchers();
        pollEvents();
    }

    @Override
    public void renderObjects() {
        Manager.activeScene().world.gameObjects.forEach((gameObject) ->{
            if(gameObject == null)
                return;
            gameObject.preRender();

            if(gameObject.renderer != null)
            {
                gameObject.renderer.Render(gameObject,0, Manager.getCamera());
            }
        });
    }

    @Override
    public void renderGUI() {
        for (Gizmo gizmo : Manager.activeScene().world.gizmos) {
            if (gizmo == null)
                continue;
            if (gizmo.renderer != null) {
                gizmo.onDraw();
                gizmo.renderer.Render(gizmo);
            }
        }
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
