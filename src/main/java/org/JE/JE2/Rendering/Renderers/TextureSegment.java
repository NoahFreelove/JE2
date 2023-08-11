package org.JE.JE2.Rendering.Renderers;

import org.JE.JE2.Objects.Scripts.Transform;
import org.JE.JE2.Rendering.Renderers.VertexBuffers.VAO2f;
import org.JE.JE2.Rendering.Texture;
import org.joml.Vector2f;

import static org.lwjgl.opengl.GL11C.GL_TRIANGLE_FAN;

public class TextureSegment extends RenderSegment{
    public static final Vector2f[] squareSpritePoints = new Vector2f[]{
            new Vector2f(0,0),
            new Vector2f(1,0),
            new Vector2f(1,1),
            new Vector2f(0,1)
    };

    private Texture texture;
    private Texture normal;

    private Vector2f tileFactor = new Vector2f(1,1);

    private VAO2f coords;
    public TextureSegment(Vector2f[] vaoPoints, Transform relativeTransform, int drawMode, Texture texture, Texture normal) {
        super(new VAO2f(vaoPoints), relativeTransform, drawMode);
        this.coords = new VAO2f(vaoPoints);
        this.texture = texture;
        this.normal = normal;
        setAdditionalBufferSize(coords.getVertices().length*coords.getDataSize());
    }

    public TextureSegment(Vector2f[] vaoPoints, Vector2f[] normalPoints, Transform relativeTransform, int drawMode, Texture texture, Texture normal) {
        super(new VAO2f(vaoPoints), relativeTransform, drawMode);
        this.coords = new VAO2f(normalPoints);
        this.texture = texture;
        this.normal = normal;
        setAdditionalBufferSize(coords.getVertices().length*coords.getDataSize());
    }

    public TextureSegment(VAO2f vao, VAO2f normals, Transform relativeTransform, int drawMode, Texture texture, Texture normal) {
        super(vao, relativeTransform, drawMode);
        this.coords = normals;
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

    public TextureSegment(TextureSegment clone, Transform t) {
        super(clone.vao, t, GL_TRIANGLE_FAN);
        this.texture = clone.getTexture();
        this.normal = clone.getNormal();
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
        tileFactor = new Vector2f(1,1);
    }

    public void customTile(Vector2f scale){
        tileFactor = scale;
    }

    public VAO2f getCoords() {
        return coords;
    }

    @Override
    public void destroy() {
        super.destroy();
        if(refuseDestroy())
            return;
        getCoords().destroy();
    }

    public Vector2f getTileFactor() {
        return tileFactor;
    }
}
