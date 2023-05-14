package org.JE.JE2.Window;

import org.JE.JE2.IO.UserInput.Keyboard.Keyboard;
import org.JE.JE2.IO.UserInput.Mouse.Mouse;
import org.JE.JE2.IO.UserInput.Mouse.MouseButton;
import org.JE.JE2.IO.Logging.Logger;
import org.JE.JE2.Manager;
import org.JE.JE2.UI.UIElements.Style.Color;
import org.lwjgl.BufferUtils;
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
import static org.lwjgl.opengl.GL30.*;
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
    public static final CopyOnWriteArrayList<Thread> fileDialogs = new CopyOnWriteArrayList<>();
    public static Pipeline pipeline = new DefaultPipeline();
    private static double deltaTime = 0;

    public static int framebuffer;
    public static int textureColorBuffer;
    public static int rbo;

    public static ThreadLocal<Boolean> glThread;

    public static void createWindow(WindowPreferences wp) {
        CreateOpenAL();
        glThread = new ThreadLocal<>();
        glThread.set(false);
        new Thread(() -> {
            glThread.set(true);
            initializeWindow(wp);
            hasInit = true;
            monitorHeight = glfwGetVideoMode(glfwGetPrimaryMonitor()).height();
            monitorWidth = glfwGetVideoMode(glfwGetPrimaryMonitor()).width();
            loop();
        }).start();
    }
    public static void closeWindow(int code){
        Logger.log("Closing window with code: " + code);
        glfwSetWindowShouldClose(windowHandle, true);
    }

    public static void loop(){
        windowLoop();
        destroy();
    }

    private static void destroy(){
        UIHandler.destroy();

        fileDialogs.forEach(Thread::interrupt);
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
        width = wp.windowSize.x();
        height = wp.windowSize.y();
        if ( windowHandle == NULL )
            throw new RuntimeException("Failed to create the GLFW windowHandle");

        // Setup a key callback. It will be called every time a key is pressed, repeated or released.
        glfwSetKeyCallback(windowHandle, (windowHandle, key, scancode, action, mods) -> {

            if ( key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE )
                glfwSetWindowShouldClose(windowHandle, true);

            if(action == GLFW_PRESS){
                Keyboard.triggerKeyPressed(key, mods);
            }
            else if(action == GLFW_RELEASE){
                Keyboard.triggerKeyReleased(key, mods);
            }
            else if (action == GLFW_REPEAT){
                Keyboard.triggerKeyRepeat(key, mods);
            }
        });

        glfwSetWindowSizeCallback(windowHandle, (windowHandle, width, height) -> {
            Window.width = width;
            Window.height = height;
            Manager.onWindowSizeChange(width,height);
        });



        glfwSetMouseButtonCallback(windowHandle, (windowHandle, button, action, mods) -> {
            if(action == GLFW_PRESS){
                Mouse.triggerMousePressed(MouseButton.values()[button], mods);
            }
            else if(action == GLFW_RELEASE){
                Mouse.triggerMouseReleased(MouseButton.values()[button], mods);
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
        if(wp.initializeNuklear)
            UIHandler.init();

        generateFrameBuffer();
    }

    private static void generateFrameBuffer() {
        IntBuffer result = BufferUtils.createIntBuffer(1);
        glGenFramebuffers(result);
        framebuffer = result.get();
        glBindFramebuffer(GL_FRAMEBUFFER, framebuffer);

        textureColorBuffer =  glGenTextures();
        glBindTexture(GL_TEXTURE_2D, textureColorBuffer);
        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGB, width, height, 0, GL_RGB, GL_UNSIGNED_BYTE, NULL);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
        glFramebufferTexture2D(GL_FRAMEBUFFER, GL_COLOR_ATTACHMENT0, GL_TEXTURE_2D, textureColorBuffer, 0);

        rbo = glGenRenderbuffers();
        glBindRenderbuffer(GL_RENDERBUFFER, rbo);
        glRenderbufferStorage(GL_RENDERBUFFER, GL_DEPTH24_STENCIL8, width, height); // use a single renderbuffer object for both a depth AND stencil buffer.
        glFramebufferRenderbuffer(GL_FRAMEBUFFER, GL_DEPTH_STENCIL_ATTACHMENT, GL_RENDERBUFFER, rbo); // now actually attach it
        // now that we actually created the framebuffer and added all attachments we want to check if it is actually complete now

        if (glCheckFramebufferStatus(GL_FRAMEBUFFER) != GL_FRAMEBUFFER_COMPLETE)
            System.out.println("ERROR::FRAMEBUFFER:: Framebuffer is not complete!");

        glBindFramebuffer(GL_FRAMEBUFFER, 0);

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

    private static void windowLoop() {

        glClearColor(0.0f, 0.0f, 0.0f, 1.0f);

        glEnable(GL_TEXTURE_2D);
        glEnable(GL_BLEND);
        glEnable(GL_COLOR_MATERIAL);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        glDepthFunc(GL_LEQUAL);

        //Logger.stackTrace = true;
        while ( !glfwWindowShouldClose(windowHandle) ) {

            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); // clear the framebuffer
            //glBindFramebuffer(GL_FRAMEBUFFER, framebuffer);

            UIHandler.frameStart();
            double startTime = glfwGetTime();
            Color clear = Manager.activeScene().mainCamera().backgroundColor;

            try (MemoryStack stack = stackPush()) {
                IntBuffer width  = stack.mallocInt(1);
                IntBuffer height = stack.mallocInt(1);

                glfwGetWindowSize(windowHandle, width, height);
                glViewport(0, 0, width.get(0), height.get(0));
                glClearColor(clear.r(), clear.g(), clear.b(), clear.a());
            }

            glfwPollEvents();
            pipeline.onStart();

            //glBindFramebuffer(GL_FRAMEBUFFER, 0);

           /* glClearColor(clear.r(), clear.g(), clear.b(), clear.a());
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);*/

            //pipeline.postProcess();

            glfwSwapBuffers(windowHandle);
            deltaTime = (glfwGetTime() - startTime);

            // If the window was moved or resized, the delta time will be very large.
            // This is to prevent that and physics from breaking.
            // This is a very temporary fix.
            if(deltaTime()>= 0.1){
                deltaTime = 0;
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
            Manager.onWindowSizeChange(width,height);
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
