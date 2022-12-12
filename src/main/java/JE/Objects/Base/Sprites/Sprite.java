package JE.Objects.Base.Sprites;

import JE.IO.ImageProcessor;
import JE.Objects.Base.GameObject;
import JE.Objects.Components.Base.ComponentRestrictions;
import JE.Rendering.Shaders.BuiltIn.LightSprite.LightSpriteShader;
import JE.Rendering.Shaders.BuiltIn.SpriteShader;
import JE.Rendering.RenderTypes.SpriteRenderer;
import JE.Rendering.Shaders.ShaderProgram;
import JE.Rendering.Texture;
import JE.Rendering.VertexBuffers.VAO2f;
import JE.Resources.Resource;
import org.joml.Vector2f;
import org.joml.Vector2i;
import org.joml.Vector4f;

public class Sprite extends GameObject {

    protected SpriteRenderer sr;

    public Sprite(){
        super();
        addComponent(sr = new SpriteRenderer());
        sr.setRestrictions(new ComponentRestrictions(false,true,false));
    }
    public void setShader(ShaderProgram sp){
        sr.getSpriteVAO().shaderProgram = sp;
        sr.getVAO().shaderProgram = sp;
    }
    public void setTexture(Texture t){
        sr.setTexture(t);
    }
    public void setNormalTexture(Texture t){sr.setNormalTexture(t);}
    public void setVertices(Vector2f[] vertices){
        sr.getSpriteVAO().setVertices(vertices);
    }
    public void setBaseColor(Vector4f color){
        sr.baseColor = color;
    }
    public void setDrawMode(int mode){
        sr.setDrawMode(mode);
    }
}
