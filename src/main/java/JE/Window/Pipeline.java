package JE.Window;

import JE.Annotations.GLThread;

import static org.lwjgl.glfw.GLFW.glfwGetTime;

public abstract class Pipeline {

    public float deltaTime = 1f;
    double startTime = 1f;

    @GLThread
    public abstract void run(); // will be called every frame

    @GLThread
    public void onStart(){
        startTime = glfwGetTime();
        run();
        onEnd();
    }

    @GLThread
    public void onEnd(){
        double endTime = glfwGetTime();
        deltaTime = (float)(endTime - startTime);
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
    public abstract void calculateAudio(); // play scene audio

}
