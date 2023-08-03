package org.JE.JE2.Rendering.Renderers;

import org.JE.JE2.Annotations.GLThread;
import org.JE.JE2.Annotations.HideFromInspector;
import org.JE.JE2.Manager;
import org.JE.JE2.Objects.Scripts.Transform;
import org.JE.JE2.Rendering.Camera;
import org.JE.JE2.Rendering.Shaders.ShaderProgram;
import org.JE.JE2.Rendering.Texture;
import org.JE.JE2.Rendering.Renderers.VertexBuffers.VAO2f;
import org.joml.Vector2f;

import static org.lwjgl.opengl.GL11C.GL_TRIANGLE_FAN;
import static org.lwjgl.opengl.GL13C.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13C.GL_TEXTURE1;
import static org.lwjgl.opengl.GL20.glGetUniformLocation;
import static org.lwjgl.opengl.GL20.glUniform1i;

public class SpriteRenderer extends Renderer {
    TextureSegment[] textureSegments = new TextureSegment[0];

    public SpriteRenderer() {
        this(ShaderProgram.spriteShader());
    }

    public SpriteRenderer(ShaderProgram shader){
        VAO2f spriteCoordVAO = new VAO2f(new Vector2f[]{
                new Vector2f(0,0),
                new Vector2f(1,0),
                new Vector2f(1,1),
                new Vector2f(0,1)
        });
        shaderProgram = shader;
        textureSegments = new TextureSegment[1];
        textureSegments[0] = new TextureSegment(spriteCoordVAO, new Transform(), GL_TRIANGLE_FAN, new Texture(),new Texture());

    }

    @Override
    public void requestRender(Camera camera) {
        for (TextureSegment ts : textureSegments) {
            RenderTextureSegment(ts,camera);
        }
    }

    private void RenderTextureSegment(TextureSegment seg, Camera c){
        if(shaderProgram == null)
            return;
        if (!shaderProgram.use() || !getActive())
            return;

        if(seg.getTexture().activateTexture(GL_TEXTURE0)) {
            glUniform1i(glGetUniformLocation(shaderProgram.programID, "JE_Texture"), 0);
            shaderProgram.setUniform2f("JE_TextureSize", seg.getTexture().resource.getBundle().getImageSize().x, seg.getTexture().resource.getBundle().getImageSize().y);
        }

        if(seg.getTexture().activateTexture(GL_TEXTURE1))
            glUniform1i(glGetUniformLocation(shaderProgram.programID, "JE_Normal"), 1);

        seg.getVao().Enable(1); super.Render(seg,c); seg.getVao().Disable();
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

    public void setTexture(Texture texture){
        if(textureSegments.length == 1)
            setTexture(texture, textureSegments[0]);
    }

    public void setNormalTexture(Texture texture) {
        if(textureSegments.length == 1)
            setNormalTexture(texture, textureSegments[0]);
    }

    public void setNormalTexture(Texture texture, TextureSegment seg){
        seg.setNormal(texture);
    }

    public void setTexture(Texture texture, TextureSegment seg) {
        seg.setTexture(texture);
    }


    public void setSpriteVAO(VAO2f vao){
        textureSegments[0].setVao(vao);
    }

    public Texture getTexture(){ return textureSegments[0].getTexture(); }

    public Texture getNormalTexture(){ return textureSegments[0].getNormal(); }

    /*@Override
    public void load() {
        if(spriteCoordVAO !=null)
        {
            shaderProgram.presetIndex = defaultShaderIndex;
            vao = spriteCoordVAO;
            spriteCoordVAO.load();
            vao.load();
        }
        super.load();

        setTexture(Texture.checkExistElseCreate(textureName,-1,textureFilepath));
        setNormalTexture(Texture.checkExistElseCreate(normalTextureName,-1,normalFilepath));
    }*/





    /*public void invalidateTextures(){
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
    }*/

    public void invalidateShader(){
        setShaderProgram(ShaderProgram.invalidShader());
    }


    public TextureSegment[] getTextureSegments() {
        return textureSegments;
    }
}
