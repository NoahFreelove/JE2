package org.JE.JE2.Objects.Scripts.Attributes;

import org.JE.JE2.Objects.GameObject;
import org.JE.JE2.Objects.Scripts.Physics.Collision.BoxTrigger;
import org.JE.JE2.Objects.Scripts.Physics.PhysicsBody;
import org.JE.JE2.Objects.Scripts.Script;
import org.JE.JE2.Scene.Scene;

public interface ITriggerLiability extends IDontDestroyOnLoad{

    @Override
    default void addLiability(Scene oldScene, Scene newScene, GameObject object, Script script){
        BoxTrigger bt = (BoxTrigger) script;
        bt.cloneAndAdd(newScene);
        bt.destroy();
        object.removeScript(bt);
    }

    @Override
    default Class<? extends Script> getApplicableClass() {
        return BoxTrigger.class;
    }
}
