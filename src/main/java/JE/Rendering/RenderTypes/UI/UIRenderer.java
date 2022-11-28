package JE.Rendering.RenderTypes.UI;

import JE.Objects.Components.Transform;
import JE.Rendering.RenderTypes.Renderer;
import JE.Rendering.RenderTypes.SpriteRenderer;
import JE.Rendering.Shaders.ShaderProgram;
import JE.Rendering.Texture;
import JE.Rendering.VertexBuffers.VAO;
import JE.Rendering.VertexBuffers.VAO2f;
import org.joml.Vector2f;
import org.joml.Vector2i;

import static org.lwjgl.opengl.GL11.GL_POLYGON;

public class UIRenderer extends SpriteRenderer {

    public UIRenderer(Vector2f[] bounds, ShaderProgram sp)
    {
        super(new VAO2f(bounds,sp));
        setDrawMode(GL_POLYGON);
    }

    public UIRenderer(Vector2f[] bounds, ShaderProgram sp, Texture t)
    {
        super(new VAO2f(bounds,sp),t);
        setDrawMode(GL_POLYGON);
    }

    @Override
    public void Render(Transform t, int additionalBuffer){

        // Draw on screen not in world
        t.position = new Vector2f(0,0);
        // draw on top of everything
        t.zPos = 1;


        super.Render(t,additionalBuffer);
    }
}
