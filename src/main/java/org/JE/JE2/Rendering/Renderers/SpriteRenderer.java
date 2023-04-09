package org.JE.JE2.Rendering.Renderers;

import org.JE.JE2.Annotations.GLThread;
import org.JE.JE2.Annotations.HideFromInspector;
import org.JE.JE2.Manager;
import org.JE.JE2.Objects.GameObject;
import org.JE.JE2.Rendering.Camera;
import org.JE.JE2.Rendering.Shaders.ShaderProgram;
import org.JE.JE2.Rendering.Texture;
import org.JE.JE2.Rendering.VertexBuffers.VAO2f;
import org.JE.JE2.Resources.DataLoader;
import org.joml.Vector2f;

import static org.lwjgl.opengl.GL13C.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13C.GL_TEXTURE1;
import static org.lwjgl.opengl.GL20.glGetUniformLocation;
import static org.lwjgl.opengl.GL20.glUniform1i;

public class SpriteRenderer extends Renderer {

    @HideFromInspector
    private final VAO2f spriteCoordVAO;

    private transient Texture texture = new Texture();
    private String textureFilepath = "";
    private transient Texture normal = new Texture();
    private String normalFilepath = "";


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

        if(texture.activateTexture(GL_TEXTURE0))
            glUniform1i(glGetUniformLocation(vao.getShaderProgram().programID, "JE_Texture"), 0);

        if(normal.activateTexture(GL_TEXTURE1))
            glUniform1i(glGetUniformLocation(vao.getShaderProgram().programID, "JE_Normal"), 1);

        spriteCoordVAO.Enable(1); super.Render(gameObject, 0, camera); spriteCoordVAO.Disable();
    }

    public void setTexture(Texture texture){
        setTexture(texture, spriteCoordVAO.getVertices(), true);
        //textureFp = texture.resource.bundle.filepath;
    }

    public void setNormalTexture(Texture texture) {
        this.normal = texture;
        //normalFp = texture.resource.bundle.filepath;
    }

    public void setTexture(Texture texture, Vector2f[] textCoords, boolean softSet) {
        this.texture = texture;
        if(softSet) return;
        Runnable r = () ->{
            spriteCoordVAO.setVertices(textCoords);
        };
        Manager.queueGLFunction(r);
    }

    public VAO2f getSpriteVAO(){return spriteCoordVAO;}

    public Texture getTexture(){ return texture; }

    public Texture getNormalTexture(){ return normal; }

    @Override
    public void load() {
        if(spriteCoordVAO !=null)
        {
            spriteCoordVAO.getShaderProgram().presetIndex = defaultShaderIndex;
            vao.getShaderProgram().presetIndex = defaultShaderIndex;
            vao = spriteCoordVAO;
            spriteCoordVAO.load();
            vao.load();
        }
        super.load();

        setTexture(Texture.checkExistElseCreate(textureFilepath,-1,textureFilepath));
        setNormalTexture(Texture.checkExistElseCreate(normalFilepath,-1,normalFilepath));
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

    public String getTextureFilepath() {
        return textureFilepath;
    }

    public String getNormalFilepath() {
        return normalFilepath;
    }
    public void invalidateTextures(){
        normal.valid = false;
        normal.forceValidateMode = 2;
        texture.valid = false;
        texture.forceValidateMode = 2;
    }
    public void forceValidateTextures(){
        normal.valid = true;
        normal.forceValidateMode = 1;
        texture.valid = true;
        texture.forceValidateMode = 1;
    }

    public void invalidateShader(){
        vao.setShaderProgram(ShaderProgram.invalidShader());
    }
}
