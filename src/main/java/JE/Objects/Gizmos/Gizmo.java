package JE.Objects.Gizmos;

import JE.Objects.Base.Sprite;
import JE.Rendering.Shaders.ShaderProgram;
import JE.Rendering.Texture;
import org.joml.Vector2f;
import org.joml.Vector2i;
import org.joml.Vector4f;

import static org.lwjgl.opengl.GL11.*;

public class Gizmo extends Sprite {

    public Gizmo(){
        super(new Vector2f[]{
                new Vector2f(0,0),
                new Vector2f(1,0),
                new Vector2f(1,1),
                new Vector2f(0,1)
        },new Vector2f[]{
                new Vector2f(0,0),
                new Vector2f(1,0),
                new Vector2f(1,1),
                new Vector2f(0,1)
        },new ShaderProgram());

        renderer.baseColor = new Vector4f(1,1,1,1);
        renderer.setDrawMode(GL_POLYGON);
        getTransform().zPos = 10;
    }

    public Gizmo(Vector2f[] vertices, Vector4f color, int drawMode){
        super(vertices,vertices,new ShaderProgram());
        getTransform().zPos = 10;
        renderer.baseColor = color;
        renderer.setDrawMode(drawMode);
    }

    public Gizmo(Vector2f[] vertices, String spriteFP, Vector2i size){
        super(vertices,new ShaderProgram(), spriteFP, size);
        getTransform().zPos = 10;
    }

    public void onDraw(){}

}
