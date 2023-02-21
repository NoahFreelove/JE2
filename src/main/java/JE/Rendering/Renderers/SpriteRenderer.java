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

    private transient Texture texture = new Texture();
    private String textureFp;
    private transient Texture normal = new Texture();
    private String normalFp;

    public SpriteRenderer() {
        spriteCoordVAO = new VAO2f(new Vector2f[]{
                new Vector2f(0,0),
                new Vector2f(1,0),
                new Vector2f(1,1),
                new Vector2f(0,1)
        }, ShaderProgram.spriteShader());

    }

    public SpriteRenderer(ShaderProgram shader){
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
    public void Render(GameObject gameObject, int additionalBufferSize, Camera camera) {
        if (!vao.getShaderProgram().use())
            return;

        texture.activateTexture(GL_TEXTURE0);
        normal.activateTexture(GL_TEXTURE1);
        glUniform1i(glGetUniformLocation(vao.getShaderProgram().programID, "JE_Texture"), 0);
        glUniform1i(glGetUniformLocation(vao.getShaderProgram().programID, "JE_Normal"), 1);


        spriteCoordVAO.Enable(1);
        super.Render(gameObject, 0, camera);
        spriteCoordVAO.Disable();
    }


    public void setTexture(Texture texture){
        setTexture(texture, spriteCoordVAO.getVertices(), true);
        textureFp = texture.resource.bundle.filepath;

    }


    public void setNormalTexture(Texture texture) {
        this.normal = texture;
        normalFp = texture.resource.bundle.filepath;

    }

    private void setTexture(Texture texture, Vector2f[] textCoords, boolean softSet) {
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

    @Override
    public void load() {
        System.out.println("Loaded: " + textureFp);
        setTexture(new Texture(textureFp));
        setNormalTexture(new Texture(normalFp));
        if(spriteCoordVAO !=null)
            spriteCoordVAO.load();
        super.load();
    }

    public void customTile(Vector2f scale){
        spriteCoordVAO.setVertices(new Vector2f[]{
                new Vector2f(0,0),
                new Vector2f(1,0),
                new Vector2f(1,1),
                new Vector2f(0,1),
                new Vector2f(0,0),
                new Vector2f(scale.x(),0),
                new Vector2f(scale.x(),scale.y()),
                new Vector2f(0,scale.y()),

        });
        this.scale = false;
    }

    public void defaultTile(){
        customTile(getAttachedObject().getTransform().scale());
    }

    public void disableTile(){
        customTile(new Vector2f(1,1));
        this.scale = true;
    }
}
