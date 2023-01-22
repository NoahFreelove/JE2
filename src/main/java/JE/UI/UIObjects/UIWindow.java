package JE.UI.UIObjects;

import JE.Manager;
import JE.UI.UIElements.Style.Color;
import JE.UI.UIElements.UIElement;
import org.joml.Vector2f;
import org.lwjgl.nuklear.*;

import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;

import static JE.Window.UIHandler.ctx;
import static org.lwjgl.nuklear.Nuklear.*;
import static org.lwjgl.nuklear.Nuklear.nk_end;

public class UIWindow extends UIObject {
    private final NkContext context = ctx;
    public String name = "Window";
    public int windowOptions = NK_WINDOW_TITLE|NK_WINDOW_BORDER|NK_WINDOW_MINIMIZABLE|NK_WINDOW_SCALABLE|NK_WINDOW_MOVABLE|NK_WINDOW_CLOSABLE;
    private NkStyleWindow window;
    private NkRect rect = NkRect.create();
    // Will reset to this position upon showing window from a hidden state. definitely a feature.
    private Vector2f pos = new Vector2f(50,50);
    private Vector2f size = new Vector2f(200,200);
    public Color backgroundColor = Color.createColor(0.1f,0.1f,0.1f,1f);
    private boolean isMinimized = false;
    private boolean closedFromWindow = false;

    public boolean reset = false;

    public CopyOnWriteArrayList<UIElement> children = new CopyOnWriteArrayList<>();

    public UIWindow() {
        super();
        window = NkStyleWindow.create();
    }

    public UIWindow(String name) {
        this.name = name;
        window = NkStyleWindow.create();
    }

    public UIWindow(String name, int windowOptions) {
        this.name = name;
        this.windowOptions = windowOptions;
        window = NkStyleWindow.create();
    }

    public UIWindow(String name, int windowOptions, Vector2f pos) {
        this.name = name;
        this.windowOptions = windowOptions;
        this.pos = pos;
        window = NkStyleWindow.create();
    }

    public UIWindow(String name, int windowOptions, Vector2f pos, ArrayList<UIElement> children) {
        this.name = name;
        this.windowOptions = windowOptions;
        this.pos = pos;
        this.children.addAll(children);
        window = NkStyleWindow.create();
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

        if (nk_begin(context, name, nk_rect(pos.x, pos.y, size.x, size.y, rect), windowOptions)) {
            isCreated = true;
            closedFromWindow = false;
            // set background color

            window = context.style().window();
            window.fixed_background().data().color().set((byte) backgroundColor.r255(), (byte) backgroundColor.g255(), (byte) backgroundColor.b255(), (byte) backgroundColor.a255());


            children.forEach((uiElement -> {
                uiElement.requestRender();

            }));
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
