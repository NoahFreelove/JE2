package JE.Rendering.RenderTypes;

import JE.Annotations.GLThread;
import JE.IO.ImageProcessor;
import JE.Manager;
import JE.Objects.Components.Common.Transform;
import JE.Rendering.Texture;
import JE.Rendering.VertexBuffers.VAO2f;
import org.joml.Vector2f;
import org.joml.Vector2i;

import java.nio.ByteBuffer;

public class SpriteRenderer extends Renderer {
    private VAO2f spriteCoordVAO;
    public Texture texture = new Texture();

    public SpriteRenderer(){
        spriteCoordVAO = new VAO2f(new Vector2f[]{}, vao.shaderProgram);
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
    public void Render(Transform t) {
        Render(t, 0);
    }

    @Override
    @GLThread
    public void Render(Transform t, int additionalBufferSize) {
        if(texture.generatedTextureID <=-1) {
            super.Render(t, spriteCoordVAO.getVertices().length*2+additionalBufferSize);
            return;
        }

        texture.activateTexture(vao.shaderProgram);
        spriteCoordVAO.Enable(1);
        super.Render(t, spriteCoordVAO.getVertices().length*2+additionalBufferSize);
        spriteCoordVAO.Disable();
    }


    public void setTexture(Texture texture, boolean softSet){
        setTexture(texture, spriteCoordVAO.getVertices(), softSet);
    }

    public void setTexture(ByteBuffer texture, Vector2i size){
        setTexture(new Texture(texture, size), spriteCoordVAO.getVertices(), false);
    }

    public void setTexture(Texture texture, Vector2f[] textCoords, boolean softSet) {
        Runnable r = () ->{
            this.texture = texture;
            if(softSet) return;
            spriteCoordVAO.setVertices(textCoords);
        };
        Manager.queueGLFunction(r);
    }
}
