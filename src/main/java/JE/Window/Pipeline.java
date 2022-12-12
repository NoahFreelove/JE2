package JE.Window;

import JE.Annotations.GLThread;
import JE.Manager;

import static org.lwjgl.glfw.GLFW.glfwGetTime;

public abstract class Pipeline {

    public float deltaTime = 1f;
    double startTime = 1f;
    int gcThreshold = 1000;

    @GLThread
    protected abstract void run(); // will be called every frame

    @GLThread
    public final void onStart(){
        startTime = glfwGetTime();
        run();
        onEnd();
    }

    @GLThread
    protected void onEnd(){
        double endTime = glfwGetTime();
        deltaTime = (float)(endTime - startTime);

        if(Manager.activeScene().world.gameObjects.size() > gcThreshold)
        {
            System.gc();
        }
    }
    @GLThread
    public abstract void renderObjects(); // GameObjects
    @GLThread
    public abstract void renderGUI(); // UI / Gizmos
    @GLThread
    public abstract void runQueuedEvents(); // GL thread events
    @GLThread
    public abstract void pollEvents(); // check for keyboard inputs, window updates, etc.
    @GLThread
    public abstract void updateScene(); // run GameObject update methods
    @GLThread
    public abstract void checkWatchers(); // run scene's watchers to watch for event changes

}
