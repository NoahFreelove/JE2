package JE.Objects.Gizmos;

import JE.Input.Mouse;
import JE.Input.MousePressedEvent;
import JE.Input.MouseReleasedEvent;
import JE.Objects.Base.GameObject;
import JE.Rendering.Camera;
import org.joml.Vector2f;
import org.joml.Vector4f;

import static org.lwjgl.opengl.GL11.GL_POLYGON;

public class MoveGizmo extends Gizmo{
    GameObject controlledObject;
    boolean interacting = false;

    Vector2f prevMousePos = new Vector2f();

    public MoveGizmo(){
        super();
        setBaseColor(new Vector4f(1,0,0,0.5f));

        Mouse.mousePressedEvents.add((button, mods) -> {
            if(controlledObject == null)
                return;
            if(button == Mouse.nameToCode("LEFT")){
                Vector2f cursorPos = Mouse.getMouseWorldPosition();
                Vector2f bottomLeft = controlledObject.getTransform().position;
                Vector2f topRight = new Vector2f(bottomLeft.x()+1,bottomLeft.y()+1);
                if(cursorPos.x() >= bottomLeft.x() && cursorPos.x() <= topRight.x() && cursorPos.y() >= bottomLeft.y() && cursorPos.y() <= topRight.y()){
                    interacting = true;
                }
            }
        });

        Mouse.mouseReleasedEvents.add((button, mods) -> {
            if(controlledObject == null)
                return;
            if(button == Mouse.nameToCode("LEFT")){
                interacting = false;
            }
        });
    }

    @Override
    public void onDraw() {
        if(controlledObject == null)
            return;

        if(!interacting)
        {
            prevMousePos = Mouse.getMouseWorldPosition();
            getTransform().position = controlledObject.getTransform().position;
            return;
        }
        controlledObject.getTransform().position.add(Mouse.getMouseWorldPosition().sub(prevMousePos));
        prevMousePos = Mouse.getMouseWorldPosition();
    }

    public void setControlledObject(GameObject object){
        this.controlledObject = object;
        getTransform().position = controlledObject.getTransform().position;
    }
}
