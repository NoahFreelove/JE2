package JE.Objects.Lights;

import JE.Objects.Base.GameObject;
import org.joml.Vector2f;
import org.joml.Vector4f;

public class Light extends GameObject {
    public Vector4f color;
    public Vector2f size;

    public Light(Vector2f position, Vector2f size, Vector4f color){
        super();
        getTransform().position = new Vector2f(position.x(), position.y());
        this.size = size;
        this.color = color;
    }
}
