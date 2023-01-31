package JE.Objects.Gizmos;

import JE.Objects.Base.Sprite;
import JE.UI.UIElements.Style.Color;
import org.joml.Vector4f;

import static org.lwjgl.opengl.GL11.GL_POLYGON;

public class Gizmo extends Sprite {

    public Gizmo(){
        super();
        renderer.baseColor = Color.WHITE;
        renderer.setDrawMode(GL_POLYGON);
        getTransform().setZPos(10);
    }


    public void onDraw(){}

}
