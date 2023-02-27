package JE.IO.UserInput.Mouse;

import JE.Manager;
import JE.Rendering.Camera;
import JE.Window.Window;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector4f;
import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFW;

import java.nio.DoubleBuffer;
import java.util.ArrayList;

public class Mouse {
    public static ArrayList<MousePressedEvent> mousePressedEvents = new ArrayList<>();
    public static ArrayList<MouseReleasedEvent> mouseReleasedEvents = new ArrayList<>();

    private static final boolean[] buttons = new boolean[8];

    public static void mousePressed(int button){
        if(button >= buttons.length)
            return;
        buttons[button] = true;
    }
    public static void mouseReleased(int button){
        if(button >= buttons.length)
            return;
        buttons[button] = false;
    }

    public static boolean isPressed(int button){
        if(button >= buttons.length)
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

    public static Vector2f getMousePosition(){
        DoubleBuffer x = BufferUtils.createDoubleBuffer(1);
        DoubleBuffer y = BufferUtils.createDoubleBuffer(1);
        GLFW.glfwGetCursorPos(Window.getWindowHandle(),x,y);
        return new Vector2f((float) x.get(), (float) y.get());
    }
    public static Vector2f getMouseWorldPosition() {
        return getMouseWorldPosition(Manager.getMainCamera());
    }
    public static Vector2f getMouseWorldPosition(Camera c){
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
