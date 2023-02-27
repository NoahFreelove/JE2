package JE.Objects.Scripts.Common;

import JE.Objects.Scripts.Base.Script;
import JE.Objects.Scripts.Base.ScriptRestrictions;
import JE.Objects.Scripts.Physics.PhysicsBody;
import JE.Utility.JOMLtoJBOX;
import org.joml.*;

import java.lang.Math;

public class Transform extends Script {
    private Vector3f position;
    private Vector3f rotation;
    private Vector3f scale;

    private transient PhysicsBody physicsBody;

    public Transform(){
        position = new Vector3f();
        rotation = new Vector3f();
        scale = new Vector3f(1,1,1);
        setRestrictions(new ScriptRestrictions(false,false,false));
    }

    public Vector2f position(){
        return new Vector2f(position.x, position.y);
    }
    public Vector3f position3D(){
        return position;
    }
    public Vector3f rotation(){
        return rotation;
    }

    public Vector2f scale(){
        return new Vector2f(scale.x, scale.y);
    }

    public Vector3f scale3D(){
        return scale;
    }

    public void setPosition(Vector2f position){
        this.position = new Vector3f(position.x, position.y, this.position.z);
    }

    public void setPosition(Vector3f position){
        this.position = position;
    }

    public void setPosition(float x, float y){
        this.position = new Vector3f(x, y, position.z);
    }

    public void setPosition(float x, float y, float z){
        this.position = new Vector3f(x, y, z);
    }

    public void setRotation(Vector3f rotation){
        this.rotation = rotation;
    }

    public void setRotation(float x, float y, float z){
        this.rotation = new Vector3f(x, y, z);
    }

    public void setScale(Vector2f scale){
        this.scale = new Vector3f(scale.x, scale.y, this.scale.z);
    }

    public void setScale(Vector3f scale){
        this.scale = scale;
    }

    public void setScale(float x, float y){
        this.scale = new Vector3f(x, y, scale.z);
    }

    public void setScale(float x, float y, float z){
        this.scale = new Vector3f(x, y, z);
    }

    public float zPos()
    {
        return position.z();
    }

    public void setZPos(float zPos)
    {
        this.position.z = zPos;
    }

    public void translateX(float x){
        position.x += x;
    }

    public void translateY(float y){
        position.y += y;
    }

    public void translateZ(float z){
        position.z += z;
    }

    public Vector3f lookAt(Vector2f target){
        Vector2f direction = new Vector2f(target).sub(position());
        float angle = (float) Math.atan2(direction.y, direction.x);
        return new Vector3f(0, 0, (float) Math.toDegrees(angle));
    }

    public Vector3f lookAt(Vector3f target){
        Vector3f direction = new Vector3f(target).sub(position3D());
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

    @Override
    public void load(){
        super.load();
        setRestrictions(new ScriptRestrictions(false,false,false));
    }
}
