package org.JE.JE2.IO.UserInput.Mouse;

import org.JE.JE2.Manager;
import org.JE.JE2.Rendering.Camera;
import org.JE.JE2.Window.UIHandler;
import org.JE.JE2.Window.Window;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector4f;
import org.lwjgl.nuklear.NkVec2;
import org.lwjgl.system.MemoryStack;

import java.util.ArrayList;
import java.util.Arrays;

import static org.JE.JE2.Window.UIHandler.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.nuklear.Nuklear.nk_input_motion;
import static org.lwjgl.nuklear.Nuklear.nk_input_scroll;
import static org.lwjgl.system.MemoryStack.stackPush;

public class Mouse {
    private static float x;
    private static float y;

    public static boolean disableGameInput = false;
    public static boolean disableUIInput = false;

    private static final ArrayList<MousePressedEvent> mousePressedEvents = new ArrayList<>();
    private static final ArrayList<MouseReleasedEvent> mouseReleasedEvents = new ArrayList<>();

    public static void addMousePressedEvent(MousePressedEvent event){
        if(mousePressedEvents.contains(event))
            return;
        mousePressedEvents.add(event);
    }
    public static void addMouseReleasedEvent(MouseReleasedEvent event){
        if(mouseReleasedEvents.contains(event))
            return;
        mouseReleasedEvents.add(event);
    }

    public static void removeMousePressedEvent(MousePressedEvent event){
        mousePressedEvents.remove(event);
    }
    public static void removeMouseReleasedEvent(MouseReleasedEvent event){
        mouseReleasedEvents.remove(event);
    }

    public static void triggerMouseMoved(float x, float y){
        if(disableGameInput)
            return;
        Mouse.x = x;
        Mouse.y = y;
    }

    public static Vector2f getMousePosition(){
        return new Vector2f(x, y);
    }

    public static float getX(){
        return x;
    }
    public static float getY(){
        return y;
    }

    public static void triggerMousePressed(MouseButton button, int mods){
        if(disableGameInput)
            return;
        mousePressedEvents.forEach(event -> event.invoke(button, mods));
        mousePressed(button.ordinal());
    }

    public static void triggerMouseReleased(MouseButton button, int mods){
        if(disableGameInput)
            return;
        mouseReleasedEvents.forEach(event -> event.invoke(button, mods));
        mouseReleased(button.ordinal());
    }

    public static void triggerMouseClick(MouseButton button, int mods){
        if(disableGameInput)
            return;
        triggerMousePressed(button, mods);
        triggerMouseReleased(button, mods);
    }

    private static final boolean[] buttons = new boolean[8];
    private static final long[] pressedFor = new long[8];
    private static final long[] releasedSince = new long[8];

    static {
        for(int i = 0; i < buttons.length; i++){
            buttons[i] = false;
            pressedFor[i] = 0;
            releasedSince[i] = System.currentTimeMillis();
        }
    }

    public static void reset(){
        Arrays.fill(buttons,false);
    }

    private static void mousePressed(int button){
        if(button >= buttons.length || button < 0)
            return;
        buttons[button] = true;
        pressedFor[button] = System.currentTimeMillis();
        releasedSince[button] = 0;
    }

    private static void mouseReleased(int button){
        if(button >= buttons.length || button < 0)
            return;
        buttons[button] = false;
        pressedFor[button] = 0;
        releasedSince[button] = System.currentTimeMillis();
    }

    public static boolean isPressed(int button){
        if(button >= buttons.length || button < 0)
            return false;
        return buttons[button];
    }

    public static int nameToCode(String buttonName){
        buttonName = buttonName.toUpperCase();
        return switch (buttonName){
            case "LEFT", "BUTTON1" -> 0;
            case "RIGHT", "BUTTON2" -> 1;
            case "MIDDLE", "BUTTON3" -> 2;
            case "BUTTON4" -> 3;
            case "BUTTON5" -> 4;
            case "BUTTON6" -> 5;
            case "BUTTON7" -> 6;
            case "BUTTON8" -> 7;
            default -> -1;
        };
    }

    public static float buttonPressedForSeconds(int button){
        if(button >= buttons.length || button < 0)
            return 0;
        if(pressedFor[button] == 0)
            return 0;
        long delta = System.currentTimeMillis() - pressedFor[button];
        return delta / 1000f;
    }

    public static float buttonReleasedForSeconds(int button){
        if(button >= buttons.length || button < 0)
            return 0;
        if(releasedSince[button] == 0)
            return 0;

        long delta = System.currentTimeMillis() - releasedSince[button];
        return delta / 1000f;
    }

    public static Vector2f getMouseWorldPosition2D() {
        return getMouseWorldPosition2D(Manager.getMainCamera());
    }
    public static Vector2f getMouseWorldPosition2D(Camera c){
        Vector2f cursorPos = getMousePosition();
        Matrix4f viewMatrix = c.getViewMatrix();
        Matrix4f projectionMatrix = c.getOrtho();
        Matrix4f inverted = new Matrix4f();
        projectionMatrix.mul(viewMatrix, inverted);
        inverted.invert();
        Vector4f worldPos = new Vector4f(cursorPos.x(), cursorPos.y(),0,1);
        worldPos.mul(2f / Manager.getWindowSize().x(), 2f / Manager.getWindowSize().y(),1,1);
        worldPos.y = 1 - worldPos.y;
        worldPos.sub(1,0,0,0);
        worldPos.mul(inverted);

        return new Vector2f(worldPos.x(),worldPos.y());
    }


    private static boolean setupMouse = false;
    public static void setupMouse(){
        if(setupMouse)
            return;
        setupMouse = true;

        glfwSetMouseButtonCallback(Window.getWindowHandle(), (windowHandle, button, action, mods) -> {
            if(action == GLFW_PRESS){
                Mouse.triggerMousePressed(MouseButton.values()[button], mods);
                triggerUIMouseInput(button,true);
            }
            else if(action == GLFW_RELEASE){
                Mouse.triggerMouseReleased(MouseButton.values()[button], mods);
                triggerUIMouseInput(button,false);
            }
        });

        glfwSetCursorPosCallback(Window.getWindowHandle(), (window, xpos, ypos) -> {
            Mouse.triggerMouseMoved((float)xpos, (float)ypos);
            if(nuklearReady && !disableUIInput)
                nk_input_motion(nuklearContext, (int)xpos, (int)ypos);
        });

        glfwSetScrollCallback(Window.getWindowHandle(), (window, xoffset, yoffset) -> {
            if(disableUIInput)
                return;
            if(!nuklearReady)
                return;
            try (MemoryStack stack = stackPush()) {
                NkVec2 scroll = NkVec2.malloc(stack)
                        .x((float)xoffset)
                        .y((float)yoffset);
                nk_input_scroll(nuklearContext, scroll);
            }
        });
    }
}
