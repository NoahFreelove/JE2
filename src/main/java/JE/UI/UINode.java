package JE.UI;

import JE.Objects.Base.GameObject;
import JE.Rendering.RenderTypes.UI.UIRenderer;
import JE.Rendering.Shaders.BuiltIn.UI.ScreenUI.ScreenUIShader;
import JE.Rendering.Shaders.ShaderProgram;
import JE.Rendering.Texture;
import org.joml.Vector2f;
import org.joml.Vector2i;
import org.joml.Vector4f;

public class UINode extends GameObject {

    public UINode(Vector2f position, Vector2f size){
        super();
        getTransform().position = position;
        getTransform().scale = size;
        renderer = new UIRenderer(new Vector2f[]{
                new Vector2f(0,0),
                new Vector2f(1,0),
                new Vector2f(1,1),
                new Vector2f(0,1)
        },new ScreenUIShader());
        renderer.baseColor = new Vector4f(0.5f,0.5f,0.5f,1);
    }

    public UINode(Vector2f position, Vector2f size, String texturePath, Vector2i textureSize){
        super();
        getTransform().position = position;
        getTransform().scale = size;
        renderer = new UIRenderer(new Vector2f[]{
                new Vector2f(0,0),
                new Vector2f(1,0),
                new Vector2f(1,1),
                new Vector2f(0,1)
        },new ShaderProgram(),new Texture(texturePath,textureSize));
        renderer.baseColor = new Vector4f(0.5f,0.5f,0.5f,1);
    }
}
