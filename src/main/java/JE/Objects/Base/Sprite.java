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

    protected String spriteVertexShader = "#version 330 core\n" +
            "layout(location = 0) in vec2 modelPos;\n" +
            "layout(location = 1) in vec2 texCoord;\n" +
            "\n" +
            "uniform mat4 MVP;\n" +
            "uniform float zPos;\n" +
            "out vec2 UV;\n" +
            "void main(){\n" +
            "  vec4 pos = MVP * vec4(modelPos, zPos, 1);\n" +
            "  gl_Position = pos;\n" +
            "  UV = texCoord;\n" +
            "}";

    protected String spriteFragmentShader = "#version 330 core\n" +
            "out vec4 FragColor;" +
            "uniform sampler2D JE_Texture;\n" +
            "in vec2 UV;\n" +
            "\n" +
            "void main(){\n" +
            "  FragColor = texture(JE_Texture, UV);\n" +
            "}";

    public Sprite(){
        super();
        init(new Vector2f[]{}, new ShaderProgram(spriteVertexShader, spriteFragmentShader), "", new Vector2i(64,64));
    }
    public Sprite(Vector2f[] vertices)
    {
        super();
        init(vertices, new ShaderProgram(spriteVertexShader, spriteFragmentShader), "", new Vector2i(64,64));
    }

    public Sprite(Vector2f[] vertices, String spriteFilePath, Vector2i size)
    {
        super();
        init(vertices, new ShaderProgram(spriteVertexShader, spriteFragmentShader), spriteFilePath, size);
    }

    public Sprite(Vector2f[] vertices, Vector2f[] uv, String spriteFilePath, Vector2i size)
    {
        super();
        sr = new SpriteRenderer(new VAO2f(vertices, new ShaderProgram(spriteVertexShader, spriteFragmentShader)),uv, new Texture(ImageProcessor.ProcessImage(spriteFilePath), size));
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

    private void init(Vector2f[] vertices, ShaderProgram sp, String spriteFilePath, Vector2i size){
        sr = new SpriteRenderer(new VAO2f(vertices, sp), new Texture(ImageProcessor.ProcessImage(spriteFilePath), size));
        sr.setRestrictions(new ComponentRestrictions(false, true, false));
        addComponent(sr);
    }
}
