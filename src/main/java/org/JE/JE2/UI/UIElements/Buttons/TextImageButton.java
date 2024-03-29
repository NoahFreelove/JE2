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
    private Texture texture;
    public String text = "Button";

    public TextImageButton(Texture texture, Runnable onClickEvent) {
        super();
        this.texture = texture;
        this.onClickEvent = onClickEvent;
    }

    public TextImageButton(Texture texture, String text) {
        super();
        this.texture = texture;
        this.text = text;
    }

    public TextImageButton(Texture texture, Runnable onClickEvent, String text) {
        super();
        this.texture = texture;
        this.onClickEvent = onClickEvent;
        this.text = text;
    }

    @Override
    protected void render() {
        nk_image_id(texture.resource.getID(), img);

        if(nk_button_image(UIHandler.nuklearContext, img)){
            if(isActive())
                onClickEvent.run();
        }
        nk_label(UIHandler.nuklearContext, text,NK_TEXT_ALIGN_CENTERED);
    }
}
