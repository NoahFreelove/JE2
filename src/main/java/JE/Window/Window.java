package JE.Window;

import JE.Input.Keyboard;
import JE.Input.Mouse;
import JE.Logging.Logger;
import JE.Manager;
import JE.Objects.Base.GameObject;
import JE.Objects.Gizmos.Gizmo;
import org.lwjgl.glfw.GLFWErrorCallback;

import org.lwjgl.glfw.*;
import org.lwjgl.openal.AL;
import org.lwjgl.openal.ALC;
import org.lwjgl.openal.ALCCapabilities;
import org.lwjgl.openal.ALCapabilities;
import org.lwjgl.opengl.*;
import org.lwjgl.system.*;

import java.nio.*;
import java.util.ArrayList;
import java.util.Objects;
import java.util.concurrent.CopyOnWriteArrayList;

import static org.lwjgl.glfw.Callbacks.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.openal.AL10.alGenBuffers;
import static org.lwjgl.openal.ALC10.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryStack.*;
import static org.lwjgl.system.MemoryUtil.*;

public class Window {
    private static long windowHandle = 0;
    private static long audioDevice =-1;
    private static long audioContext =-1;
    public static boolean hasInit = false;
    public static final CopyOnWriteArrayList<Runnable> actionQueue = new CopyOnWriteArrayList<>();
    public static Pipeline pipeline = new DefaultPipeline();
    public static int audioBuffer = 0;

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

        glfwSetMouseButtonCallback(windowHandle, (windowHandle, button, action, mods) -> {
            if(action == GLFW_PRESS){
                Mouse.mousePressedEvents.forEach(e -> e.invoke(button, mods));
            }
            else if(action == GLFW_RELEASE){
                Mouse.mouseReleasedEvents.forEach(e -> e.invoke(button, mods));
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

        Keyboard.keyPressedEvents.add((key, mods) -> Keyboard.keyPressed(key));
        Keyboard.keyReleasedEvents.add((key, mods) -> Keyboard.keyReleased(key));

        Mouse.mousePressedEvents.add((button, mods) -> Mouse.mousePressed(button));
        Mouse.mouseReleasedEvents.add((button, mods) -> Mouse.mouseReleased(button));

        GL.setCapabilities(GL.createCapabilities());
        CreateOpenAL();
    }

    public static void CreateOpenAL() {
        String audioDeviceName = alcGetString(0, ALC_DEFAULT_DEVICE_SPECIFIER);
        audioDevice = alcOpenDevice(audioDeviceName);

        if (audioDevice == NULL) {
            Logger.log("Could not initialize OpenAl! There is no audio device.");
        }
        else{
            int[] attributes = {0};
            audioContext = alcCreateContext(audioDevice, attributes);
            alcMakeContextCurrent(audioContext);

            ALCCapabilities alcCapabilities = ALC.createCapabilities(audioDevice);
            ALCapabilities alCapabilities = AL.createCapabilities(alcCapabilities);

            assert alCapabilities.OpenAL10 : "OpenAL 1.0 is not supported";
        }

        audioBuffer = alGenBuffers();
    }

    private static void WindowLoop() {

        glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
        glEnable(GL_TEXTURE_2D);
        glEnable(GL_DEPTH_TEST);
        glEnable(GL_BLEND);
        glEnable(GL_COLOR_MATERIAL);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        glDepthFunc(GL_LEQUAL);

        while ( !glfwWindowShouldClose(windowHandle) ) pipeline.onStart();
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

    public static long getWindowHandle(){
        return windowHandle;
    }
    public static float deltaTime(){
        return pipeline.deltaTime;
    }
    public static long handle()
    {
        return windowHandle;
    }
    public static long audioContext(){
        return audioContext;
    }
}
