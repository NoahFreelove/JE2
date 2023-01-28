package JE.Rendering.Renderers;

import JE.Annotations.GLThread;
import JE.Logging.Errors.ShaderError;
import JE.Logging.Logger;
import JE.Manager;
import JE.Objects.Base.GameObject;
import JE.Objects.Components.Common.Transform;
import JE.Rendering.Camera;
import JE.Rendering.Shaders.ShaderProgram;
import JE.Rendering.Texture;
import JE.Rendering.VertexBuffers.VAO2f;
import org.joml.Vector2f;
import org.joml.Vector2i;

import java.nio.ByteBuffer;

import static org.lwjgl.opengl.GL13C.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13C.GL_TEXTURE1;
import static org.lwjgl.opengl.GL20.glGetUniformLocation;
import static org.lwjgl.opengl.GL20.glUniform1i;

public class SpriteRenderer extends Renderer {
    private final VAO2f spriteCoordVAO;
    private Texture texture = new Texture();
    private Texture normal = new Texture();

    public SpriteRenderer(ShaderProgram shader){
        super();
        spriteCoordVAO = new VAO2f(new Vector2f[]{
                new Vector2f(0,0),
                new Vector2f(1,0),
                new Vector2f(1,1),
                new Vector2f(0,1)
        }, shader);
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
    public void Render(GameObject gameObject) {
        Render(gameObject, 0);
    }

    @Override
    @GLThread
    public void Render(GameObject gameObject, int additionalBufferSize) {
        Render(gameObject,additionalBufferSize,Manager.getCamera());
    }

    @Override
    @GLThread
    public void Render(GameObject gameObject, int additionalBufferSize, Camera camera){
        if(!vao.shaderProgram.use())
            return;
        texture.activateTexture(GL_TEXTURE0);
        normal.activateTexture(GL_TEXTURE1);
        glUniform1i(glGetUniformLocation(vao.shaderProgram.programID, "JE_Texture"), 0);
        glUniform1i(glGetUniformLocation(vao.shaderProgram.programID, "JE_Normal"), 1);

        spriteCoordVAO.Enable(1);
        super.Render(gameObject, spriteCoordVAO.getVertices().length*2+additionalBufferSize, camera);
        spriteCoordVAO.Disable();
    }


    public void setTexture(Texture texture){
        setTexture(texture, spriteCoordVAO.getVertices(), true);
    }

    public void setTexture(ByteBuffer texture, Vector2i size){
        setTexture(new Texture(texture, size), spriteCoordVAO.getVertices(), false);
    }

    public void setNormalTexture(Texture texture) {
        this.normal = texture;
    }

    public void setTexture(Texture texture, Vector2f[] textCoords, boolean softSet) {
        this.texture = texture;
        Runnable r = () ->{
            if(softSet) return;
            spriteCoordVAO.setVertices(textCoords);
        };
        Manager.queueGLFunction(r);
    }
    public VAO2f getSpriteVAO(){return spriteCoordVAO;}

    public Texture getTexture(){ return texture; }
    public Texture getNormalTexture(){ return normal; }

}
