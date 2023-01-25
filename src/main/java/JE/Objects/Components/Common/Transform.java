package JE.Objects.Components.Common;

import JE.Objects.Components.Base.Component;
import org.joml.*;

import java.lang.Math;

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

    public Vector3f lookAt(Vector2f target){
        Vector2f direction = new Vector2f(target).sub(position);
        float angle = (float) Math.atan2(direction.y, direction.x);
        return new Vector3f(0, 0, (float) Math.toDegrees(angle));
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
