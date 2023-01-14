package JE.UI.UIObjects;

public abstract class Object_UI {
    protected boolean isCreated = false;
    protected boolean isVisible =true ;
    protected boolean isActive = true;
    abstract public void update();
    abstract protected void render();
    public final void requestRender(){
        render();
    }
}
