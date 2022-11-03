package JE.Rendering;

import JE.Objects.Components.Component;
import JE.Objects.Components.ComponentRestrictions;
import JE.Security.GetClassCaller;

public class Renderer extends Component {
    protected VAO vao = new VAO();

    public Renderer(){
        restrictions = new ComponentRestrictions(false, true, true);
    }

    // When GL methods are called from the window class, we don't need to wrap in a runnable because its already in the GL thread
    public void Render() {
        System.out.println(GetClassCaller.getCallerClass());
    }

    @Override
    public void update() {

    }

    @Override
    public void start() {

    }

    @Override
    public void awake() {

    }
}
