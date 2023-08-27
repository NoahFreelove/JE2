package org.JE.JE2.Objects.Scripts.Physics.Collision;

import org.JE.JE2.Objects.GameObject;
import org.JE.JE2.Objects.Scripts.Physics.PhysicsBody;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.contacts.ContactEdge;
import org.joml.Vector2f;

import java.util.HashMap;

public abstract class TriggerVolume extends PhysicsBody {
    private final HashMap<GameObject,Boolean> triggeredObjects = new HashMap<>();

    @Override
    protected PhysicsBody create(BodyType defaultState, Vector2f initialPosition, Vector2f initialSize) {
        super.create(BodyType.STATIC,initialPosition,initialSize);
        activeFixture.m_isSensor = true;
        return this;
    }

    @Override
    public void start() {
        super.start();
    }

    @Override
    public void update(){
        if(!hasInitialized)
            return;

        if(attachedScene != null){
            if(!isInCorrectScene())
                return;
        }
        if (body !=null) {
            ContactEdge contactEdge = body.getContactList();
            if(contactEdge == null)
                return;
            while (contactEdge != null) {
                if (contactEdge.contact.isTouching()) {
                    if (contactEdge.contact.getFixtureA() == activeFixture) {
                        onTrigger((GameObject) contactEdge.contact.getFixtureB().getBody().getUserData());

                        // if the object is already triggered, don't trigger it again
                        if(triggeredObjects.containsKey((GameObject) contactEdge.contact.getFixtureB().getBody().getUserData()))
                            return;
                        // mark the object as triggered so we don't call onTriggerEnter again
                        triggeredObjects.put((GameObject) contactEdge.contact.getFixtureB().getBody().getUserData(),true);
                        onTriggerEnter((GameObject) contactEdge.contact.getFixtureB().getBody().getUserData());

                    } else if (contactEdge.contact.getFixtureB() == activeFixture) {
                        onTrigger((GameObject) contactEdge.contact.getFixtureB().getBody().getUserData());

                        // if the object is already triggered, don't trigger it again
                        if(triggeredObjects.containsKey((GameObject) contactEdge.contact.getFixtureA().getBody().getUserData()))
                            return;
                        triggeredObjects.put((GameObject) contactEdge.contact.getFixtureA().getBody().getUserData(),true);
                        onTriggerEnter((GameObject) contactEdge.contact.getFixtureA().getBody().getUserData());
                    }
                }
                contactEdge = contactEdge.next;
            }
            // call onTriggerExit for all objects that are no longer touching the trigger
            triggeredObjects.forEach((key, value) -> {if(!value){onTriggerExit(key);}});
            // Remove all objects that are no longer touching the trigger, so we don't call onTriggerExit again
            triggeredObjects.values().removeIf(value -> !value);
            triggeredObjects.forEach((key, value) -> triggeredObjects.put(key,false));
        }
    }

    protected abstract void onTrigger(GameObject other);

    protected abstract void onTriggerEnter(GameObject other);

    protected abstract void onTriggerExit(GameObject other);
}