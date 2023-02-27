package org.JE.JE2.Objects;

import org.JE.JE2.IO.FileInput.ImageProcessor;
import org.JE.JE2.Rendering.Renderers.SpriteRenderer;
import org.JE.JE2.Rendering.Texture;

import org.JE.JE2.UI.UIElements.Style.Color;
import org.joml.Vector2f;
import org.joml.Vector2i;

import static org.JE.JE2.IO.FileInput.ImageProcessor.generateSolidColorImage;

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
