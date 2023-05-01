package org.JE.JE2.IO.UserInput.Mouse;

import org.JE.JE2.Manager;
import org.JE.JE2.Rendering.Camera;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector4f;

import java.util.ArrayList;

public class Mouse {
    private static float x, y;

    public static boolean disableInput = false;

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
        if(disableInput)
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
        if(disableInput)
            return;
        mousePressedEvents.forEach(event -> event.invoke(button, mods));
        mousePressed(button.ordinal());
    }

    public static void triggerMouseReleased(MouseButton button, int mods){
        if(disableInput)
            return;
        mouseReleasedEvents.forEach(event -> event.invoke(button, mods));
        mouseReleased(button.ordinal());
    }

    public static void triggerMouseClick(MouseButton button, int mods){
        if(disableInput)
            return;
        triggerMousePressed(button, mods);
        triggerMouseReleased(button, mods);
    }

    private static final boolean[] buttons = new boolean[8];

    private static void mousePressed(int button){
        if(button >= buttons.length || button < 0)
            return;
        buttons[button] = true;
    }

    private static void mouseReleased(int button){
        if(button >= buttons.length || button < 0)
            return;
        buttons[button] = false;
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
}
