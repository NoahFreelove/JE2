package org.JE.JE2.Rendering.Renderers;

import org.JE.JE2.IO.Filepath;
import org.JE.JE2.Objects.Scripts.Serialize.Load;
import org.JE.JE2.Objects.Scripts.Serialize.Save;
import org.JE.JE2.Objects.Scripts.Transform;
import org.JE.JE2.Rendering.Camera;
import org.JE.JE2.Rendering.Renderers.VertexBuffers.VAO2f;
import org.JE.JE2.Rendering.Shaders.ShaderProgram;
import org.JE.JE2.Rendering.Texture;
import org.joml.Vector2f;
import org.joml.Vector2i;

import java.util.HashMap;

import static org.lwjgl.opengl.GL11C.GL_TRIANGLE_FAN;
import static org.lwjgl.opengl.GL13C.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13C.GL_TEXTURE1;

public class SpriteRenderer extends Renderer implements Save, Load {
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

    @Override
    public RenderSegment[] getRenderSegments() {
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

    @Override
    public void load(HashMap<String, String> data) {
        // Need to load the Texture Segments, Its VAO points, its relative transform, texture, and normal texture, and tile factor and draw mode
        // In Future material and shader program
        textureSegments = new TextureSegment[0];
        int i = 0;
        while (data.containsKey("TextureSegment" + i + ":VAO")) {
            VAO2f vao = new VAO2f();
            vao.setVertices(VAO2f.stringToVerts(data.get("TextureSegment" + i + ":VAO")));
            VAO2f coords = new VAO2f();
            coords.setVertices(VAO2f.stringToVerts(data.get("TextureSegment" + i + ":COORDS")));

            Transform relativeTransform = new Transform();
            relativeTransform.simpleDeserialize(data.get("TextureSegment" + i + ":RELATIVE_TRANSFORM"));
            Transform animationTransform = new Transform();
            animationTransform.simpleDeserialize(data.get("TextureSegment" + i + ":ANIMATION_TRANSFORM"));
            String[] textureData = data.get("TextureSegment" + i + ":TEXTURE").split(":");
            Texture texture = Texture.checkExistElseCreate(textureData[0],-1, new Filepath(textureData[1],true));
            String[] normalData = data.get("TextureSegment" + i + ":NORMAL").split(":");
            Texture normal = Texture.checkExistElseCreate(normalData[0],-1, new Filepath(normalData[1],true));
            String[] tileFactorData = data.get("TextureSegment" + i + ":TILE_FACTOR").split(":");
            Vector2f tileFactor = new Vector2f(Float.parseFloat(tileFactorData[0]), Float.parseFloat(tileFactorData[1]));

            int drawMode = Integer.parseInt(data.get("TextureSegment" + i + ":DRAW_MODE"));
            TextureSegment ts = new TextureSegment(vao, coords, relativeTransform, drawMode, texture, normal);
            ts.customTile(tileFactor);
            addTextureSegment(ts);
            i++;
        }
    }

    @Override
    public HashMap<String, String> save() {
        HashMap<String, String> data = new HashMap<>();
        // Need to save the Texture Segments, Its VAO points, its relative transform, texture, and normal texture, and tile factor and draw mode
        // In Future material and shader program
        int i = 0;
        for (TextureSegment ts : textureSegments) {
            data.put("TextureSegment" + i + ":VAO", ts.getVao().vertsToString());
            data.put("TextureSegment" + i + ":COORDS", ts.getCoords().vertsToString());
            data.put("TextureSegment" + i + ":RELATIVE_TRANSFORM", ts.getRelativeTransform().simpleSerialize());
            data.put("TextureSegment" + i + ":ANIMATION_TRANSFORM", ts.getAnimationTransform().simpleSerialize());
            data.put("TextureSegment" + i + ":TEXTURE", ts.getTexture().resource.getName() + ":" + ts.getTexture().resource.getBundle().tryGetFilepath().getPath(true));
            data.put("TextureSegment" + i + ":NORMAL", ts.getTexture().resource.getName() + ":" + ts.getNormal().resource.getBundle().tryGetFilepath().getPath(true));
            data.put("TextureSegment" + i + ":TILE_FACTOR", ts.getTileFactor().x() + ":" + ts.getTileFactor().y());
            data.put("TextureSegment" + i + ":DRAW_MODE", ts.getDrawMode() + "");
            i++;
        }

        return data;
    }
}
