package org.JE.JE2.Rendering.Renderers;

import org.JE.JE2.Objects.Scripts.Transform;
import org.JE.JE2.Rendering.Renderers.VertexBuffers.VAO;
import org.JE.JE2.Rendering.Renderers.VertexBuffers.VAO2f;
import org.JE.JE2.Rendering.Texture;
import org.joml.Vector2f;

import static org.lwjgl.opengl.GL11C.GL_TRIANGLE_FAN;

public class TextureSegment extends RenderSegment{
    public static final VAO2f squareSpriteVAO = new VAO2f(new Vector2f[]{
            new Vector2f(0,0),
            new Vector2f(1,0),
            new Vector2f(1,1),
            new Vector2f(0,1)
    });

    private Texture texture;
    private Texture normal;

    private VAO2f vao2fRef; // To limit casting
    public TextureSegment(VAO2f vao, Transform relativeTransform, int drawMode, Texture texture, Texture normal) {
        super(vao, relativeTransform, drawMode);
        this.vao2fRef = vao;
        this.texture = texture;
        this.normal = normal;
    }


    public TextureSegment(Transform relativeTransform, Texture texture, Texture normal) {
        super(new VAO2f(new Vector2f[]{
                new Vector2f(0,0),
                new Vector2f(1,0),
                new Vector2f(1,1),
                new Vector2f(0,1)
        }), relativeTransform, GL_TRIANGLE_FAN);
        this.texture = texture;
        this.normal = normal;
    }

    public Texture getTexture() {
        return texture;
    }

    public Texture getNormal() {
        return normal;
    }

    public void setTexture(Texture texture) {
        this.texture = texture;
    }

    public void setNormal(Texture normal) {
        this.normal = normal;
    }

    public void disableTile(){
        customTile(new Vector2f(1,1));
        setScale(true);
    }

    public void customTile(Vector2f scale){
        vao2fRef.setVertices(new Vector2f[]{
                new Vector2f(0,0),
                new Vector2f(1,0),
                new Vector2f(1,1),
                new Vector2f(0,1),
                new Vector2f(0,0),
                new Vector2f(scale.x(),0),
                new Vector2f(scale.x(),scale.y()),
                new Vector2f(0,scale.y()),

        });
        setScale(false);
    }

    public VAO2f getVao2fRef() {
        return vao2fRef;
    }
}
