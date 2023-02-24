package JE.UI.UIElements.Buttons;

import JE.IO.FileInput.ImageProcessor;
import JE.Rendering.Texture;
import JE.Resources.ResourceBundle;
import JE.UI.UIElements.UIElement;
import JE.Window.UIHandler;
import org.joml.Vector2f;
import org.lwjgl.nuklear.NkImage;
import org.lwjgl.nuklear.NkRect;

import static JE.Window.UIHandler.nuklearContext;
import static org.lwjgl.nuklear.Nuklear.*;

public class ImageButton extends UIElement {
    public Runnable onClickEvent = () -> {};
    public NkImage img = NkImage.create();
    private ResourceBundle textureBundle;
    private Texture text;
    private Vector2f dimensions = new Vector2f(1,1);
    private NkRect rect = NkRect.create();
    public ImageButton() {
        this.textureBundle = new ResourceBundle();
        this.text = new Texture(textureBundle.imageData, textureBundle.imageSize);
    }

    public ImageButton(String filepath) {
        this.textureBundle = ImageProcessor.processImage(filepath,false);
        this.text = new Texture(textureBundle.imageData, textureBundle.imageSize);
    }

    public ImageButton(byte[] text) {
        this.textureBundle = ImageProcessor.processImage(text,false);
        this.text = new Texture(textureBundle.imageData, textureBundle.imageSize);
    }

    public ImageButton(String filepath, Vector2f dimensions) {
        this.textureBundle = ImageProcessor.processImage(filepath,false);
        this.text = new Texture(textureBundle.imageData, textureBundle.imageSize);
        this.dimensions = dimensions;
    }

    public ImageButton(String filepath, Runnable onClickEvent) {
        this.textureBundle = ImageProcessor.processImage(filepath,false);
        this.onClickEvent = onClickEvent;
        this.text = new Texture(textureBundle.imageData, textureBundle.imageSize);
    }
    public ImageButton(byte[] texture, Runnable onClickEvent) {
        this.textureBundle = ImageProcessor.processImage(texture,false);
        this.onClickEvent = onClickEvent;
        this.text = new Texture(textureBundle.imageData, textureBundle.imageSize);
    }
    public ImageButton(String filepath, Runnable onClickEvent, Vector2f dimensions) {
        this.textureBundle = ImageProcessor.processImage(filepath,false);
        this.onClickEvent = onClickEvent;
        this.text = new Texture(textureBundle.imageData, textureBundle.imageSize);
        this.dimensions = dimensions;
    }
    public ImageButton(byte[] texture, Runnable onClickEvent, Vector2f dimensions) {
        this.textureBundle = ImageProcessor.processImage(texture,false);
        this.onClickEvent = onClickEvent;
        this.text = new Texture(textureBundle.imageData, textureBundle.imageSize);
        this.dimensions = dimensions;
    }

    @Override
    protected void render() {
        nk_image_id(text.generatedTextureID,img);
        nk_layout_row_template_begin(UIHandler.nuklearContext, text.resource.bundle.imageSize.y);
        nk_layout_row_template_push_static(UIHandler.nuklearContext, text.resource.bundle.imageSize.x);
        nk_layout_row_template_end(UIHandler.nuklearContext);

        if(nk_button_image(nuklearContext, img)) {
            if(isActive())
                onClickEvent.run();
        }

    }

    public ImageButton setDimensions(Vector2f dim){
        this.dimensions = dim;
        return this;
    }
}
