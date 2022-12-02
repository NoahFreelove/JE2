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
    public Transform(Vector2f position, Vector3f rotation, Vector2f scale){
        this.position = position;
        this.rotation = rotation;
        this.scale = scale;
    }
    public Transform(Vector2f position){
        this.position = position;
        rotation = new Vector3f();
        scale = new Vector2f(1, 1);
    }
    public Transform(Transform t){
        this.position = new Vector2f(t.position);
        this.rotation = new Vector3f(t.rotation);
        this.scale = new Vector2f(t.scale);
    }

    public static final Transform Zero = new Transform();
    public static final Transform One = new Transform(new Vector2f(1,1), new Vector3f(1,1,1), new Vector2f(1,1));

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
