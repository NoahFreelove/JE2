package JE.Objects.Base.Sprites;

import JE.Objects.Base.GameObject;
import JE.Objects.Components.Base.ComponentRestrictions;
import JE.Rendering.Shaders.BuiltIn.SpriteShader;
import JE.Rendering.Renderers.SpriteRenderer;
import JE.Rendering.Shaders.ShaderProgram;
import JE.Rendering.Texture;
import org.joml.Vector2f;
import org.joml.Vector4f;

public class Sprite extends GameObject {

    protected SpriteRenderer sr;

    public Sprite(){
        super();
        addComponent(sr = new SpriteRenderer());
        sr.setRestrictions(new ComponentRestrictions(false,true,false));
        setShader(new SpriteShader());
    }
    public Sprite setShader(ShaderProgram sp){
        sr.getSpriteVAO().shaderProgram = sp;
        sr.getVAO().shaderProgram = sp;
        return this;
    }
    public Sprite setTexture(Texture t){
        sr.setTexture(t);
        return this;
    }
    public Sprite setNormalTexture(Texture t){
        sr.setNormalTexture(t);
        return this;
    }
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
