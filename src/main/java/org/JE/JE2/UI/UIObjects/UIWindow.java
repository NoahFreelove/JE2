package org.JE.JE2.UI.UIObjects;

import org.JE.JE2.Manager;
import org.JE.JE2.UI.UIElements.Style.Color;
import org.JE.JE2.UI.UIElements.UIElement;
import org.JE.JE2.UI.UIScaler;
import org.JE.JE2.Window.UIHandler;
import org.joml.Vector2f;
import org.lwjgl.nuklear.*;

import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;

import static org.JE.JE2.Window.UIHandler.nuklearContext;
import static org.lwjgl.nuklear.Nuklear.*;
import static org.lwjgl.nuklear.Nuklear.nk_end;

public class UIWindow extends UIObject {
    private final NkContext context = nuklearContext;
    public String name = "Window" + Math.random();
    public int windowOptions = NK_WINDOW_TITLE | NK_WINDOW_BORDER | NK_WINDOW_MINIMIZABLE | NK_WINDOW_SCALABLE | NK_WINDOW_MOVABLE | NK_WINDOW_CLOSABLE;
    private NkStyleWindow window;
    private NkRect rect = NkRect.create();
    // Will reset to this position upon showing window from a hidden state. definitely a feature.
    private Vector2f pos = new Vector2f(50, 50);
    private Vector2f size = new Vector2f(200, 200);

    private Color color = Color.DARK_GREY;
    private boolean isMinimized = false;
    public boolean closedFromWindow = false;

    public boolean reset = false;

    public boolean destroyOnLoad = true;

    public int rowHeight = 30;
    public int maxElementsPerRow = 1;

    private CopyOnWriteArrayList<UIElement> children = new CopyOnWriteArrayList<>();

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

    public void hideBar(){
        windowOptions = 0;
    }

    @Override
    public void update() {

    }

    @Override
    public void render() {
        if (reset) {
            reset = false;
            return;
        }
        if (isCreated) {
            if (!isAlive) {
                Manager.activeScene().removeUI(this);
                return;
            }

            if (isVisible && nk_window_is_closed(context, name) && !closedFromWindow) {
                closedFromWindow = true;
                isVisible = false;
            }
            if (!isVisible) {
                return;
            }
        }

        float scaledPosX = pos.x * UIScaler.MULTIPLIERX;
        float scaledPosY = pos.y * UIScaler.MULTIPLIERY;

        float scaledSizeX = size.x * UIScaler.MULTIPLIERX;
        float scaledSizeY = size.y * UIScaler.MULTIPLIERY;

        context.style().window().fixed_background().data().color().set(color.nkColor());
        if (nk_begin(context, name, nk_rect(scaledPosX, scaledPosY, scaledSizeX, scaledSizeY, rect), windowOptions)) {
            isCreated = true;
            closedFromWindow = false;
            renderChildren();
        }


        nk_end(context);
    }

    protected void renderChildren() {
        nk_layout_row_dynamic(context, rowHeight*UIScaler.MULTIPLIERY, maxElementsPerRow);
        children.forEach((uiElement -> {
            nk_style_set_font(nuklearContext, UIHandler.active_font.getFont());
            uiElement.requestRender();
        }));
    }

    public CopyOnWriteArrayList<UIElement> getChildren(){
        return children;
    }

    public void closeWindow() {
        isAlive = false;
    }

    public void toggleVisibility() {
        closedFromWindow = true;
        isVisible = !isVisible;
    }

    public void setVisibility(boolean vis) {
        closedFromWindow = true;
        isVisible = vis;
    }

    public void toggleMinimize() {
        isMinimized = !isMinimized;
        nk_window_collapse(context, name, (isMinimized ? 1 : 0));
    }

    public void setMinimized(boolean v)
    {
        isMinimized = v;
        nk_window_collapse(context, name, (isMinimized ? 1 : 0));
    }

    public void setPos(Vector2f pos) {
        this.pos = pos;
        isCreated = false;
        reset = true;
    }

    public void setPos(float x, float y) {
        this.pos = new Vector2f(x,y);
        isCreated = false;
        reset = true;
    }

    public void setSize(Vector2f size) {
        this.size = size;
        isCreated = false;
        reset = true;
    }

    public Vector2f getSize() {
        return size;
    }

    public void openWindow(){
        isVisible = true;
        closedFromWindow = false;
    }

    public void setBackgroundColor(Color c) {
        this.color = c;
        reset = true;
        isCreated = false;
    }

    public void addElement(UIElement element) {
        if(element == null)
            return;
        if(!children.contains(element))
            children.add(element);
    }
    public void addElement(UIElement... elements) {
        for(UIElement element : elements)
            addElement(element);
    }

    public void removeElement(UIElement element) {
        if(element == null)
            return;
        children.remove(element);
    }
}
