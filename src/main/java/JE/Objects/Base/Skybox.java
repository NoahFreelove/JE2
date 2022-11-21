package JE.Objects.Base;

import JE.Rendering.Shaders.BuiltIn.LightObject.LightObjectShader;
import JE.Rendering.Shaders.BuiltIn.LightSprite.LightSpriteShader;
import JE.Rendering.Shaders.BuiltIn.SpriteShader;
import JE.Rendering.Shaders.ShaderProgram;
import org.joml.Vector2f;
import org.joml.Vector4f;

import static org.lwjgl.opengl.GL11.GL_POLYGON;

public class Skybox extends Sprite {
    public Skybox(Vector4f color){
        super( // Vertices
                new Vector2f[]{
                        new Vector2f(Integer.MIN_VALUE,Integer.MIN_VALUE),
                        new Vector2f(Integer.MAX_VALUE,Integer.MIN_VALUE),
                        new Vector2f(Integer.MAX_VALUE,Integer.MAX_VALUE),
                        new Vector2f(Integer.MIN_VALUE,Integer.MAX_VALUE)
                },
                // UVs
                new Vector2f[]{
                        new Vector2f(0,0),
                        new Vector2f(1,0),
                        new Vector2f(1,1),
                        new Vector2f(0,1)
                },
                new LightObjectShader());
        renderer.setDrawMode(GL_POLYGON);
        renderer.baseColor = color;
    }
}
