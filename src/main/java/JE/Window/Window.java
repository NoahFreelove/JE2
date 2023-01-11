package JE.Window;

import JE.Input.KeyPressedEvent;
import JE.Input.Keyboard;
import JE.Input.Mouse;
import JE.Input.MousePressedEvent;
import JE.Logging.Logger;
import JE.Manager;
import JE.Objects.Base.GameObject;
import JE.Objects.Gizmos.Gizmo;
import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFWErrorCallback;

import org.lwjgl.glfw.*;
import org.lwjgl.nuklear.*;
import org.lwjgl.openal.AL;
import org.lwjgl.openal.ALC;
import org.lwjgl.openal.ALCCapabilities;
import org.lwjgl.openal.ALCapabilities;
import org.lwjgl.opengl.*;
import org.lwjgl.stb.STBTTAlignedQuad;
import org.lwjgl.stb.STBTTFontinfo;
import org.lwjgl.stb.STBTTPackContext;
import org.lwjgl.stb.STBTTPackedchar;
import org.lwjgl.system.*;

import static org.lwjgl.nuklear.NkConvertConfig.VERTEX_LAYOUT;
import static org.lwjgl.nuklear.Nuklear.*;

import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.*;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Objects;
import java.util.concurrent.CopyOnWriteArrayList;

import static org.lwjgl.glfw.Callbacks.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.openal.AL10.alGenBuffers;
import static org.lwjgl.openal.ALC10.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;
import static org.lwjgl.stb.STBTruetype.*;
import static org.lwjgl.system.MemoryStack.*;
import static org.lwjgl.system.MemoryUtil.*;

public class Window {
    private static long windowHandle = 0;
    private static ByteBuffer ttf; // Storage for font data
    private static NkContext ctx = NkContext.create(); // Create a Nuklear context, it is used everywhere.
    private static NkUserFont default_font = NkUserFont.create(); // This is the Nuklear font object used for rendering text.

    private static NkBuffer cmds = NkBuffer.create(); // Stores a list of drawing commands that will be passed to OpenGL to render the interface.
    private static NkDrawNullTexture null_texture = NkDrawNullTexture.create(); // An empty texture used for drawing.

    private static int vbo, vao, ebo;
    private static int prog;
    private static int vert_shdr;
    private static int frag_shdr;
    private static int uniform_tex;
    private static int uniform_proj;
    static NkAllocator allocator;
    private static long audioDevice =-1;
    private static long audioContext =-1;
    public static boolean hasInit = false;
    private static int width;
    private static int height;
    private static int monitorWidth;
    private static int monitorHeight;
    public static final CopyOnWriteArrayList<Runnable> actionQueue = new CopyOnWriteArrayList<>();
    public static Pipeline pipeline = new DefaultPipeline();
    public static int audioBuffer = 0;

    public static void createWindow(WindowPreferences wp) {
        Thread t = new Thread(() -> {
            InitializeWindow(wp);
            hasInit = true;
            monitorHeight = glfwGetVideoMode(glfwGetPrimaryMonitor()).height();
            monitorWidth = glfwGetVideoMode(glfwGetPrimaryMonitor()).width();
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

        GLCapabilities caps = GL.createCapabilities();

        GL.setCapabilities(caps);
        Callback debugProc = GLUtil.setupDebugMessageCallback();

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

        NkContext ctx = setupWindow(handle());
        createFont();


        CreateOpenAL();
    }

    private static NkContext setupWindow(long win) {
        glfwSetScrollCallback(win, (window, xoffset, yoffset) -> {
            try (MemoryStack stack = stackPush()) {
                NkVec2 scroll = NkVec2.mallocStack(stack)
                        .x((float)xoffset)
                        .y((float)yoffset);
                nk_input_scroll(ctx, scroll);
            }
        });
        glfwSetCharCallback(win, (window, codepoint) -> nk_input_unicode(ctx, codepoint));

        Keyboard.keyPressedEvents.add((key, mods) -> {
            boolean press = true;
            switch (key) {
                case GLFW_KEY_ESCAPE -> glfwSetWindowShouldClose(handle(), true);
                case GLFW_KEY_DELETE -> nk_input_key(ctx, NK_KEY_DEL, press);
                case GLFW_KEY_ENTER -> nk_input_key(ctx, NK_KEY_ENTER, press);
                case GLFW_KEY_TAB -> nk_input_key(ctx, NK_KEY_TAB, press);
                case GLFW_KEY_BACKSPACE -> nk_input_key(ctx, NK_KEY_BACKSPACE, press);
                case GLFW_KEY_UP -> nk_input_key(ctx, NK_KEY_UP, press);
                case GLFW_KEY_DOWN -> nk_input_key(ctx, NK_KEY_DOWN, press);
                case GLFW_KEY_HOME -> {
                    nk_input_key(ctx, NK_KEY_TEXT_START, press);
                    nk_input_key(ctx, NK_KEY_SCROLL_START, press);
                }
                case GLFW_KEY_END -> {
                    nk_input_key(ctx, NK_KEY_TEXT_END, press);
                    nk_input_key(ctx, NK_KEY_SCROLL_END, press);
                }
                case GLFW_KEY_PAGE_DOWN -> nk_input_key(ctx, NK_KEY_SCROLL_DOWN, press);
                case GLFW_KEY_PAGE_UP -> nk_input_key(ctx, NK_KEY_SCROLL_UP, press);
                case GLFW_KEY_LEFT_SHIFT, GLFW_KEY_RIGHT_SHIFT -> nk_input_key(ctx, NK_KEY_SHIFT, press);
                case GLFW_KEY_LEFT_CONTROL, GLFW_KEY_RIGHT_CONTROL -> {
                    if (press) {
                        nk_input_key(ctx, NK_KEY_COPY, glfwGetKey(handle(), GLFW_KEY_C) == GLFW_PRESS);
                        nk_input_key(ctx, NK_KEY_PASTE, glfwGetKey(handle(), GLFW_KEY_P) == GLFW_PRESS);
                        nk_input_key(ctx, NK_KEY_CUT, glfwGetKey(handle(), GLFW_KEY_X) == GLFW_PRESS);
                        nk_input_key(ctx, NK_KEY_TEXT_UNDO, glfwGetKey(handle(), GLFW_KEY_Z) == GLFW_PRESS);
                        nk_input_key(ctx, NK_KEY_TEXT_REDO, glfwGetKey(handle(), GLFW_KEY_R) == GLFW_PRESS);
                        nk_input_key(ctx, NK_KEY_TEXT_WORD_LEFT, glfwGetKey(handle(), GLFW_KEY_LEFT) == GLFW_PRESS);
                        nk_input_key(ctx, NK_KEY_TEXT_WORD_RIGHT, glfwGetKey(handle(), GLFW_KEY_RIGHT) == GLFW_PRESS);
                        nk_input_key(ctx, NK_KEY_TEXT_LINE_START, glfwGetKey(handle(), GLFW_KEY_B) == GLFW_PRESS);
                        nk_input_key(ctx, NK_KEY_TEXT_LINE_END, glfwGetKey(handle(), GLFW_KEY_E) == GLFW_PRESS);
                    } else {
                        nk_input_key(ctx, NK_KEY_LEFT, glfwGetKey(handle(), GLFW_KEY_LEFT) == GLFW_PRESS);
                        nk_input_key(ctx, NK_KEY_RIGHT, glfwGetKey(handle(), GLFW_KEY_RIGHT) == GLFW_PRESS);
                        nk_input_key(ctx, NK_KEY_COPY, false);
                        nk_input_key(ctx, NK_KEY_PASTE, false);
                        nk_input_key(ctx, NK_KEY_CUT, false);
                        nk_input_key(ctx, NK_KEY_SHIFT, false);
                    }
                }
            }
        });
        glfwSetCursorPosCallback(win, (window, xpos, ypos) -> nk_input_motion(ctx, (int)xpos, (int)ypos));
        Mouse.mousePressedEvents.add(new MousePressedEvent() {
            @Override
            public void invoke(int button, int mods) {
                try (MemoryStack stack = stackPush()) {
                    DoubleBuffer cx = stack.mallocDouble(1);
                    DoubleBuffer cy = stack.mallocDouble(1);

                    glfwGetCursorPos(handle(), cx, cy);

                    int x = (int)cx.get(0);
                    int y = (int)cy.get(0);

                    int nkButton;
                    switch (button) {
                        case GLFW_MOUSE_BUTTON_RIGHT:
                            nkButton = NK_BUTTON_RIGHT;
                            break;
                        case GLFW_MOUSE_BUTTON_MIDDLE:
                            nkButton = NK_BUTTON_MIDDLE;
                            break;
                        default:
                            nkButton = NK_BUTTON_LEFT;
                    }
                    nk_input_button(ctx, nkButton, x, y, true);
                }
            }
        });
        allocator = NkAllocator.create()
                .alloc((handle, old, size) -> nmemAllocChecked(size))
                .mfree((handle, ptr) -> nmemFree(ptr));

        nk_init(ctx, allocator, null);
        ctx.clip()
                .copy((handle, text, len) -> {
                    if (len == 0) {
                        return;
                    }

                    try (MemoryStack stack = stackPush()) {
                        ByteBuffer str = stack.malloc(len + 1);
                        memCopy(text, memAddress(str), len);
                        str.put(len, (byte)0);

                        glfwSetClipboardString(win, str);
                    }
                })
                .paste((handle, edit) -> {
                    long text = nglfwGetClipboardString(win);
                    if (text != NULL) {
                        nnk_textedit_paste(edit, text, nnk_strlen(text));
                    }
                });

        //setupContext();
        return ctx;
    }

    private void setupContext() {
        String NK_SHADER_VERSION = Platform.get() == Platform.MACOSX ? "#version 150\n" : "#version 300 es\n";
        String vertex_shader =
                NK_SHADER_VERSION +
                        "uniform mat4 ProjMtx;\n" +
                        "in vec2 Position;\n" +
                        "in vec2 TexCoord;\n" +
                        "in vec4 Color;\n" +
                        "out vec2 Frag_UV;\n" +
                        "out vec4 Frag_Color;\n" +
                        "void main() {\n" +
                        "   Frag_UV = TexCoord;\n" +
                        "   Frag_Color = Color;\n" +
                        "   gl_Position = ProjMtx * vec4(Position.xy, 0, 1);\n" +
                        "}\n";
        String fragment_shader =
                NK_SHADER_VERSION +
                        "precision mediump float;\n" +
                        "uniform sampler2D Texture;\n" +
                        "in vec2 Frag_UV;\n" +
                        "in vec4 Frag_Color;\n" +
                        "out vec4 Out_Color;\n" +
                        "void main(){\n" +
                        "   Out_Color = Frag_Color * texture(Texture, Frag_UV.st);\n" +
                        "}\n";
        var BUFFER_INITIAL_SIZE = 4 * 1024;
        nk_buffer_init(cmds, allocator, BUFFER_INITIAL_SIZE);
        prog = glCreateProgram();
        vert_shdr = glCreateShader(GL_VERTEX_SHADER);
        frag_shdr = glCreateShader(GL_FRAGMENT_SHADER);
        glShaderSource(vert_shdr, vertex_shader);
        glShaderSource(frag_shdr, fragment_shader);
        glCompileShader(vert_shdr);
        glCompileShader(frag_shdr);
        if (glGetShaderi(vert_shdr, GL_COMPILE_STATUS) != GL_TRUE) {
            throw new IllegalStateException();
        }
        if (glGetShaderi(frag_shdr, GL_COMPILE_STATUS) != GL_TRUE) {
            throw new IllegalStateException();
        }
        glAttachShader(prog, vert_shdr);
        glAttachShader(prog, frag_shdr);
        glLinkProgram(prog);
        if (glGetProgrami(prog, GL_LINK_STATUS) != GL_TRUE) {
            throw new IllegalStateException();
        }

        uniform_tex = glGetUniformLocation(prog, "Texture");
        uniform_proj = glGetUniformLocation(prog, "ProjMtx");
        int attrib_pos = glGetAttribLocation(prog, "Position");
        int attrib_uv  = glGetAttribLocation(prog, "TexCoord");
        int attrib_col = glGetAttribLocation(prog, "Color");

        {
            // buffer setup
            vbo = glGenBuffers();
            ebo = glGenBuffers();
            vao = glGenVertexArrays();

            glBindVertexArray(vao);
            glBindBuffer(GL_ARRAY_BUFFER, vbo);
            glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, ebo);

            glEnableVertexAttribArray(attrib_pos);
            glEnableVertexAttribArray(attrib_uv);
            glEnableVertexAttribArray(attrib_col);

            glVertexAttribPointer(attrib_pos, 2, GL_FLOAT, false, 20, 0);
            glVertexAttribPointer(attrib_uv, 2, GL_FLOAT, false, 20, 8);
            glVertexAttribPointer(attrib_col, 4, GL_UNSIGNED_BYTE, true, 20, 16);
        }

        {
            // null texture setup
            int nullTexID = glGenTextures();

            null_texture.texture().id(nullTexID);
            null_texture.uv().set(0.5f, 0.5f);

            glBindTexture(GL_TEXTURE_2D, nullTexID);
            try (MemoryStack stack = stackPush()) {
                glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA8, 1, 1, 0, GL_RGBA, GL_UNSIGNED_INT_8_8_8_8_REV, stack.ints(0xFFFFFFFF));
            }
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
        }

        glBindTexture(GL_TEXTURE_2D, 0);
        glBindBuffer(GL_ARRAY_BUFFER, 0);
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
        glBindVertexArray(0);
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
        int MAX_VERTEX_BUFFER = 512 * 1024;
        int MAX_ELEMENT_BUFFER = 128 * 1024;
        while ( !glfwWindowShouldClose(windowHandle) ) {
            pipeline.onStart();

            renderNuklear(NK_ANTI_ALIASING_ON, MAX_VERTEX_BUFFER, MAX_ELEMENT_BUFFER);
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

    private static void createFont(){
        int BITMAP_W = 1024;
        int BITMAP_H = 1024;

        int FONT_HEIGHT = 18;
        int fontTexID   = glGenTextures();

        STBTTFontinfo fontInfo = STBTTFontinfo.create();
        STBTTPackedchar.Buffer cdata    = STBTTPackedchar.create(95);

        float scale;
        float descent;

        try (MemoryStack stack = stackPush()) {

            // use stb to load font to ttf byte buffer
            ttf = BufferUtils.createByteBuffer(1024 * 1024);
            try {
                InputStream is = new FileInputStream("");
                FileChannel fc = ((FileInputStream) is).getChannel();
                fc.read(ttf);
            }
            catch (Exception e){
                System.out.println("Font read error");
            }


            stbtt_InitFont(fontInfo, ttf);
            scale = stbtt_ScaleForPixelHeight(fontInfo, FONT_HEIGHT);

            IntBuffer d = stack.mallocInt(1);
            stbtt_GetFontVMetrics(fontInfo, null, d, null);
            descent = d.get(0) * scale;

            ByteBuffer bitmap = memAlloc(BITMAP_W * BITMAP_H);

            STBTTPackContext pc = STBTTPackContext.mallocStack(stack);
            stbtt_PackBegin(pc, bitmap, BITMAP_W, BITMAP_H, 0, 1, NULL);
            stbtt_PackSetOversampling(pc, 4, 4);
            stbtt_PackFontRange(pc, ttf, 0, FONT_HEIGHT, 32, cdata);
            stbtt_PackEnd(pc);

            // Convert R8 to RGBA8
            ByteBuffer texture = memAlloc(BITMAP_W * BITMAP_H * 4);
            for (int i = 0; i < bitmap.capacity(); i++) {
                texture.putInt((bitmap.get(i) << 24) | 0x00FFFFFF);
            }
            texture.flip();

            glBindTexture(GL_TEXTURE_2D, fontTexID);
            glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA8, BITMAP_W, BITMAP_H, 0, GL_RGBA, GL_UNSIGNED_INT_8_8_8_8_REV, texture);
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);

            memFree(texture);
            memFree(bitmap);
        }

        default_font
                .width((handle, h, text, len) -> {
                    float text_width = 0;
                    try (MemoryStack stack = stackPush()) {
                        IntBuffer unicode = stack.mallocInt(1);

                        int glyph_len = nnk_utf_decode(text, memAddress(unicode), len);
                        int text_len  = glyph_len;

                        if (glyph_len == 0) {
                            return 0;
                        }

                        IntBuffer advance = stack.mallocInt(1);
                        while (text_len <= len && glyph_len != 0) {
                            if (unicode.get(0) == NK_UTF_INVALID) {
                                break;
                            }

                            /* query currently drawn glyph information */
                            stbtt_GetCodepointHMetrics(fontInfo, unicode.get(0), advance, null);
                            text_width += advance.get(0) * scale;

                            /* offset next glyph */
                            glyph_len = nnk_utf_decode(text + text_len, memAddress(unicode), len - text_len);
                            text_len += glyph_len;
                        }
                    }
                    return text_width;
                })
                .height(FONT_HEIGHT)
                .query((handle, font_height, glyph, codepoint, next_codepoint) -> {
                    try (MemoryStack stack = stackPush()) {
                        FloatBuffer x = stack.floats(0.0f);
                        FloatBuffer y = stack.floats(0.0f);

                        STBTTAlignedQuad q       = STBTTAlignedQuad.mallocStack(stack);
                        IntBuffer        advance = stack.mallocInt(1);

                        stbtt_GetPackedQuad(cdata, BITMAP_W, BITMAP_H, codepoint - 32, x, y, q, false);
                        stbtt_GetCodepointHMetrics(fontInfo, codepoint, advance, null);

                        NkUserFontGlyph ufg = NkUserFontGlyph.create(glyph);

                        ufg.width(q.x1() - q.x0());
                        ufg.height(q.y1() - q.y0());
                        ufg.offset().set(q.x0(), q.y0() + (FONT_HEIGHT + descent));
                        ufg.xadvance(advance.get(0) * scale);
                        ufg.uv(0).set(q.s0(), q.t0());
                        ufg.uv(1).set(q.s1(), q.t1());
                    }
                })
                .texture(it -> it
                        .id(fontTexID));

        nk_style_set_font(ctx, default_font);
    }

    private static void renderNuklear(int AA, int max_vertex_buffer, int max_element_buffer) {
        try (MemoryStack stack = stackPush()) {
            // setup global state
            glEnable(GL_BLEND);
            glBlendEquation(GL_FUNC_ADD);
            glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
            glDisable(GL_CULL_FACE);
            glDisable(GL_DEPTH_TEST);
            glEnable(GL_SCISSOR_TEST);
            glActiveTexture(GL_TEXTURE0);

            // setup program
            glUseProgram(prog);
            glUniform1i(uniform_tex, 0);
            glUniformMatrix4fv(uniform_proj, false, stack.floats(
                    2.0f / width, 0.0f, 0.0f, 0.0f,
                    0.0f, -2.0f / height, 0.0f, 0.0f,
                    0.0f, 0.0f, -1.0f, 0.0f,
                    -1.0f, 1.0f, 0.0f, 1.0f
            ));
            glViewport(0, 0, width, height);
        }


        {
            // convert from command queue into draw list and draw to screen

            // allocate vertex and element buffer
            glBindVertexArray(vao);
            glBindBuffer(GL_ARRAY_BUFFER, vbo);
            glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, ebo);

            glBufferData(GL_ARRAY_BUFFER, max_vertex_buffer, GL_STREAM_DRAW);
            glBufferData(GL_ELEMENT_ARRAY_BUFFER, max_element_buffer, GL_STREAM_DRAW);

            // load draw vertices & elements directly into vertex + element buffer
            ByteBuffer vertices = Objects.requireNonNull(glMapBuffer(GL_ARRAY_BUFFER, GL_WRITE_ONLY, max_vertex_buffer, null));
            ByteBuffer elements = Objects.requireNonNull(glMapBuffer(GL_ELEMENT_ARRAY_BUFFER, GL_WRITE_ONLY, max_element_buffer, null));
            try (MemoryStack stack = stackPush()) {
                // fill convert configuration
                NkConvertConfig config = NkConvertConfig.callocStack(stack);
                        config.vertex_layout(VERTEX_LAYOUT);
                        config.vertex_size(20)
                        .vertex_alignment(4)
                        .null_texture(null_texture)
                        .circle_segment_count(22)
                        .curve_segment_count(22)
                        .arc_segment_count(22)
                        .global_alpha(1.0f)
                        .shape_AA(AA)
                        .line_AA(AA);

                // setup buffers to load vertices and elements
                NkBuffer vbuf = NkBuffer.mallocStack(stack);
                NkBuffer ebuf = NkBuffer.mallocStack(stack);

                nk_buffer_init_fixed(vbuf, vertices/*, max_vertex_buffer*/);
                nk_buffer_init_fixed(ebuf, elements/*, max_element_buffer*/);
                nk_convert(ctx, cmds, vbuf, ebuf, config);
            }
            glUnmapBuffer(GL_ELEMENT_ARRAY_BUFFER);
            glUnmapBuffer(GL_ARRAY_BUFFER);

            // iterate over and execute each draw command
            float fb_scale_x = (float)monitorWidth / (float)width;
            float fb_scale_y = (float)monitorHeight / (float)height;

            long offset = NULL;
            for (NkDrawCommand cmd = nk__draw_begin(ctx, cmds); cmd != null; cmd = nk__draw_next(cmd, cmds, ctx)) {
                if (cmd.elem_count() == 0) {
                    continue;
                }
                glBindTexture(GL_TEXTURE_2D, cmd.texture().id());
                glScissor(
                        (int)(cmd.clip_rect().x() * fb_scale_x),
                        (int)((height - (int)(cmd.clip_rect().y() + cmd.clip_rect().h())) * fb_scale_y),
                        (int)(cmd.clip_rect().w() * fb_scale_x),
                        (int)(cmd.clip_rect().h() * fb_scale_y)
                );
                glDrawElements(GL_TRIANGLES, cmd.elem_count(), GL_UNSIGNED_SHORT, offset);
                offset += cmd.elem_count() * 2;
            }
            nk_clear(ctx);
        }

        // default OpenGL state
        glUseProgram(0);
        glBindBuffer(GL_ARRAY_BUFFER, 0);
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
        glBindVertexArray(0);
        glDisable(GL_BLEND);
        glDisable(GL_SCISSOR_TEST);
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
