package org.JE.JE2.Objects.Scripts;

import org.JE.JE2.Annotations.ActPublic;
import org.JE.JE2.Objects.Scripts.Physics.PhysicsBody;
import org.jbox2d.common.Vec2;
import org.joml.*;

import java.lang.Math;

public class Transform extends Script {

    public static Transform zero = new Transform();

    @ActPublic
    private Vector3f position;
    private Vector2f position2d = new Vector2f();
    @ActPublic
    private Vector3f rotation;

    @ActPublic
    private Vector3f scale;
    private Vector2f scale2d = new Vector2f();

    private transient PhysicsBody physicsBody;

    public Transform(){
        position = new Vector3f();
        rotation = new Vector3f();
        scale = new Vector3f(1,1,1);
        setRestrictions(new ScriptRestrictions(false,false,false));
    }

    public Vector2f position(){
        position2d.set(position.x(),position.y());
        return position2d;
    }
    public Vector3f position3D(){
        return position;
    }
    public Vector3f rotation(){
        return rotation;
    }

    public Vector2f scale(){
        scale2d.set(scale.x(),scale.y());
        return scale2d;
    }

    public Vector3f scale3D(){
        return scale;
    }

    public Vector3f setPosition(Vector2f position){
        return this.position.set(position.x, position.y, this.position.z);
    }

    public Vector3f setPosition(Vector3f position){
        return this.position.set(position);
    }

    public Vector3f setPosition(float x, float y){
        return this.position.set(x, y, this.position.z);
    }

    public Vector3f setPosition(float x, float y, float z){
        return this.position.set(x, y, z);
    }

    public Vector3f setRotation(Vector3f rotation){
        return this.rotation.set(rotation);
    }

    public Vector3f setRotation(float x, float y, float z){
        return this.rotation.set(x, y, z);
    }

    public Vector3f setScale(Vector2f scale){
        return this.scale.set(scale.x, scale.y, this.scale.z);
    }

    public Vector3f setScale(Vector3f scale){
        return this.scale.set(scale);
    }

    public Vector3f setScale(float x, float y){
        return this.scale.set(x, y, this.scale.z);
    }

    public Vector3f setScale(float x, float y, float z){
        return this.scale.set(x, y, z);
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

    public void translate(Vector2f vec){
        translateX(vec.x());
        translateY(vec.y());
    }
    public void translate(Vector3f vec){
        translateX(vec.x());
        translateY(vec.y());
        translateZ(vec.z());
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

    private final Vector2f adjustedPos = new Vector2f();
    private final Vec2 jBoxAdjustedPos = new Vec2();
    @Override
    public void update() {
        if(physicsBody !=null)
        {
            if(!physicsBody.hasInitialized())
                return;
            adjustedPos.set(position());
            adjustedPos.x += physicsBody.getSize().x/2;
            adjustedPos.y += physicsBody.getSize().y/2;
            jBoxAdjustedPos.set(adjustedPos.x(),adjustedPos.y());
            physicsBody.body.setTransform(jBoxAdjustedPos, rotation().z());
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
