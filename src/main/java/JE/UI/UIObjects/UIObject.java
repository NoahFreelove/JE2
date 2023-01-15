package JE.UI.UIObjects;

public abstract class UIObject {
    protected boolean isCreated = false;
    protected boolean isVisible =true; // will not be drawn if false
    protected boolean isAlive = true; // will be removed from scene if false
    abstract public void update();
    abstract protected void render();
    public final void requestRender(){
        render();
    }
}
