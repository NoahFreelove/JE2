package JE.Objects.Lights;

import JE.Objects.Base.GameObject;
import JE.Rendering.RenderTypes.Renderer;
import JE.Rendering.Shaders.BuiltIn.SimpleLightShader;
import JE.Rendering.VertexBuffers.VAO;
import org.joml.Vector2f;
import org.joml.Vector4f;

public class SimpleLight extends GameObject {
    public SimpleLight(Vector2f position, Vector2f size, Vector4f color){
        super();
        getTransform().position = new Vector2f(position.x(), position.y());
        addComponent(new Renderer(new VAO(new float[]{}, new SimpleLightShader(size, color))));
    }
}
