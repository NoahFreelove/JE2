package JE.Objects.Gizmos;

import JE.Objects.Base.Sprites.Sprite;
import JE.Rendering.Shaders.BuiltIn.SpriteShader;
import JE.Rendering.Shaders.ShaderProgram;
import org.joml.Vector2f;
import org.joml.Vector2i;
import org.joml.Vector4f;
import org.lwjgl.stb.STBImage;

import static org.lwjgl.opengl.GL11.*;

public class Gizmo extends Sprite {

    public Gizmo(){
        super();
        renderer.baseColor = new Vector4f(1,1,1,1);
        renderer.setDrawMode(GL_POLYGON);
        getTransform().zPos = 10;
    }


    public void onDraw(){}

}
