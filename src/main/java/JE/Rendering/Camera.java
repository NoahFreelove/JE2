package JE.Rendering;

import JE.Objects.Base.GameObject;
import JE.Objects.Components.Component;
import JE.Security.GetClassCaller;
import org.joml.Vector2f;

public class Camera extends Component {
    public GameObject parentObject = new GameObject();
    public Vector2f position = new Vector2f();
    public float zPos = 10;
    public Vector2f positionOffset = new Vector2f();

    public Camera(){
    }

    @Override
    public void update() {
        this.position = new Vector2f(parentObject.getTransform().position.x(), parentObject.getTransform().position.y()).add(positionOffset);
    }

    @Override
    public void start() {

    }

    @Override
    public void awake() {

    }
}
