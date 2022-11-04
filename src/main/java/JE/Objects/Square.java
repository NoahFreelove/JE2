package JE.Objects;

import JE.Objects.Base.Sprite;
import org.joml.Vector2f;

import static org.lwjgl.opengl.GL11.GL_POLYGON;

public class Square extends Sprite {

    public Square(){
        super(new Vector2f[]{
                new Vector2f(-0.5f, -0.5f),
                new Vector2f(0.5f, -0.5f),
                new Vector2f(0.5f, 0.5f),
                new Vector2f(-0.5f, 0.5f)
        }, "bin/texture.png");
        renderer.setDrawMode(GL_POLYGON);
    }
}
