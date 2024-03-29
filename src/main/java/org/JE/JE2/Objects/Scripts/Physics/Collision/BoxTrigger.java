package org.JE.JE2.Objects.Scripts.Physics.Collision;

import org.JE.JE2.Objects.GameObject;
import org.JE.JE2.Objects.Scripts.Physics.PhysicsBody;
import org.JE.JE2.Scene.Scene;
import org.jbox2d.dynamics.BodyType;
import org.joml.Vector2f;

/**
 * You should extend this class to create a trigger volume.
 * But if you don't want to you can use the triggerEvent field to set the trigger event.
 */
public class BoxTrigger extends TriggerVolume{
    private TriggerEvent triggerEvent = new TriggerEvent(){};

    public BoxTrigger(){}

    @Override
    protected void onTrigger(GameObject other) {
        triggerEvent.onTrigger(other);
    }

    @Override
    protected void onTriggerEnter(GameObject other) {
        triggerEvent.onTriggerEnter(other);
    }

    @Override
    protected void onTriggerExit(GameObject other) {
        triggerEvent.onTriggerExit(other);
    }

    public static GameObject triggerObject(Vector2f pos, Vector2f size){
        GameObject triggerObject = new GameObject();
        triggerObject.getTransform().setScale(size);
        triggerObject.getTransform().setPosition(pos);
        triggerObject.addScript(new BoxTrigger());
        return triggerObject;
    }

    public static GameObject triggerObject(Vector2f pos, Vector2f size, TriggerEvent triggerEvent){
        GameObject triggerObject = new GameObject();
        triggerObject.getTransform().setScale(size);
        triggerObject.getTransform().setPosition(pos);
        BoxTrigger bt = new BoxTrigger();
        triggerObject.addScript(bt);
        bt.triggerEvent = triggerEvent;
        return triggerObject;
    }

    @Override
    public void cloneAndAdd(Scene scene) {
        BoxTrigger bt = new BoxTrigger();
        bt.triggerEvent = triggerEvent;
        bt.defaultDensity = defaultDensity;
        bt.defaultFriction = defaultFriction;
        bt.defaultGravity = defaultGravity;
        bt.defaultRestitution = defaultRestitution;
        bt.fixedRotation = fixedRotation;
        bt.mode = mode;
        bt.attachedScene = scene;
        bt.positionOffset = positionOffset;
        getAttachedObject().addScript(bt);
        bt.create(mode, getAttachedObject().getTransform().position(), getAttachedObject().getTransform().scale());
    }

    public void setTriggerEvent(TriggerEvent triggerEvent){
        this.triggerEvent = triggerEvent;
    }
}
