package JE.UI.UIObjects;

import JE.Manager;
import JE.UI.UIElements.Element_UI;
import JE.Window.UIHandler;
import org.joml.Vector2i;
import org.lwjgl.nuklear.NkContext;
import org.lwjgl.nuklear.NkRect;

import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;

import static org.lwjgl.nuklear.Nuklear.*;
import static org.lwjgl.nuklear.Nuklear.nk_end;

public class Window_UI extends Object_UI {
    private final NkContext context = UIHandler.ctx;
    public String name = "Window";
    public int windowOptions = NK_WINDOW_TITLE|NK_WINDOW_BORDER|NK_WINDOW_MINIMIZABLE|NK_WINDOW_SCALABLE|NK_WINDOW_MOVABLE|NK_WINDOW_CLOSABLE;
    private NkRect rect;

    // Will reset to this position upon showing window from a hidden state. definitely a feature.
    public Vector2i homePosition = new Vector2i(50,50);
    private boolean isMinimized = false;

    private boolean closedFromWindow = false;

    public CopyOnWriteArrayList<Element_UI> children = new CopyOnWriteArrayList<>();

    public Window_UI() {
        super();
        rect = NkRect.create();
    }

    public Window_UI(String name) {
        this.name = name;
        rect = NkRect.create();
    }

    public Window_UI(String name, int windowOptions) {
        this.name = name;
        this.windowOptions = windowOptions;
        rect = NkRect.create();
    }

    public Window_UI(String name, int windowOptions, Vector2i pos) {
        this.name = name;
        this.windowOptions = windowOptions;
        this.homePosition = pos;
        rect = NkRect.create();
    }

    public Window_UI(String name, int windowOptions, Vector2i pos, ArrayList<Element_UI> children) {
        this.name = name;
        this.windowOptions = windowOptions;
        this.homePosition = pos;
        this.children.addAll(children);
        rect = NkRect.create();
    }

    @Override
    public void update() {

    }

    @Override
    public void render() {
        /*System.out.println("Visible:" + isVisible);
        System.out.println("ClosedFrom:" + closedFromWindow);*/

        if(isCreated){
            if(!isActive){
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

        if (nk_begin(context, name, nk_rect(homePosition.x, homePosition.y, 200, 200, rect), windowOptions)) {
            isCreated = true;
            closedFromWindow = false;
            nk_layout_row_dynamic(context, 30, 1);
            children.forEach(Element_UI::requestRender);

        }
        nk_end(context);
    }

    public void closeWindow(){
        isActive = false;
    }

    public void toggleVisibility(){
        closedFromWindow = true;
        isVisible = !isVisible;
    }
    public void toggleMinimize(){
        isMinimized = !isMinimized;
        nk_window_collapse(context,name, (isMinimized? 1 : 0));

    }}
