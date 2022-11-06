package JE.Rendering;

import JE.Annotations.GLThread;
import JE.IO.ImageProcessor;
import JE.Objects.Components.Transform;
import org.joml.Vector2f;
import org.joml.Vector2i;

import java.nio.ByteBuffer;

public class SpriteRenderer extends Renderer {
    private VAO2f spriteCoordVAO = new VAO2f();
    public Texture texture = new Texture();

    public SpriteRenderer(){
        spriteCoordVAO = new VAO2f(vao.getVertices(), vao.shaderProgram);
    }

    public SpriteRenderer(VAO2f vao){
        super(vao);
        spriteCoordVAO = new VAO2f(vao.getVertices(), vao.shaderProgram);
    }

    public SpriteRenderer(VAO2f vao, Texture texture){
        super(vao);
        spriteCoordVAO = new VAO2f(vao.getVertices(), vao.shaderProgram);
        this.texture = texture;
    }

    public SpriteRenderer(VAO2f vao, Vector2f[] uv, Texture texture){
        super(vao);
        spriteCoordVAO = new VAO2f(uv, vao.shaderProgram);
        this.texture = texture;
    }

    @Override
    public void update() {

    }

    @Override
    public void start() {

    }

    @Override
    public void awake() {

    }

    @Override
    @GLThread
    public void Render(Transform t){
        if(texture.generatedTextureID <=-1) {
            super.Render(t, 0);
            return;
        }

        texture.ActivateTexture(vao.shaderProgram, texture);

        spriteCoordVAO.Enable(1);
        super.Render(t, spriteCoordVAO.getVertices().length*2);
        spriteCoordVAO.Disable();
    }

    public void setTextureBuffer(String texturePath) {
        setTexture(ImageProcessor.ProcessImage(texturePath), new Vector2i(64,64));
    }

    public void setTexture(ByteBuffer texture, Vector2i size){
        setTexture(new Texture(texture, size), vao.getVertices());
    }

    public void setTexture(Texture texture, Vector2f[] textCoords) {
        this.texture = texture;
        spriteCoordVAO.setVertices(textCoords);
    }
}
