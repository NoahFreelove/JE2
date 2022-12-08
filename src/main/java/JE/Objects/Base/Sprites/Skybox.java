package JE.Objects.Base.Sprites;

import org.joml.Vector2f;
import org.joml.Vector4f;

import static org.lwjgl.opengl.GL11.GL_POLYGON;

public class Skybox extends Sprite {
    public Skybox(Vector4f color){
        super();
        getTransform().scale = new Vector2f(1000,1000);
        getTransform().position = new Vector2f(-500,-500);
        renderer.setDrawMode(GL_POLYGON);
        renderer.baseColor = color;
    }
}
