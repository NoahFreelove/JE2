package JE.Objects;

import JE.Objects.Base.Sprite;
import org.joml.Vector2f;
import org.joml.Vector2i;

import static org.lwjgl.opengl.GL11.GL_POLYGON;

public class Square extends Sprite {

    public Square(){
        super(  // Vertices
                new Vector2f[]{
                        new Vector2f(0,0),
                        new Vector2f(1,0),
                        new Vector2f(1,1),
                        new Vector2f(0,1)
                },

                "bin/cat.png",
                new Vector2i(158,209));
        renderer.setDrawMode(GL_POLYGON);
    }

    public Square(Vector2f pos){
        super(  // Vertices
                new Vector2f[]{
                        new Vector2f(0,0),
                        new Vector2f(1,0),
                        new Vector2f(1,1),
                        new Vector2f(0,1)
                },

                "bin/cat.png",
                new Vector2i(158,209));
        renderer.setDrawMode(GL_POLYGON);
        getTransform().position = pos;
    }
}
