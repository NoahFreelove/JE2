package JE.Objects.Base;

import JE.IO.ImageProcessor;
import JE.Objects.Components.SpriteRenderer;
import JE.Rendering.ShaderProgram;
import JE.Rendering.VAO;
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


    private void init(Vector2f[] vertices, ShaderProgram sp, String spriteFilePath, Vector2i size){
        sr = new SpriteRenderer(new VAO(vertices, sp));
        sr.setTexture(ImageProcessor.ProcessImage(spriteFilePath), size);
        addComponent(sr);
    }
}
