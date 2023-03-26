package org.JE.JE2.Window;

import org.JE.JE2.IO.UserInput.Keyboard.Keyboard;
import org.JE.JE2.IO.UserInput.Mouse.Mouse;
import org.JE.JE2.IO.UserInput.Mouse.MouseButton;
import org.JE.JE2.IO.Logging.Logger;
import org.JE.JE2.Manager;
import org.JE.JE2.UI.UIElements.Style.Color;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.openal.AL;
import org.lwjgl.openal.ALC;
import org.lwjgl.openal.ALCCapabilities;
import org.lwjgl.openal.ALCapabilities;
import org.lwjgl.opengl.*;
import org.lwjgl.system.MemoryStack;

import java.nio.IntBuffer;
import java.util.Objects;
import java.util.concurrent.CopyOnWriteArrayList;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.openal.ALC10.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryStack.stackPush;
import static org.lwjgl.system.MemoryUtil.NULL;

public class Window {
    private static long windowHandle = 0;

    private static long audioDevice =-1;
    private static long audioContext =-1;
    public static boolean hasInit = false;
    public static int width;
    public static int height;
    public static int monitorWidth;
    public static int monitorHeight;
    public static final CopyOnWriteArrayList<Runnable> actionQueue = new CopyOnWriteArrayList<>();
    public static Pipeline pipeline = new DefaultPipeline();
    private static double deltaTime = 0;
    private static int fpsLimit = 60;
    public static boolean queuedScene = false;
    private static boolean waitedFrame = false;

    public static void createWindow(WindowPreferences wp) {
        CreateOpenAL();
        Thread t = new Thread(() -> {
            initializeWindow(wp);
            hasInit = true;
            monitorHeight = glfwGetVideoMode(glfwGetPrimaryMonitor()).height();
            monitorWidth = glfwGetVideoMode(glfwGetPrimaryMonitor()).width();
            loop();
        });
        t.start();

    }
    public static void closeWindow(int code){
        Logger.log("Closing window with code: " + code);
        glfwSetWindowShouldClose(windowHandle, true);
    }

    public static void loop(){
        WindowLoop();
        Destroy();
    }

    private static void Destroy(){
        UIHandler.destroy();

        glfwFreeCallbacks(windowHandle);
        glfwDestroyWindow(windowHandle);
        glfwTerminate();
        Objects.requireNonNull(glfwSetErrorCallback(new GLFWErrorCallback() {
            @Override
            public void invoke(int i, long l) {
                Logger.log("Error: " + i + " " + l);
            }
        })).free();
        System.exit(0);
    }

    private static void initializeWindow(WindowPreferences wp) {
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
                Mouse.mousePressedEvents.forEach(e -> e.invoke(MouseButton.values()[button], mods));
            }
            else if(action == GLFW_RELEASE){
                Mouse.mouseReleasedEvents.forEach(e -> e.invoke(MouseButton.values()[button], mods));
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

        Mouse.mousePressedEvents.add((button, mods) -> Mouse.mousePressed(button.ordinal()));
        Mouse.mouseReleasedEvents.add((button, mods) -> Mouse.mouseReleased(button.ordinal()));

        GLCapabilities caps = GL.createCapabilities();

        GL.setCapabilities(caps);
        GLUtil.setupDebugMessageCallback();

        if (caps.OpenGL43) {
            GL43.glDebugMessageControl(GL43.GL_DEBUG_SOURCE_API, GL43.GL_DEBUG_TYPE_OTHER, GL43.GL_DEBUG_SEVERITY_NOTIFICATION, (IntBuffer)null, false);
        } else if (caps.GL_KHR_debug) {
            KHRDebug.glDebugMessageControl(
                    KHRDebug.GL_DEBUG_SOURCE_API,
                    KHRDebug.GL_DEBUG_TYPE_OTHER,
                    KHRDebug.GL_DEBUG_SEVERITY_NOTIFICATION,
                    (IntBuffer)null,
                    false
            );
        }
        UIHandler.init();


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

    }

    private static void WindowLoop() {

        glClearColor(0.0f, 0.0f, 0.0f, 1.0f);

        glEnable(GL_TEXTURE_2D);
        glEnable(GL_DEPTH_TEST);
        glEnable(GL_BLEND);
        glEnable(GL_COLOR_MATERIAL);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        glDepthFunc(GL_LEQUAL);

        //Logger.stackTrace = true;
        while ( !glfwWindowShouldClose(windowHandle) ) {

            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); // clear the framebuffer

            UIHandler.frameStart();
            double startTime = glfwGetTime();

            try (MemoryStack stack = stackPush()) {
                IntBuffer width  = stack.mallocInt(1);
                IntBuffer height = stack.mallocInt(1);

                glfwGetWindowSize(windowHandle, width, height);
                glViewport(0, 0, width.get(0), height.get(0));
                Color clear = Manager.activeScene().mainCamera().backgroundColor;
                glClearColor(clear.r(), clear.g(), clear.b(), clear.a());
            }

            glfwPollEvents();
            pipeline.onStart();

            glfwSwapBuffers(windowHandle);

            deltaTime = (glfwGetTime() - startTime);

            // If the window was moved or resized, the delta time will be very large.
            // This is to prevent that and physics from breaking.
            // This is a very temporary fix.
            if(deltaTime()>= 0.1){
                deltaTime = 0;
            }
            if(queuedScene){
                if(waitedFrame)
                {
                    Manager.setQueuedScene();
                    queuedScene = false;
                    waitedFrame = false;
                }
                else {
                    waitedFrame = true;
                }
            }
        }
    }

    public static void onPreferenceUpdated(WindowPreferences wp){
        actionQueue.add(() -> {
            glfwSetWindowSize(windowHandle, wp.windowSize.x(), wp.windowSize.y());
            glfwSetWindowTitle(windowHandle, wp.windowTitle);
            glfwSetWindowAttrib(windowHandle, GLFW_RESIZABLE, (wp.windowResizable? 1:0));
            glfwSwapInterval((wp.vSync? 1:0));
            width = wp.windowSize.x();
            height = wp.windowSize.y();
        });
    }

    /**
     * Equivalent to the JavaFX Platform.runLater() method. But way cooler.
     * @param r The runnable to be executed on the main thread.
     */
    public static void queueGLFunction(Runnable r) {
        actionQueue.add(r);
    }

    public static long getWindowHandle(){
        return windowHandle;
    }
    public static float deltaTime(){
        return (float)deltaTime;
    }
    public static long handle()
    {
        return windowHandle;
    }
    public static long audioContext(){
        return audioContext;
    }
}
