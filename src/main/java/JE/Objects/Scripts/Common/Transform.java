package JE.Objects.Scripts.Common;

import JE.Objects.Scripts.Base.Script;
import JE.Objects.Scripts.Physics.PhysicsBody;
import JE.Utility.JOMLtoJBOX;
import org.joml.*;

import java.lang.Math;

public class Transform extends Script {
    private Vector2f position;
    private float zPos = 1;
    private Vector3f rotation;
    private Vector2f scale;

    private PhysicsBody physicsBody;

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

    public float zPos()
    {
        return zPos;
    }

    public void setZPos(float zPos)
    {
        this.zPos = zPos;
    }

    public void translateX(float x){
        position.x += x;
    }

    public void translateY(float y){
        position.y += y;
    }

    public Vector3f lookAt(Vector2f target){
        Vector2f direction = new Vector2f(target).sub(position);
        float angle = (float) Math.atan2(direction.y, direction.x);
        return new Vector3f(0, 0, (float) Math.toDegrees(angle));
    }


    @Override
    public void start() {
        physicsBody = getAttachedObject().getScript(PhysicsBody.class);
    }

    @Override
    public void update() {
        if(physicsBody !=null)
        {
            if(!physicsBody.hasInitialized())
                return;
            Vector2f adjustedPos = new Vector2f(position());
            adjustedPos.x += physicsBody.getSize().x/2;
            adjustedPos.y += physicsBody.getSize().y/2;
            physicsBody.body.setTransform(JOMLtoJBOX.vec2(adjustedPos), rotation().z());
        }
    }

    @Override
    public void awake() {

    }

    @Override
    public void onForeignScriptAdded(Script script) {
        if(script instanceof PhysicsBody pb){
            physicsBody = pb;
        }
    }
}
