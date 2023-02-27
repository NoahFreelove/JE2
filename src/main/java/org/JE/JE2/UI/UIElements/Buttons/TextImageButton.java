package org.JE.JE2.UI.UIElements.Buttons;

import org.JE.JE2.IO.FileInput.ImageProcessor;
import org.JE.JE2.Rendering.Texture;
import org.JE.JE2.Resources.ResourceBundle;
import org.JE.JE2.UI.UIElements.UIElement;
import org.JE.JE2.Window.UIHandler;
import org.joml.Vector2f;
import org.lwjgl.nuklear.NkImage;

import static org.lwjgl.nuklear.Nuklear.*;

public class TextImageButton extends UIElement {
    public Runnable onClickEvent = () -> {};
    public NkImage img = NkImage.create();
    private ResourceBundle textureBundle;
    private Texture texture;
    private Vector2f dimensions = new Vector2f(1,1);
    public String text = "Button";

    public TextImageButton() {
        this.textureBundle = new ResourceBundle();
        this.texture = new Texture(textureBundle.imageData, textureBundle.imageSize);
    }

    public TextImageButton(String text, byte[] texture) {
        this.textureBundle = ImageProcessor.processImage(texture,false);
        this.texture = new Texture(textureBundle.imageData, textureBundle.imageSize);
        this.text = text;
    }

    public TextImageButton(String text, byte[] texture, Vector2f dimensions) {
        this.textureBundle = ImageProcessor.processImage(texture,false);
        this.texture = new Texture(textureBundle.imageData, textureBundle.imageSize);
        this.dimensions = dimensions;
        this.text = text;
    }

    public TextImageButton(String text, byte[] texture, Runnable onClickEvent) {
        this.textureBundle = ImageProcessor.processImage(texture,false);
        this.onClickEvent = onClickEvent;
        this.texture = new Texture(textureBundle.imageData, textureBundle.imageSize);
        this.text = text;
    }

    public TextImageButton(String text, byte[] texture, Runnable onClickEvent, Vector2f dimensions) {
        this.textureBundle = ImageProcessor.processImage(texture,false);
        this.onClickEvent = onClickEvent;
        this.texture = new Texture(textureBundle.imageData, textureBundle.imageSize);
        this.dimensions = dimensions;
        this.text = text;
    }

    @Override
    protected void render() {
        nk_image_id(texture.generatedTextureID,img);
        nk_layout_row_template_begin(UIHandler.nuklearContext, texture.resource.bundle.imageSize.y);
        nk_layout_row_template_push_static(UIHandler.nuklearContext, texture.resource.bundle.imageSize.x);
        nk_layout_row_template_end(UIHandler.nuklearContext);

        if(nk_button_image(UIHandler.nuklearContext, img)){
            if(isActive())
                onClickEvent.run();
        }
        nk_label(UIHandler.nuklearContext, text,NK_TEXT_ALIGN_CENTERED);
    }

    public TextImageButton setDimensions(Vector2f dim){
        this.dimensions = dim;
        return this;
    }
}
