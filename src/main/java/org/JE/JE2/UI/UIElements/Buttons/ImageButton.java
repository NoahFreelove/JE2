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

    public int sizeX;
    public int sizeY;
    public static NkImage empty = NkImage.create();

    public ImageButton(Texture texture) {
        super();
        this.texture = texture;
        sizeX = texture.resource.getBundle().getImageSize().x();
        sizeY = texture.resource.getBundle().getImageSize().y();
    }

    public ImageButton(Texture texture, Runnable onClickEvent) {
        super();
        this.texture = texture;
        this.onClickEvent = onClickEvent;
        sizeX = texture.resource.getBundle().getImageSize().x();
        sizeY = texture.resource.getBundle().getImageSize().y();
    }

    public void setTexture(Texture texture) {
        this.texture = texture;
    }

    @Override
    protected void render() {
        nk_image_id(texture.resource.getID(), img);
        nuklearContext.style().button().normal().data().color().set(style.normalColor.nkColor());
        nuklearContext.style().button().hover().data().color().set(style.hoverColor.nkColor());
        nuklearContext.style().button().active().data().color().set(style.pressedColor.nkColor());

        if (nk_button_image(nuklearContext, img)) {
            if (isActive())
                onClickEvent.run();
        }

        nuklearContext.style().clear();

    }
}
