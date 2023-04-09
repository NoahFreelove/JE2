package org.JE.JE2.UI.UIElements.Buttons;

import org.JE.JE2.Rendering.Texture;
import org.JE.JE2.Resources.Bundles.TextureBundle;
import org.JE.JE2.UI.UIElements.UIElement;
import org.JE.JE2.Window.UIHandler;
import org.joml.Vector2f;
import org.lwjgl.nuklear.NkImage;

import static org.lwjgl.nuklear.Nuklear.*;

public class TextImageButton extends UIElement {
    public Runnable onClickEvent = () -> {};
    public NkImage img = NkImage.create();
    private TextureBundle textureBundle;
    private Texture texture;
    private Vector2f dimensions = new Vector2f(1,1);
    public String text = "Button";

    /*public TextImageButton() {
        this.textureBundle = new TextureBundle();
        this.texture = new Texture(textureBundle.getImageData(), textureBundle.getImageSize());
    }

    public TextImageButton(String text, byte[] texture) {
        this.textureBundle = TextureProcessor.processImage(texture,false);
        this.texture = new Texture(textureBundle.getImageData(), textureBundle.getImageSize());
        this.text = text;
    }

    public TextImageButton(String text, byte[] texture, Vector2f dimensions) {
        this.textureBundle = TextureProcessor.processImage(texture,false);
        this.texture = new Texture(textureBundle.getImageData(), textureBundle.getImageSize());
        this.dimensions = dimensions;
        this.text = text;
    }

    public TextImageButton(String text, byte[] texture, Runnable onClickEvent) {
        this.textureBundle = TextureProcessor.processImage(texture,false);
        this.onClickEvent = onClickEvent;
        this.texture = new Texture(textureBundle.getImageData(), textureBundle.getImageSize());
        this.text = text;
    }*/


    @Override
    protected void render() {
        nk_image_id(texture.resource.getID(), img);
        nk_layout_row_template_begin(UIHandler.nuklearContext, texture.resource.getBundle().getImageSize().y);
        nk_layout_row_template_push_static(UIHandler.nuklearContext, texture.resource.getBundle().getImageSize().x);
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
