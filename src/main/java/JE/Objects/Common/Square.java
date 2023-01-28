package JE.Objects.Common;

import JE.IO.FileInput.ImageProcessor;
import JE.Objects.Base.Sprite;
import JE.Rendering.Shaders.ShaderProgram;
import JE.Rendering.Texture;
import JE.Resources.Resource;
import JE.Resources.ResourceType;
import JE.UI.UIElements.Style.Color;
import org.joml.Vector2i;

public class Square extends Sprite {
    private Vector2i size = new Vector2i(64,64);
    private Color color = Color.WHITE;
    public Square (){
        super(ShaderProgram.spriteShader());
        setColor(color);
        remakeTexture();
    }
    public Square setColor(Color c){
        this.color = c;
        remakeTexture();
        return this;
    }
    public Square setSize(Vector2i size){
        this.size = size;
        remakeTexture();
        return this;
    }

    private void remakeTexture(){
        Texture t = new Texture(new Resource("square", ImageProcessor.generateSolidColorImage(size, color.getRGB()), ResourceType.TEXTURE));
        setTexture(t);
        setNormalTexture(ImageProcessor.generateNormalMap(t));
    }
}
