package org.JE.JE2.Rendering.Renderers;

import org.JE.JE2.Objects.Scripts.Transform;
import org.JE.JE2.Rendering.Camera;
import org.JE.JE2.Rendering.Shaders.ShaderProgram;
import org.JE.JE2.Rendering.Texture;
import org.JE.JE2.Rendering.Renderers.VertexBuffers.VAO2f;
import org.joml.Vector2f;
import org.joml.Vector2i;

import static org.lwjgl.opengl.GL11C.GL_TRIANGLE_FAN;
import static org.lwjgl.opengl.GL13C.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13C.GL_TEXTURE1;
import static org.lwjgl.opengl.GL20.glGetUniformLocation;
import static org.lwjgl.opengl.GL20.glUniform1i;

public class SpriteRenderer extends Renderer {
    private TextureSegment[] textureSegments;

    public SpriteRenderer() {
        this(ShaderProgram.spriteShader());
        textureSegments[0] = new TextureSegment(TextureSegment.squareSpritePoints, new Transform(), GL_TRIANGLE_FAN, new Texture(),new Texture());
    }

    public SpriteRenderer(ShaderProgram shader){

        shaderProgram = shader;
        textureSegments = new TextureSegment[1];
        textureSegments[0] = new TextureSegment(TextureSegment.squareSpritePoints, new Transform(), GL_TRIANGLE_FAN, new Texture(),new Texture());
    }

    @Override
    public void requestRender(Transform t, Camera camera) {
        for (TextureSegment ts : textureSegments) {
            if(!ts.isActive())
                continue;
            RenderTextureSegment(ts,t,camera);
        }
    }

    protected void RenderTextureSegment(TextureSegment seg, Transform t, Camera c){
        if(shaderProgram == null)
            return;
        if (!shaderProgram.use() || !getActive())
            return;

        if(debug)
            System.out.println("passed shader check");

        if(seg.getTexture().activateTexture(GL_TEXTURE0)) {
            shaderProgram.JE_Texture.setValue(0);
            //glUniform1i(glGetUniformLocation(shaderProgram.programID, "JE_Texture"), 0);

            Vector2i size = seg.getTexture().resource.getBundle().getImageSize();
            shaderProgram.texture_size.setValue(size.x, size.y);
        }

        if(seg.getTexture().activateTexture(GL_TEXTURE1)) {
            shaderProgram.JE_Normal.setValue(1);
            //glUniform1i(glGetUniformLocation(shaderProgram.programID, "JE_Normal"), 0);

            Vector2i size = seg.getNormal().resource.getBundle().getImageSize();
            shaderProgram.normal_texture_size.setValue(size.x, size.y);
        }

        shaderProgram.tile_factor.setValue(seg.getTileFactor());

        if(debug)
            System.out.println("passed texture check");

        seg.getCoords().Enable(1);
        super.Render(seg,t,c);
        seg.getTexture().unbindTexture(GL_TEXTURE0);
        seg.getTexture().unbindTexture(GL_TEXTURE1);

        seg.getCoords().Disable();
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
        renderSegments[0].setVao(vao);
    }

    public Texture getTexture(){ return textureSegments[0].getTexture(); }

    public Texture getNormalTexture(){ return textureSegments[0].getNormal(); }

    public void invalidateShader(){
        setShaderProgram(ShaderProgram.invalidShader());
    }


    public TextureSegment[] getTextureSegments() {
        return textureSegments;
    }

    public TextureSegment getTextureSegment(int index) {
        return textureSegments[index];
    }

    public TextureSegment getTextureSegment(){
        return textureSegments[0];
    }

    @Override
    public void destroy() {
        super.destroy();
        for (TextureSegment ts : textureSegments) {
            ts.destroy();
        }
    }

    public void addTextureSegment(TextureSegment ts) {
        TextureSegment[] temp = new TextureSegment[textureSegments.length + 1];
        for (int i = 0; i < textureSegments.length; i++) {
            temp[i] = textureSegments[i];
        }
        temp[temp.length - 1] = ts;
        textureSegments = temp;
    }

}
