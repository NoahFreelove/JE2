package JE.Window;

import JE.Annotations.GLThread;
import JE.Input.KeyPressedEvent;
import JE.Input.Keyboard;
import JE.Manager;
import JE.Objects.Base.GameObject;
import JE.Scene.World;
import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFWErrorCallback;

import org.lwjgl.glfw.*;
import org.lwjgl.nuklear.NkAllocator;
import org.lwjgl.nuklear.NkContext;
import org.lwjgl.nuklear.Nuklear;
import org.lwjgl.opengl.*;
import org.lwjgl.system.*;

import java.nio.*;
import java.util.ArrayList;
import java.util.Objects;

import static org.lwjgl.glfw.Callbacks.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryStack.*;
import static org.lwjgl.system.MemoryUtil.*;

public class Window {
    private static long windowHandle = 0;
    public static boolean hasInit = false;
    private static final ArrayList<Runnable> actionQueue = new ArrayList<>();


    public static void createWindow(WindowPreferences wp) {
        Thread t = new Thread(() -> {
            InitializeWindow(wp);
            hasInit = true;
            Loop();
        });
        t.start();

    }
    public static void CloseWindow(int code){
        System.out.println("Closing window with code: " + code);
        glfwSetWindowShouldClose(windowHandle, true);
    }

    public static void Loop(){
        WindowLoop();
        glfwFreeCallbacks(windowHandle);
        glfwDestroyWindow(windowHandle);
        glfwTerminate();
        Objects.requireNonNull(glfwSetErrorCallback(new GLFWErrorCallback() {
            @Override
            public void invoke(int i, long l) {
                System.out.println("Error: " + i + " " + l);
            }
        })).free();
    }

    private static void InitializeWindow(WindowPreferences wp) {
        // Setup an error callback. The default implementation
        // will print the error message in System.err.
        GLFWErrorCallback.createPrint(System.err).set();

        // Initialize GLFW. Most GLFW functions will not work before doing this.
        if ( !glfwInit() )
            throw new IllegalStateException("Unable to initialize GLFW");

        // Configure GLFW
        glfwDefaultWindowHints(); // optional, the current windowHandle hints are already the default
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE); // the windowHandle will stay hidden after creation
        glfwWindowHint(GLFW_RESIZABLE, (wp.windowResizable? 1:0)); // the windowHandle will be resizable

        // Create the windowHandle
        windowHandle = glfwCreateWindow(wp.windowSize.x(), wp.windowSize.y(), wp.windowTitle, NULL, NULL);
        if ( windowHandle == NULL )
            throw new RuntimeException("Failed to create the GLFW windowHandle");

        // Setup a key callback. It will be called every time a key is pressed, repeated or released.
        glfwSetKeyCallback(windowHandle, (windowHandle, key, scancode, action, mods) -> {
            if ( key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE )
                glfwSetWindowShouldClose(windowHandle, true);

            if(action == GLFW_PRESS){
                Keyboard.keyPressedEvents.forEach(e -> e.invoke(key, mods));
            }
            else if(action == GLFW_RELEASE){
                Keyboard.keyReleasedEvents.forEach(e -> e.invoke(key, mods));
            }
        });


        // Get the thread stack and push a new frame
        try ( MemoryStack stack = stackPush() ) {
            IntBuffer pWidth = stack.mallocInt(1); // int*
            IntBuffer pHeight = stack.mallocInt(1); // int*

            // Get the windowHandle size passed to glfwCreateWindow
            glfwGetWindowSize(windowHandle, pWidth, pHeight);

            // Get the resolution of the primary monitor
            GLFWVidMode videoMode = glfwGetVideoMode(glfwGetPrimaryMonitor());

            // Center the windowHandle
            glfwSetWindowPos(
                    windowHandle,
                    (videoMode.width() - pWidth.get(0)) / 2,
                    (videoMode.height() - pHeight.get(0)) / 2
            );
        } // the stack frame is popped automatically

        // Make the OpenGL context current
        glfwMakeContextCurrent(windowHandle);

        // Enable VSync if requested
        glfwSwapInterval((wp.vSync? 1:0));

        // Make the windowHandle visible
        glfwShowWindow(windowHandle);
    }
    private static void WindowLoop() {

        GL.setCapabilities(GL.createCapabilities());
        glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
        glEnable(GL_TEXTURE_2D);
        glEnable(GL_DEPTH_TEST);

        while ( !glfwWindowShouldClose(windowHandle) ) {

            Object[] actions = actionQueue.toArray();
            for (Object action : actions) {
                ((Runnable) action).run();
            }
            actionQueue.clear();

            Manager.getActiveScene().update();
            render();
            glfwPollEvents();

        }
    }

    private static void render() {
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); // clear the framebuffer

        for (GameObject object: Manager.getActiveScene().world.gameObjects) {
            if(object == null)
                continue;
            if(object.renderer != null)
            {
                object.renderer.Render(object.getTransform());
            }
        }
        glfwSwapBuffers(windowHandle); // swap the color buffers
    }

    public static void onPreferenceUpdated(WindowPreferences wp){
        actionQueue.add(() -> {
            glfwSetWindowSize(windowHandle, wp.windowSize.x(), wp.windowSize.y());
            glfwSetWindowTitle(windowHandle, wp.windowTitle);
            glfwSetWindowAttrib(windowHandle, GLFW_RESIZABLE, (wp.windowResizable? 1:0));
            glfwSwapInterval((wp.vSync? 1:0));
        });
    }

    public static void QueueGLFunction(Runnable r) {
        actionQueue.add(r);
    }
}
