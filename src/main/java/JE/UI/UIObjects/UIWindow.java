package JE.UI.UIObjects;

import JE.IO.FileInput.IOUtil;
import JE.IO.FileInput.ImageProcessor;
import JE.Manager;
import JE.Rendering.Texture;
import JE.Resources.ResourceBundle;
import JE.UI.UIElements.UIElement;
import JE.Window.UIHandler;
import org.joml.Vector2f;
import org.lwjgl.BufferUtils;
import org.lwjgl.nuklear.*;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;

import static org.lwjgl.nuklear.Nuklear.*;
import static org.lwjgl.nuklear.Nuklear.nk_end;

public class UIWindow extends UIObject {
    private final NkContext context = UIHandler.ctx;
    public String name = "Window";
    public int windowOptions = NK_WINDOW_TITLE|NK_WINDOW_BORDER|NK_WINDOW_MINIMIZABLE|NK_WINDOW_SCALABLE|NK_WINDOW_MOVABLE|NK_WINDOW_CLOSABLE;
    private NkRect rect;

    // Will reset to this position upon showing window from a hidden state. definitely a feature.
    private Vector2f pos = new Vector2f(50,50);
    private Vector2f size = new Vector2f(200,200);
    private boolean isMinimized = false;
    private boolean closedFromWindow = false;

    public boolean reset = false;

    NkImage nkImage = NkImage.create();
    ResourceBundle textureBundle = ImageProcessor.ProcessImage("bin/texture1.png",false);
    Texture text = new Texture(textureBundle.imageData, textureBundle.imageSize);

    public CopyOnWriteArrayList<UIElement> children = new CopyOnWriteArrayList<>();

    public UIWindow() {
        super();
        rect = NkRect.create();
    }

    public UIWindow(String name) {
        this.name = name;
        rect = NkRect.create();
    }

    public UIWindow(String name, int windowOptions) {
        this.name = name;
        this.windowOptions = windowOptions;
        rect = NkRect.create();
    }

    public UIWindow(String name, int windowOptions, Vector2f pos) {
        this.name = name;
        this.windowOptions = windowOptions;
        this.pos = pos;
        rect = NkRect.create();
    }

    public UIWindow(String name, int windowOptions, Vector2f pos, ArrayList<UIElement> children) {
        this.name = name;
        this.windowOptions = windowOptions;
        this.pos = pos;
        this.children.addAll(children);
        rect = NkRect.create();
    }

    @Override
    public void update() {

    }

    @Override
    public void render() {
        if(reset)
        {
            reset = false;
            return;
        }
        if(isCreated){
            if(!isAlive){
                Manager.activeScene().removeUI(this);
                return;
            }

            if(isVisible && nk_window_is_closed(context,name) && !closedFromWindow)
            {
                closedFromWindow = true;
                isVisible = false;
            }
            if(!isVisible){
                return;
            }
        }

        nk_image_id(text.generatedTextureID,nkImage);

        if (nk_begin(context, name, nk_rect(pos.x, pos.y, size.x, size.y, rect), windowOptions)) {
            isCreated = true;
            closedFromWindow = false;

            nk_layout_row_begin(context, NK_STATIC, 128,1);
            nk_layout_row_push(context, 128);
            nk_button_image(context, nkImage);

            //nk_group_end(context);

            nk_layout_row_dynamic(context, 30, 1);

            children.forEach(UIElement::requestRender);
        }

        nk_end(context);
    }

    public void closeWindow(){
        isAlive = false;
    }

    public void toggleVisibility(){
        closedFromWindow = true;
        isVisible = !isVisible;
    }
    public void toggleMinimize(){
        isMinimized = !isMinimized;
        nk_window_collapse(context,name, (isMinimized? 1 : 0));
    }

    public void setPos(Vector2f pos) {
        this.pos = pos;
        isCreated = false;
        reset = true;
    }

    public void setSize(Vector2f size) {
        this.size = size;
        isCreated = false;
        reset = true;
    }
}
