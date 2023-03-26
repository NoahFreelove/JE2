package org.JE.JE2.UI.UIElements.Buttons;

import org.JE.JE2.IO.FileInput.ImageProcessor;
import org.JE.JE2.Rendering.Texture;
import org.JE.JE2.Resources.Bundles.ResourceBundle;
import org.JE.JE2.Resources.Bundles.TextureBundle;
import org.JE.JE2.UI.UIElements.UIElement;
import org.JE.JE2.Window.UIHandler;
import org.joml.Vector2f;
import org.lwjgl.nuklear.NkImage;
import org.lwjgl.nuklear.NkRect;

import static org.JE.JE2.Window.UIHandler.nuklearContext;
import static org.lwjgl.nuklear.Nuklear.*;

public class ImageButton extends UIElement {
    public Runnable onClickEvent = () -> {};
    public NkImage img = NkImage.create();
    private TextureBundle textureBundle;
    private Texture text;
    private Vector2f dimensions = new Vector2f(1,1);
    private NkRect rect = NkRect.create();
    public ImageButton() {
        this.textureBundle = new TextureBundle();
        this.text = new Texture(textureBundle.getImageData(), textureBundle.getImageSize());
    }

    public ImageButton(String filepath) {
        this.textureBundle = ImageProcessor.processImage(filepath,false);
        this.text = new Texture(textureBundle.getImageData(), textureBundle.getImageSize());
    }

    public ImageButton(byte[] text) {
        this.textureBundle = ImageProcessor.processImage(text,false);
        this.text = new Texture(textureBundle.getImageData(), textureBundle.getImageSize());
    }

    public ImageButton(String filepath, Vector2f dimensions) {
        this.textureBundle = ImageProcessor.processImage(filepath,false);
        this.text = new Texture(textureBundle.getImageData(), textureBundle.getImageSize());
        this.dimensions = dimensions;
    }

    public ImageButton(String filepath, Runnable onClickEvent) {
        this.textureBundle = ImageProcessor.processImage(filepath,false);
        this.onClickEvent = onClickEvent;
        this.text = new Texture(textureBundle.getImageData(), textureBundle.getImageSize());
    }
    public ImageButton(byte[] texture, Runnable onClickEvent) {
        this.textureBundle = ImageProcessor.processImage(texture,false);
        this.onClickEvent = onClickEvent;
        this.text = new Texture(textureBundle.getImageData(), textureBundle.getImageSize());
    }
    public ImageButton(String filepath, Runnable onClickEvent, Vector2f dimensions) {
        this.textureBundle = ImageProcessor.processImage(filepath,false);
        this.onClickEvent = onClickEvent;
        this.text = new Texture(textureBundle.getImageData(), textureBundle.getImageSize());
        this.dimensions = dimensions;
    }
    public ImageButton(byte[] texture, Runnable onClickEvent, Vector2f dimensions) {
        this.textureBundle = ImageProcessor.processImage(texture,false);
        this.onClickEvent = onClickEvent;
        this.text = new Texture(textureBundle.getImageData(), textureBundle.getImageSize());
        this.dimensions = dimensions;
    }

    @Override
    protected void render() {
        nk_image_id(text.generatedTextureID,img);
        nk_layout_row_template_begin(UIHandler.nuklearContext, text.resource.getTextureBundle().getImageSize().y);
        nk_layout_row_template_push_static(UIHandler.nuklearContext, text.resource.getTextureBundle().getImageSize().x);
        nk_layout_row_template_end(UIHandler.nuklearContext);

        if(nk_button_image(nuklearContext, img)) {
            if(isActive())
                onClickEvent.run();
        }

    }

    public ImageButton setDimensions(Vector2f dim){
        this.dimensions = dim;
        return this;
    }
}
