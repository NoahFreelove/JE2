package JE.UI.UIElements;

public abstract class Element_UI {
    protected abstract void render();
    public boolean isVisible = true;
    public final void requestRender(){
        if(isVisible)
            render();
    }
    public void setVisible(boolean v){
        this.isVisible = v;
    }
}
