package org.JE.JE2.Objects.Scripts;

import org.JE.JE2.Annotations.ActPublic;
import org.JE.JE2.Annotations.PrimarySetter;
import org.JE.JE2.Annotations.StandaloneScript;
import org.JE.JE2.Objects.Scripts.Physics.PhysicsBody;
import org.JE.JE2.Objects.Scripts.Serialize.Load;
import org.JE.JE2.Objects.Scripts.Serialize.Save;
import org.jbox2d.common.Vec2;
import org.joml.Vector2f;
import org.joml.Vector3f;

import java.util.HashMap;

@StandaloneScript
public class Transform extends Script implements Save, Load {

    public static Transform zero = new Transform();

    @ActPublic
    private Vector3f position = new Vector3f();
    private Vector2f position2d = new Vector2f();
    @ActPublic
    private Vector3f rotation = new Vector3f();

    @ActPublic
    private Vector3f scale = new Vector3f();
    private Vector2f scale2d = new Vector2f();

    private transient PhysicsBody physicsBody;

    private TransformRestrictions restrictions = new TransformRestrictions();

    public Transform(){
        position = new Vector3f();
        rotation = new Vector3f();
        scale = new Vector3f(1,1,1);
        setRestrictions(new ScriptRestrictions(false,false,false));
    }

    public Transform(Transform clone){
        this();
        this.set(clone);
    }

    public Transform(Vector2f pos){
        this();
        setPosition(pos);
    }

    public Transform(Vector2f pos,  Vector3f rot, Vector2f scale){
        setPosition(pos);
        setRotation(rot);
        setScale(scale);
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
        return this.setPosition(position.x(), position.y(), zPos());
    }

    public Vector3f setPosition(Vector3f position){
        return this.setPosition(position.x, position.y, position.z);
    }

    public Vector3f setPosition(float x, float y){
        return this.setPosition(x,y,zPos());
    }

    @PrimarySetter
    public Vector3f setPosition(float x, float y, float z){
        if(restrictions.LOCK)
            return position3D();
        if(!restrictions.TRANSLATE_X)
            x = position.x;
        if(!restrictions.TRANSLATE_Y)
            y = position.y;
        if(!restrictions.TRANSLATE_Z)
            z = position.z;

        this.position.set(x, y, z);
        update();
        return position3D();
    }

    public Vector3f setRotation(Vector3f rotation){
        return setRotation(rotation.x, rotation.y, rotation.z);
    }

    @PrimarySetter
    public Vector3f setRotation(float x, float y, float z){
        if(restrictions.LOCK)
            return rotation();
        if(!restrictions.ROTATE_X)
            x = rotation.x;
        if(!restrictions.ROTATE_Y)
            y = rotation.y;
        if(!restrictions.ROTATE_Z)
            z = rotation.z;

        this.rotation.set(x, y, z);
        update();
        return this.rotation();
    }

    public void rotate(float x, float y, float z){
        setRotation(rotation.x + x, rotation.y + y, rotation.z + z);
    }

    public void rotateZ(float z){
        setRotation(rotation.x, rotation.y, rotation.z + z);
    }


    public Vector3f setScale(Vector2f scale){
        return this.setScale(scale.x, scale.y, scale3D().z);
    }

    public Vector3f setScale(Vector3f scale){
       return this.scale.set(scale.x, scale.y, scale.z);
    }

    public Vector3f setScale(float x, float y){
        return this.setScale(x, y, scale.z);
    }

    @PrimarySetter
    public Vector3f setScale(float x, float y, float z){
        if(restrictions.LOCK)
            return scale3D();
        if(!restrictions.SCALE_X)
            x = scale.x;
        if(!restrictions.SCALE_Y)
            y = scale.y;
        if(!restrictions.SCALE_Z)
            z = scale.z;

        this.scale.set(x, y, z);
        return this.scale3D();
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
        setPosition(position.x + x, position.y);
    }

    public void translateY(float y){
        setPosition(position.x, position.y + y);
    }

    public void translateZ(float z){
        setPosition(position.x, position.y, position.z + z);
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

    public void scale(float x, float y){
        setScale(scale.x + x, scale.y + y);
    }

    public void scale(Vector2f vec){
        scale(vec.x(), vec.y());
    }

    private void translate(float x, float y, float z) {
        setPosition(position.x + x, position.y + y, position.z + z);
    }
    private void rotate(Vector3f vec) {
        setRotation(rotation.x + vec.x(), rotation.y + vec.y(), rotation.z + vec.z());
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
        physicsBody = getAttachedObject().getPhysicsBody();
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


    public Transform relativeAdd(Transform other){
        this.translate(other.position());
        this.setRotation(rotation().add(other.rotation()));
        this.setScale(scale().mul(other.scale()));
        return this;
    }

    public Transform quickRelativeAdd(Transform other){
        setPosition(position.x + other.position().x, position.y + other.position().y, position.z + other.zPos());
        setRotation(rotation.x + other.rotation().x, rotation.y + other.rotation().y, rotation.z + other.rotation().z);
        setScale(scale.x * other.scale().x, scale.y * other.scale().y, scale.z * other.scale3D().z);
        return this;
    }

    public Transform relativeSub(Transform other){
        this.translate(other.position().negate());
        this.setRotation(rotation().sub(other.rotation()));
        this.setScale(scale().div(other.scale()));
        return this;
    }

    public Transform sub(Transform other){
        this.position.sub(other.position3D());
        this.rotation.sub(other.rotation());
        this.scale.sub(other.scale3D());
        return this;
    }

    private static Transform temp = new Transform();
    public static Transform anonymousSub(Transform t1, Transform t2){
        temp.set(t1);
        temp.relativeSub(t2);
        return new Transform(temp);
    }

    public Transform copy() {
        return new Transform(new Vector2f(position2d),new Vector3f(rotation),new Vector2f(scale2d));
    }

    @PrimarySetter
    public Transform set(Transform other){
        setPosition(other.position());
        setRotation(other.rotation());
        setScale(other.scale());
        return this;
    }

    @Override
    public String toString() {
        return "Transform{" +
                "position=" + position +
                ", rotation=" + rotation +
                ", scale=" + scale +
                '}';
    }


    public static void getDelta(Transform t1, Transform t2, Transform result){
        result.set(t1);
        result.sub(t2);
    }

    public void inheritTransform(Transform delta){
        if(!restrictions.INHERIT || restrictions.LOCK)
            return;
        //System.out.println("Inheriting: " + delta.toString());
        if(restrictions.INHERIT_POSITION)
            this.translate(delta.position().negate());
        if(restrictions.INHERIT_ROTATION)
            this.rotate(delta.rotation().negate());
        if(restrictions.INHERIT_SCALE)
            this.scale(delta.scale().negate());
    }


    @Override
    public void load(HashMap<String, String> data) {
        String[] posSplit = data.get("position").split(":");
        String[] rotSplit = data.get("rotation").split(":");
        String[] scaleSplit = data.get("scale").split(":");
        String[] restrictionsSplit = data.get("restrictions").split(":");
        position.set(Float.parseFloat(posSplit[0]), Float.parseFloat(posSplit[1]), Float.parseFloat(posSplit[2]));
        rotation.set(Float.parseFloat(rotSplit[0]), Float.parseFloat(rotSplit[1]), Float.parseFloat(rotSplit[2]));
        scale.set(Float.parseFloat(scaleSplit[0]), Float.parseFloat(scaleSplit[1]), Float.parseFloat(scaleSplit[2]));
        restrictions.LOCK = Boolean.parseBoolean(restrictionsSplit[0]);
        restrictions.TRANSLATE_X = Boolean.parseBoolean(restrictionsSplit[1]);
        restrictions.TRANSLATE_Y = Boolean.parseBoolean(restrictionsSplit[2]);
        restrictions.TRANSLATE_Z = Boolean.parseBoolean(restrictionsSplit[3]);
        restrictions.ROTATE_X = Boolean.parseBoolean(restrictionsSplit[4]);
        restrictions.ROTATE_Y = Boolean.parseBoolean(restrictionsSplit[5]);
        restrictions.ROTATE_Z = Boolean.parseBoolean(restrictionsSplit[6]);
        restrictions.SCALE_X = Boolean.parseBoolean(restrictionsSplit[7]);
        restrictions.SCALE_Y = Boolean.parseBoolean(restrictionsSplit[8]);
        restrictions.SCALE_Z = Boolean.parseBoolean(restrictionsSplit[9]);
        restrictions.INHERIT = Boolean.parseBoolean(restrictionsSplit[10]);
        restrictions.INHERIT_POSITION = Boolean.parseBoolean(restrictionsSplit[11]);
        restrictions.INHERIT_ROTATION = Boolean.parseBoolean(restrictionsSplit[12]);
        restrictions.INHERIT_SCALE = Boolean.parseBoolean(restrictionsSplit[13]);
    }

    @Override
    public HashMap<String, String> save() {
        HashMap<String, String> data = new HashMap<>();
        data.put("position", position.x + ":" + position.y + ":" + position.z);
        data.put("rotation", rotation.x + ":" + rotation.y + ":" + rotation.z);
        data.put("scale", scale.x + ":" + scale.y + ":" + scale.z);
        data.put("restrictions", restrictions.LOCK + ":" + restrictions.TRANSLATE_X + ":" + restrictions.TRANSLATE_Y + ":"
                + restrictions.TRANSLATE_Z + ":" + restrictions.ROTATE_X + ":" + restrictions.ROTATE_Y + ":"
                + restrictions.ROTATE_Z + ":" + restrictions.SCALE_X + ":" + restrictions.SCALE_Y + ":"
                + restrictions.SCALE_Z + ":" + restrictions.INHERIT + ":" + restrictions.INHERIT_POSITION + ":"
                + restrictions.INHERIT_ROTATION + ":" + restrictions.INHERIT_SCALE);
        return data;
    }

    public String simpleSerialize(){
        return position.x + ":" + position.y + ":" + position.z + ":" + rotation.x + ":" + rotation.y + ":" + rotation.z + ":" + scale.x + ":" + scale.y + ":" + scale.z;
    }

    public void simpleDeserialize(String input) {
        String[] split = input.split(":");
        position.set(Float.parseFloat(split[0]), Float.parseFloat(split[1]), Float.parseFloat(split[2]));
        rotation.set(Float.parseFloat(split[3]), Float.parseFloat(split[4]), Float.parseFloat(split[5]));
        scale.set(Float.parseFloat(split[6]), Float.parseFloat(split[7]), Float.parseFloat(split[8]));
    }
}
