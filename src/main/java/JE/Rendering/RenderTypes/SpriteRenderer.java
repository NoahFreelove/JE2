package JE.Rendering.RenderTypes;

import JE.Annotations.GLThread;
import JE.Manager;
import JE.Objects.Components.Common.Transform;
import JE.Rendering.Shaders.BuiltIn.SpriteShader;
import JE.Rendering.Shaders.ShaderProgram;
import JE.Rendering.Texture;
import JE.Rendering.VertexBuffers.VAO2f;
import org.joml.Vector2f;
import org.joml.Vector2i;

import java.nio.ByteBuffer;

public class SpriteRenderer extends Renderer {
    private final VAO2f spriteCoordVAO;
    private Texture texture = new Texture();

    public SpriteRenderer(){
        super();
        spriteCoordVAO = new VAO2f(new Vector2f[]{
                new Vector2f(0,0),
                new Vector2f(1,0),
                new Vector2f(1,1),
                new Vector2f(0,1)
        }, new ShaderProgram());
        vao = spriteCoordVAO;


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
        if(texture.generatedTextureID >=0) {
            texture.activateTexture(vao.shaderProgram);
        }

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
    public VAO2f getSpriteVAO(){return spriteCoordVAO;}
}
