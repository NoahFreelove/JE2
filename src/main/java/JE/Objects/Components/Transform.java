package JE.Objects.Components;

import org.joml.*;

public class Transform extends Component{
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

    public static final Transform Zero = new Transform();
    public static final Transform One = new Transform(new Vector2f(1,1), new Vector3f(1,1,1), new Vector2f(1,1));

    @Override
    public void Start() {

    }

    @Override
    public void Update() {
    }

    @Override
    public void awake() {

    }
}
