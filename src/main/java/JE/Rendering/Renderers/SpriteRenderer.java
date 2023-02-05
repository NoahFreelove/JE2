package JE.Rendering.Renderers;

import JE.Annotations.ForceShowInInspector;
import JE.Annotations.GLThread;
import JE.Manager;
import JE.Objects.GameObject;
import JE.Rendering.Camera;
import JE.Rendering.Shaders.ShaderProgram;
import JE.Rendering.Texture;
import JE.Rendering.VertexBuffers.VAO2f;
import org.joml.Vector2f;
import org.joml.Vector2i;

import java.nio.ByteBuffer;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL12C.GL_CLAMP_TO_EDGE;
import static org.lwjgl.opengl.GL13C.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13C.GL_TEXTURE1;
import static org.lwjgl.opengl.GL20.glGetUniformLocation;
import static org.lwjgl.opengl.GL20.glUniform1i;

public class SpriteRenderer extends Renderer {
    private final VAO2f spriteCoordVAO;
    public boolean tile = false;

    @ForceShowInInspector
    private Texture texture = new Texture();

    //@ForceShowInInspector
    private Texture normal = new Texture();

    public SpriteRenderer() {
        spriteCoordVAO = new VAO2f(new Vector2f[]{
                new Vector2f(0,0),
                new Vector2f(1,0),
                new Vector2f(1,1),
                new Vector2f(0,1)
        }, ShaderProgram.spriteShader());
    }

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
        Render(gameObject,additionalBufferSize,Manager.getMainCamera());
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
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, (tile ? GL_REPEAT : GL_CLAMP_TO_EDGE));
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, (tile ? GL_REPEAT : GL_CLAMP_TO_EDGE));
        // if we tile, the coordinates are in the range of 0-1, so we need to multiply them by the texture size


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
