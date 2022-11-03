package JE.Objects;

import JE.Objects.Components.SpriteRenderer;
import JE.Rendering.ShaderProgram;
import JE.Rendering.VAO;
import org.joml.Vector2f;

public class Sprite extends GameObject {
    public Sprite(){
        super();
        init(new Vector2f[]{}, new ShaderProgram());
    }
    public Sprite(Vector2f[] vertices)
    {
        super();
        init(vertices, new ShaderProgram());
    }


    private void init(Vector2f[] vertices, ShaderProgram sp){
        addComponent(new SpriteRenderer(new VAO(vertices, sp)));
    }
}
