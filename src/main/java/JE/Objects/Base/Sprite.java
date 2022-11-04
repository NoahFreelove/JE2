package JE.Objects.Base;

import JE.IO.ImageProcessor;
import JE.Objects.Components.SpriteRenderer;
import JE.Rendering.ShaderProgram;
import JE.Rendering.VAO;
import org.joml.Vector2f;

public class Sprite extends GameObject {

    protected SpriteRenderer sr;
    public Sprite(){
        super();
        init(new Vector2f[]{}, new ShaderProgram(), "");
    }
    public Sprite(Vector2f[] vertices)
    {
        super();
        init(vertices, new ShaderProgram(), "");
    }

    public Sprite(Vector2f[] vertices, String spriteFilePath)
    {
        super();
        init(vertices, new ShaderProgram(), spriteFilePath);
    }


    private void init(Vector2f[] vertices, ShaderProgram sp, String spriteFilePath){
        sr = new SpriteRenderer(new VAO(vertices, sp));
        sr.setTexture(ImageProcessor.ProcessImage(spriteFilePath));
        System.out.println(ImageProcessor.ProcessImage(spriteFilePath).limit());
        addComponent(sr);
    }
}
