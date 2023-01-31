package JE.Objects.Gizmos;

import JE.IO.UserInput.Mouse.Mouse;
import JE.IO.UserInput.Mouse.MouseButton;
import JE.Objects.Base.GameObject;
import JE.UI.UIElements.Style.Color;
import org.joml.Vector2f;

public class MoveGizmo extends Gizmo{
    GameObject controlledObject;
    boolean interacting = false;

    Vector2f prevMousePos = new Vector2f();

    public MoveGizmo(){
        super();
        setBaseColor(Color.createColor(1,0,0,0.5f));

        Mouse.mousePressedEvents.add((button, mods) -> {
            if(controlledObject == null)
                return;
            if(button == MouseButton.LEFT){
                Vector2f cursorPos = Mouse.getMouseWorldPosition();
                Vector2f bottomLeft = controlledObject.getTransform().position();
                Vector2f topRight = new Vector2f(bottomLeft.x()+1,bottomLeft.y()+1);
                if(cursorPos.x() >= bottomLeft.x() && cursorPos.x() <= topRight.x() && cursorPos.y() >= bottomLeft.y() && cursorPos.y() <= topRight.y()){
                    interacting = true;
                }
            }
        });

        Mouse.mouseReleasedEvents.add((button, mods) -> {
            if(controlledObject == null)
                return;
            if(button == MouseButton.LEFT){
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
            getTransform().setPosition(controlledObject.getTransform().position());
            return;
        }
        controlledObject.getTransform().setPosition(controlledObject.getTransform().position().add(Mouse.getMouseWorldPosition().sub(prevMousePos)));
        prevMousePos = Mouse.getMouseWorldPosition();
    }

    public void setControlledObject(GameObject object){
        this.controlledObject = object;
        getTransform().setPosition(controlledObject.getTransform().position());
    }
}
