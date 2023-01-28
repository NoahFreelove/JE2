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

    public Vector2f position(){
        return position;
    }

    public Vector3f rotation(){
        return rotation;
    }

    public Vector2f scale(){
        return scale;
    }

    public void setPosition(Vector2f position){
        this.position = position;
    }

    public void setPosition(float x, float y){
        this.position = new Vector2f(x, y);
    }

    public void setRotation(Vector3f rotation){
        this.rotation = rotation;
    }

    public void setRotation(float x, float y, float z){
        this.rotation = new Vector3f(x, y, z);
    }

    public void setScale(Vector2f scale){
        this.scale = scale;
    }

    public void setScale(float x, float y){
        this.scale = new Vector2f(x, y);
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
