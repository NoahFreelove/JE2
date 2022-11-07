package JE.Objects.Base;

import JE.IO.ImageProcessor;
import JE.Objects.Components.ComponentRestrictions;
import JE.Rendering.SpriteRenderer;
import JE.Rendering.ShaderProgram;
import JE.Rendering.Texture;
import JE.Rendering.VAO2f;
import org.joml.Vector2f;
import org.joml.Vector2i;

public class Sprite extends GameObject {

    protected SpriteRenderer sr;
    public Sprite(){
        super();
        init(new Vector2f[]{}, new ShaderProgram(), "", new Vector2i(64,64));
    }
    public Sprite(Vector2f[] vertices)
    {
        super();
        init(vertices, new ShaderProgram(), "", new Vector2i(64,64));
    }

    public Sprite(Vector2f[] vertices, String spriteFilePath, Vector2i size)
    {
        super();
        init(vertices, new ShaderProgram(), spriteFilePath, size);
    }

    public Sprite(Vector2f[] vertices, Vector2f[] uv, String spriteFilePath, Vector2i size)
    {
        super();
        sr = new SpriteRenderer(new VAO2f(vertices, new ShaderProgram()),uv, new Texture(ImageProcessor.ProcessImage(spriteFilePath), size));
        sr.setRestrictions(new ComponentRestrictions(false, true, false));
        addComponent(sr);
    }

    private void init(Vector2f[] vertices, ShaderProgram sp, String spriteFilePath, Vector2i size){
        sr = new SpriteRenderer(new VAO2f(vertices, sp), new Texture(ImageProcessor.ProcessImage(spriteFilePath), size));
        sr.setRestrictions(new ComponentRestrictions(false, true, false));
        addComponent(sr);
    }
}
