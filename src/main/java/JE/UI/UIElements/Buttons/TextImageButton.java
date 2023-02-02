package JE.UI.UIElements.Buttons;

import JE.IO.FileInput.ImageProcessor;
import JE.Rendering.Texture;
import JE.Resources.ResourceBundle;
import JE.UI.UIElements.UIElement;
import JE.Window.UIHandler;
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

    public TextImageButton(String text, String filepath) {
        this.textureBundle = ImageProcessor.ProcessImage(filepath,false);
        this.texture = new Texture(textureBundle.imageData, textureBundle.imageSize);
        this.text = text;
    }

    public TextImageButton(String text, String filepath, Vector2f dimensions) {
        this.textureBundle = ImageProcessor.ProcessImage(filepath,false);
        this.texture = new Texture(textureBundle.imageData, textureBundle.imageSize);
        this.dimensions = dimensions;
        this.text = text;
    }

    public TextImageButton(String text, String filepath, Runnable onClickEvent) {
        this.textureBundle = ImageProcessor.ProcessImage(filepath,false);
        this.onClickEvent = onClickEvent;
        this.texture = new Texture(textureBundle.imageData, textureBundle.imageSize);
        this.text = text;
    }

    public TextImageButton(String text, String filepath, Runnable onClickEvent, Vector2f dimensions) {
        this.textureBundle = ImageProcessor.ProcessImage(filepath,false);
        this.onClickEvent = onClickEvent;
        this.texture = new Texture(textureBundle.imageData, textureBundle.imageSize);
        this.dimensions = dimensions;
        this.text = text;
    }

    @Override
    protected void render() {
        nk_image_id(texture.generatedTextureID,img);
        nk_layout_row_template_begin(UIHandler.ctx, texture.resource.bundle.imageSize.y);
        nk_layout_row_template_push_static(UIHandler.ctx, texture.resource.bundle.imageSize.x);
        nk_layout_row_template_end(UIHandler.ctx);

        if(nk_button_image(UIHandler.ctx, img)){
            if(isActive())
                onClickEvent.run();
        }
        nk_label(UIHandler.ctx, text,NK_TEXT_ALIGN_CENTERED);
    }

    public TextImageButton setDimensions(Vector2f dim){
        this.dimensions = dim;
        return this;
    }
}
