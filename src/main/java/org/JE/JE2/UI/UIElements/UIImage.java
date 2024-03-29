package org.JE.JE2.UI.UIElements;

import org.JE.JE2.Rendering.Texture;
import org.JE.JE2.Resources.Bundles.TextureBundle;
import org.JE.JE2.Resources.Resource;
import org.JE.JE2.UI.UIScaler;
import org.JE.JE2.Window.UIHandler;
import org.lwjgl.nuklear.NkImage;

import static org.lwjgl.nuklear.Nuklear.*;

public class UIImage extends UIElement {
    private Texture text = null;
    public NkImage img = NkImage.create();

    public UIImage() {
        super();
    }

    public UIImage(Texture text) {
        super();
        this.text = text;
    }

    @Override
    protected void render() {
        if(text == null)
            return;
        nk_image_id(text.resource.getID(),img);
        nk_image(UIHandler.nuklearContext, img);

    }
}
