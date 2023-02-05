package JE.UI.UIElements;

import JE.IO.FileInput.ImageProcessor;
import JE.Rendering.Texture;
import JE.Resources.Resource;
import JE.Resources.ResourceType;
import JE.Window.UIHandler;
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
        this.text = new Texture(new Resource("texture", ImageProcessor.ProcessImage(filepath, false), ResourceType.TEXTURE));
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
