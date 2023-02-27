package org.JE.JE2.UI.UIElements;

import org.JE.JE2.IO.FileInput.ImageProcessor;
import org.JE.JE2.Rendering.Texture;
import org.JE.JE2.Resources.Resource;
import org.JE.JE2.Resources.ResourceType;
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
        setImage(text.resource.bundle.filepath);
    }
    public UIImage(String filepath)
    {
        super();
        setImage(filepath);
    }

    public void setImage(String filepath){
        this.text = new Texture(new Resource("texture", ImageProcessor.processImage(filepath, false), ResourceType.TEXTURE));
    }

    @Override
    protected void render() {
        if(text == null)
            return;
        nk_image_id(text.generatedTextureID,img);
        nk_layout_row_template_begin(UIHandler.nuklearContext, text.resource.bundle.imageSize.y);
        nk_layout_row_template_push_static(UIHandler.nuklearContext, text.resource.bundle.imageSize.x);
        nk_layout_row_template_end(UIHandler.nuklearContext);
        nk_image(UIHandler.nuklearContext, img);

    }
}
