package org.JE.JE2.UI.UIElements.Buttons;

import org.JE.JE2.Rendering.Texture;
import org.JE.JE2.Resources.Bundles.TextureBundle;
import org.JE.JE2.Resources.Resource;
import org.JE.JE2.UI.UIElements.UIElement;
import org.JE.JE2.Window.UIHandler;
import org.joml.Vector2f;
import org.lwjgl.nuklear.NkImage;

import static org.lwjgl.nuklear.Nuklear.*;

public class TextImageButton extends UIElement {
    public Runnable onClickEvent = () -> {};
    public NkImage img = NkImage.create();
    private Resource<TextureBundle> texture;
    public String text = "Button";

    public TextImageButton(Resource<TextureBundle> texture, Runnable onClickEvent) {
        super();
        this.texture = texture;
        this.onClickEvent = onClickEvent;
    }

    public TextImageButton(Resource<TextureBundle> texture, String text) {
        super();
        this.texture = texture;
        this.text = text;
    }

    public TextImageButton(Resource<TextureBundle> texture, Runnable onClickEvent, String text) {
        super();
        this.texture = texture;
        this.onClickEvent = onClickEvent;
        this.text = text;
    }

    @Override
    protected void render() {
        nk_image_id(texture.getID(), img);
        nk_layout_row_template_begin(UIHandler.nuklearContext, texture.getBundle().getImageSize().y);
        nk_layout_row_template_push_static(UIHandler.nuklearContext, texture.getBundle().getImageSize().x);
        nk_layout_row_template_end(UIHandler.nuklearContext);

        if(nk_button_image(UIHandler.nuklearContext, img)){
            if(isActive())
                onClickEvent.run();
        }
        nk_label(UIHandler.nuklearContext, text,NK_TEXT_ALIGN_CENTERED);
    }
}
