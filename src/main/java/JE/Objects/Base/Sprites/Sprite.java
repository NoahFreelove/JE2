package JE.Objects.Base.Sprites;

import JE.IO.ImageProcessor;
import JE.Objects.Base.GameObject;
import JE.Objects.Components.Base.ComponentRestrictions;
import JE.Rendering.Shaders.BuiltIn.LightSprite.LightSpriteShader;
import JE.Rendering.Shaders.BuiltIn.SpriteShader;
import JE.Rendering.RenderTypes.SpriteRenderer;
import JE.Rendering.Shaders.ShaderProgram;
import JE.Rendering.Texture;
import JE.Rendering.VertexBuffers.VAO2f;
import org.joml.Vector2f;
import org.joml.Vector2i;

public class Sprite extends GameObject {

    protected SpriteRenderer sr;

    public Sprite(){
        super();
        init(new Vector2f[]{}, new SpriteShader(), "", new Vector2i(64,64));
    }
    public Sprite(Vector2f[] vertices)
    {
        super();
        init(vertices, new SpriteShader(), "", new Vector2i(64,64));
    }

    public Sprite(Vector2f[] vertices, String spriteFilePath, Vector2i size)
    {
        super();
        init(vertices, new LightSpriteShader(), spriteFilePath, size);
    }
    public Sprite(Vector2f[] vertices, String spriteFilePath, ShaderProgram sp)
    {
        super();
        sr = new SpriteRenderer(new VAO2f(vertices, sp), new Texture(spriteFilePath));
    }


    public Sprite(Vector2f[] vertices, Vector2f[] uv, String spriteFilePath, Vector2i size)
    {
        super();
        sr = new SpriteRenderer(new VAO2f(vertices, new SpriteShader()),uv, new Texture(spriteFilePath, size));
        sr.setRestrictions(new ComponentRestrictions(false, true, false));
        addComponent(sr);
    }

    public Sprite(Vector2f[] vertices, Vector2f[] uv, ShaderProgram shaderProgram)
    {
        super();
        sr = new SpriteRenderer(new VAO2f(vertices, shaderProgram),uv, new Texture());
        sr.setRestrictions(new ComponentRestrictions(false, true, false));
        addComponent(sr);
    }

    public Sprite(Vector2f[] vertices, ShaderProgram shaderProgram)
    {
        super();
        sr = new SpriteRenderer(new VAO2f(vertices, shaderProgram), new Texture());
        sr.setRestrictions(new ComponentRestrictions(false, true, false));
        addComponent(sr);
    }

    public Sprite(Vector2f[] vertices, ShaderProgram sp, String spriteFP, Vector2i spriteSize)
    {
        super();
        init(vertices, sp, spriteFP, spriteSize);
    }

    private void init(Vector2f[] vertices, ShaderProgram sp, String spriteFilePath, Vector2i size){
        sr = new SpriteRenderer(new VAO2f(vertices, sp), new Texture(spriteFilePath, size));
        sr.setRestrictions(new ComponentRestrictions(false, true, false));
        addComponent(sr);
    }
}
