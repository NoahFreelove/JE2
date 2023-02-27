package org.JE.JE2.UI.UIElements;

public abstract class UIElement {
    protected abstract void render();
    private boolean isVisible = true;
    private boolean isActive = true;

    public final void requestRender(){
        if(isVisible) {
            render();
        }

    }
    public UIElement setVisible(boolean v){
        this.isVisible = v;
        return this;
    }

    public UIElement setActive(boolean v){
        this.isActive = v;
        return this;
    }
    public boolean isActive(){
        return isActive;
    }
    public boolean isVisible(){
        return isVisible;
    }
}