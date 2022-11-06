package JE.Objects;

import JE.Objects.Base.Sprite;
import org.joml.Vector2f;
import org.joml.Vector2i;

import static org.lwjgl.opengl.GL11.GL_POLYGON;

public class Square extends Sprite {

    public Square(){
        super(new Vector2f[]{
                new Vector2f(-1f, -1f),
                new Vector2f(1f, -1f),
                new Vector2f(1f, 1f),
                new Vector2f(-1f, 1f)
        }, "bin/texture.png", new Vector2i(128,128));
        renderer.setDrawMode(GL_POLYGON);
    }
}
