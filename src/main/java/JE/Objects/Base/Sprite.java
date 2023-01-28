package JE.Objects.Base;

import JE.IO.FileInput.ImageProcessor;
import JE.Objects.Components.Base.ComponentRestrictions;
import JE.Rendering.Renderers.SpriteRenderer;
import JE.Rendering.Shaders.ShaderProgram;
import JE.Rendering.Texture;
import JE.Resources.Resource;
import JE.Resources.ResourceBundle;
import JE.Resources.ResourceType;
import JE.UI.UIElements.Style.Color;
import org.joml.Vector2f;

public class Sprite extends GameObject {

    protected SpriteRenderer sr;

    public Sprite(){
        super();
        addComponent(sr = new SpriteRenderer(ShaderProgram.spriteShader()));
        sr.setRestrictions(new ComponentRestrictions(false,true,false));
    }

    public Sprite(ShaderProgram shader){
        super();
        addComponent(sr = new SpriteRenderer(shader));
        sr.setRestrictions(new ComponentRestrictions(false,true,false));
    }

    public Sprite setShader(ShaderProgram sp){
        if(sr.getSpriteVAO().shaderProgram !=null)
            sr.getSpriteVAO().shaderProgram.destroy();

        if(sr.getVAO().shaderProgram !=null)
            sr.getVAO().shaderProgram.destroy();

        sr.getSpriteVAO().shaderProgram = sp;
        sr.getVAO().shaderProgram = sp;
        return this;
    }
    public Sprite setTexture(Texture t){
        sr.setTexture(t);
        if(sr.getNormalTexture().generatedTextureID <0)
            sr.setNormalTexture(new Texture(new Resource("",ImageProcessor.generateNormalMap(t), ResourceType.TEXTURE)));
        return this;
    }

    public Sprite setTexture(ResourceBundle t){
        if(sr.getNormalTexture().generatedTextureID <0)
            sr.setNormalTexture(new Texture(new Resource("",ImageProcessor.generateNormalMap(new Texture(t.imageData, t.imageSize)), ResourceType.TEXTURE)));
        return this;
    }

    public Sprite setNormalTexture(Texture t){
        sr.setNormalTexture(t);
        return this;
    }

    public Sprite setNormalTexture(ResourceBundle t){
        sr.setNormalTexture(new Texture(new Resource("",t, ResourceType.TEXTURE)));
        return this;
    }

    public void setVertices(Vector2f[] vertices){
        sr.getSpriteVAO().setVertices(vertices);
    }
    public void setBaseColor(Color color){
        sr.baseColor = color;
    }
    public void setDrawMode(int mode){
        sr.setDrawMode(mode);
    }
}
