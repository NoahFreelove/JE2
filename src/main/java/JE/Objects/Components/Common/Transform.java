package JE.Objects.Components.Common;

import JE.Objects.Components.Base.Component;
import org.joml.*;

public class Transform extends Component {
    public Vector2f position;
    public float zPos = 1;
    public Vector3f rotation;
    public Vector2f scale;

    public Transform(){
        position = new Vector2f();
        rotation = new Vector3f();
        scale = new Vector2f(1, 1);
    }


    @Override
    public void start() {

    }

    @Override
    public void update() {
    }

    @Override
    public void awake() {

    }
}
