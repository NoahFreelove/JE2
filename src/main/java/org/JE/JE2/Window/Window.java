package org.JE.JE2.Window;

import org.JE.JE2.IO.Logging.Errors.JE2Error;
import org.JE.JE2.IO.Logging.Logger;
import org.JE.JE2.IO.UserInput.Keyboard.Keyboard;
import org.JE.JE2.IO.UserInput.Mouse.Mouse;
import org.JE.JE2.Manager;
import org.JE.JE2.Objects.Scripts.ScreenEffects.PostProcess.PostProcessRegistry;
import org.JE.JE2.Rendering.Framebuffer;
import org.JE.JE2.Rendering.Shaders.ShaderProgram;
import org.JE.JE2.Rendering.Texture;
import org.JE.JE2.UI.UIElements.Style.Color;
import org.JE.JE2.Utility.Time;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWImage;
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

import static org.JE.JE2.Rendering.Shaders.ShaderRegistry.loadDefaultShaders;
import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.openal.ALC10.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL30.*;
import static org.lwjgl.opengl.GL30C.glFramebufferTexture2D;
import static org.lwjgl.system.MemoryStack.stackPush;
import static org.lwjgl.system.MemoryUtil.NULL;

public final class Window {
    private static long windowHandle = -1;
    private static long audioDevice =-1;
    private static long audioContext =-1;
    private static volatile boolean hasInit = false;
    private static boolean doubleRenderPostProcess = true;
    private static int width;
    private static int height;
    private static int monitorWidth;
    private static int monitorHeight;
    public static final CopyOnWriteArrayList<Runnable> actionQueue = new CopyOnWriteArrayList<>();
    public static final CopyOnWriteArrayList<Thread> fileDialogs = new CopyOnWriteArrayList<>();
    private static Thread glThread;
    private static ThreadLocal<Boolean> isGLThread;
    private static Pipeline pipeline = new DefaultPipeline();

    private static double deltaTime = 0;

    // The amount of time between frames before the frame time is capped
    // Delta time will be set to 0 as to not break physics.
    // This is put in place for when you move the window and delta times increase
    // But no frames are rendered.
    private static double frameTimeMax = 0.08;
    private static long frameCount = 0;

    private static Framebuffer defaultFramebuffer;
    private static final ShaderProgram defaultPostProcessShader;

    static {
        defaultPostProcessShader = new ShaderProgram(PostProcessRegistry.defaultShaderModule);
    }

    public static void createWindow(WindowPreferences wp) {
        CreateOpenAL();
        isGLThread = new ThreadLocal<>();
        isGLThread.set(false);

        glThread = new Thread(() -> {
            isGLThread.set(true);
            initializeWindow(wp);
            if (videoModeErrorCheck()) return;
            hasInit = true;

            loop();
        });
        glThread.start();

    }

    private static boolean videoModeErrorCheck() {
        GLFWVidMode vidMode = glfwGetVideoMode(glfwGetPrimaryMonitor());
        if(vidMode == null){
            Logger.log(new JE2Error("GLFW Video Mode is null, closing program.", Logger.MAX));
            Window.closeWindow(WindowCloseReason.ERROR);
            return true;
        }
        monitorHeight = vidMode.height();
        monitorWidth = vidMode.width();
        return false;
    }

    public static void closeWindow(int code){
        Logger.log("Quitting with code: " + code + "...");
        glfwSetWindowShouldClose(windowHandle, true);
    }

    public static void loop(){
        initFBO();
        loadDefaultShaders();
        gameLoop();
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
        {
            Logger.log(new JE2Error("Could not initialize GLFW, closing program.", Logger.MAX));
            Window.closeWindow(1);
            return;
        }

        // Configure GLFW
        glfwDefaultWindowHints(); // optional, the current windowHandle hints are already the default
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE); // the windowHandle will stay hidden after creation
        glfwWindowHint(GLFW_RESIZABLE, (wp.windowResizable? 1:0)); // the windowHandle will be resizable

        // Create the windowHandle
        windowHandle = glfwCreateWindow(wp.windowSize.x(), wp.windowSize.y(), wp.windowTitle, NULL, NULL);
        width = wp.windowSize.x();
        height = wp.windowSize.y();
        if ( windowHandle == NULL )
        {
            Logger.log(new JE2Error("Failed to create the GLFW windowHandle.", Logger.MAX));
            Window.closeWindow(1);
        }

        glfwSetWindowSizeCallback(windowHandle, (windowHandle, width, height) -> {
            Window.width = width;
            Window.height = height;
            Manager.onWindowSizeChange(width,height);
        });

        Keyboard.setupKeyboard();
        Mouse.setupMouse();

        // Get the thread stack and push a new frame
        try ( MemoryStack stack = stackPush() ) {
            IntBuffer pWidth = stack.mallocInt(1); // int*
            IntBuffer pHeight = stack.mallocInt(1); // int*

            // Get the windowHandle size passed to glfwCreateWindow
            glfwGetWindowSize(windowHandle, pWidth, pHeight);

            // Get the resolution of the primary monitor
            GLFWVidMode videoMode = glfwGetVideoMode(glfwGetPrimaryMonitor());
            if(videoMode != null){
                // Center the windowHandle
                glfwSetWindowPos(
                        windowHandle,
                        (videoMode.width() - pWidth.get(0)) / 2,
                        (videoMode.height() - pHeight.get(0)) / 2
                );
            }
            else{
                Logger.log(new JE2Error("Failed to retrieve primary monitor video mode.", Logger.MAX));
            }
        } // the stack frame is popped automatically

        glfwMakeContextCurrent(windowHandle); //make active glfw context
        glfwSwapInterval((wp.vSync? 1:0)); //vsync
        glfwShowWindow(windowHandle); //make window visible

        createCapabilities();
        if(wp.initializeNuklear)
            UIHandler.init();
        else
            Logger.log("Warning: Nuklear UI not initialized.", Logger.WARN);
    }

    private static void createCapabilities() {
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

    private static void gameLoop() {

        glClearColor(0.0f, 0.0f, 0.0f, 1.0f);

        glEnable(GL_TEXTURE_2D);
        glEnable(GL_BLEND);
        glEnable(GL_COLOR_MATERIAL);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        glDepthFunc(GL_LEQUAL);

        pipeline.init();
        //Logger.stackTrace = true;
        while ( !glfwWindowShouldClose(windowHandle) ) {
            frameCount++;
            double startTime = System.currentTimeMillis();

            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); // clear the framebuffer

            UIHandler.frameStart();
            Color clear = Manager.activeScene().mainCamera().backgroundColor;

            try (MemoryStack stack = stackPush()) {
                IntBuffer width  = stack.mallocInt(1);
                IntBuffer height = stack.mallocInt(1);

                glfwGetWindowSize(windowHandle, width, height);
                glViewport(0, 0, width.get(0), height.get(0));
                glClearColor(clear.r(), clear.g(), clear.b(), clear.a());
            }

            glfwPollEvents();

            Mouse.frameReset();
            Keyboard.frameReset();
            pipeline.onStart();

            glfwSwapBuffers(windowHandle);
            deltaTime = (System.currentTimeMillis() - startTime)/1000.0;


            // If the window was moved or resized, the delta time will be very large.
            // This is to prevent that and physics from breaking.
            // This is a very temporary fix. Haha "temporary".
            if(deltaTime >= frameTimeMax){
                deltaTime = 0;
                Keyboard.reset();
                Mouse.reset();
            }

            Time.setDeltaTime((float)deltaTime);
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



    public static long handle()
    {
        return windowHandle;
    }

    public static long audioContext(){
        return audioContext;
    }

    public static void setPipeline(Pipeline pipeline) {
        Window.pipeline = pipeline;
    }

    public static Pipeline getPipeline(){
        return pipeline;
    }

    public static boolean getHasInit() {
        return hasInit;
    }

    public static int getWidth() {
        return width;
    }

    public static void setWidth(int width) {
        Window.width = width;
    }

    public static int getHeight() {
        return height;
    }

    public static void setHeight(int height) {
        Window.height = height;
    }

    public static int getMonitorWidth() {
        return monitorWidth;
    }

    public static void setMonitorWidth(int monitorWidth) {
        Window.monitorWidth = monitorWidth;
    }

    public static int getMonitorHeight() {
        return monitorHeight;
    }

    public static void setMonitorHeight(int monitorHeight) {
        Window.monitorHeight = monitorHeight;
    }

    public static boolean isGLThread(){
        return isGLThread.get();
    }

    public static double getFrameTimeMax() {
        return frameTimeMax;
    }

    /**
     * If delta-time is greater than this value, it will be reset to 0 so physics and input don't break.
     * This is a protection set in place for when the window is moved or resized.
     * @param frameTimeMax The maximum delta time allowed. For V-Sync 60fps I would recommend 0.05 at most or 0.02.
     */
    public static void setFrameTimeMax(double frameTimeMax) {
        Window.frameTimeMax = frameTimeMax;
    }

    public static void setWindowIcon(Texture texture){
        actionQueue.add(() -> {
            GLFWImage image = GLFWImage.malloc();
            image.set(texture.resource.getBundle().getImageSize().x, texture.resource.getBundle().getImageSize().y, texture.resource.getBundle().getImageData());
            GLFWImage.Buffer imageBuffer = GLFWImage.malloc(1);
            imageBuffer.put(0, image);
            glfwSetWindowIcon(windowHandle, imageBuffer);
            imageBuffer.free();
            image.free();
        });
    }

    private static void initFBO() {
        defaultFramebuffer = new Framebuffer(width,height);
        defaultFramebuffer.create();
    }

    public static int getFramebufferTexture(){return defaultFramebuffer.getTexture();}
    public static int getFramebuffer(){return defaultFramebuffer.getFramebuffer();}

    public static boolean doubleRenderPostProcess() {
        return doubleRenderPostProcess;
    }

    public static void setDoubleRenderPostProcess(boolean doubleRenderPostProcess) {
        Window.doubleRenderPostProcess = doubleRenderPostProcess;
    }

    public static ShaderProgram getDefaultPostProcessShader() {
        return defaultPostProcessShader;
    }

    public static Framebuffer getDefaultFramebuffer(){return defaultFramebuffer;}

    public static long getFrameCount() {
        return frameCount;
    }
}
