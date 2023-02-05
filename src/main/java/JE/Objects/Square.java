package JE.Objects;

import JE.IO.FileInput.ImageProcessor;
import JE.Objects.GameObject;
import JE.Rendering.Renderers.SpriteRenderer;
import JE.Rendering.Shaders.ShaderProgram;
import JE.Rendering.Texture;

import JE.UI.UIElements.Style.Color;
import org.joml.Vector2f;
import org.joml.Vector2i;

import static JE.IO.FileInput.ImageProcessor.generateSolidColorImage;

public class Square {
    public static GameObject Square(Vector2f pos, Vector2f scale, Color color){
        GameObject square = new GameObject();
        SpriteRenderer sr = new SpriteRenderer();
        Texture t = generateSolidColorImage(new Vector2i(64,64),color.getRGB(), "square");
        sr.setTexture(t);
        sr.setNormalTexture(new Texture(ImageProcessor.generateNormalMap(t)));

        square.addScript(new SpriteRenderer());
        return square;
    }
}
