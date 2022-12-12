package JE.Window;

import JE.Manager;
import JE.Objects.Base.GameObject;
import JE.Objects.Gizmos.Gizmo;
import JE.Utility.Watcher;

import static org.lwjgl.glfw.GLFW.glfwPollEvents;
import static org.lwjgl.glfw.GLFW.glfwSwapBuffers;
import static org.lwjgl.opengl.GL11.*;

public class DefaultPipeline extends Pipeline{
    @Override
    public void run() {
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); // clear the framebuffer
        runQueuedEvents();
        updateScene();
        renderObjects();
        renderGUI();
        checkWatchers();
        pollEvents();
        glfwSwapBuffers(Window.getWindowHandle());
    }

    @Override
    public void renderObjects() {
        GameObject[] gameObjects = Manager.activeScene().world.gameObjects.toArray(new GameObject[0]);
        for (GameObject gameObject : gameObjects) {
            if(gameObject == null)
                return;
            gameObject.preRender();

            if(gameObject.renderer != null)
            {
                gameObject.renderer.Render(gameObject.getTransform(),0, Manager.getCamera());
            }
        }
    }

    @Override
    public void renderGUI() {
        for (Gizmo gizmo : Manager.activeScene().world.gizmos) {
            if (gizmo == null)
                continue;
            if (gizmo.renderer != null) {
                gizmo.onDraw();
                gizmo.renderer.Render(gizmo.getTransform());
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
