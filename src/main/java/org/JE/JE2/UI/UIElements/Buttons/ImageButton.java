package org.JE.JE2.UI.UIElements.Buttons;

import org.JE.JE2.Rendering.Texture;
import org.JE.JE2.Resources.Bundles.TextureBundle;
import org.JE.JE2.Resources.Resource;
import org.JE.JE2.UI.UIElements.UIElement;
import org.JE.JE2.UI.UIScaler;
import org.JE.JE2.Window.UIHandler;
import org.joml.Vector2f;
import org.lwjgl.nuklear.NkImage;
import org.lwjgl.nuklear.NkRect;

import static org.JE.JE2.Window.UIHandler.nuklearContext;
import static org.lwjgl.nuklear.Nuklear.*;

public class ImageButton extends UIElement {
    public Runnable onClickEvent = () -> {};
    public NkImage img = NkImage.create();
    private Texture texture;
    private NkRect rect = NkRect.create();

    public ImageButton(Texture texture) {
        super();
        this.texture = texture;
    }

    public ImageButton(Texture texture, Runnable onClickEvent) {
        super();
        this.texture = texture;
        this.onClickEvent = onClickEvent;
    }

    @Override
    protected void render() {
        nk_image_id(texture.resource.getID(), img);
        nk_layout_row_template_begin(UIHandler.nuklearContext, texture.resource.getBundle().getImageSize().y* UIScaler.MULTIPLIERY);
        nk_layout_row_template_push_static(UIHandler.nuklearContext, texture.resource.getBundle().getImageSize().x* UIScaler.MULTIPLIERX);
        nk_layout_row_template_end(UIHandler.nuklearContext);

        if (nk_button_image(nuklearContext, img)) {
            if (isActive())
                onClickEvent.run();
        }
    }
}
