package org.JE.JE2.Objects.Scripts.Attributes;

import org.JE.JE2.Objects.GameObject;
import org.JE.JE2.Objects.Scripts.Physics.PhysicsBody;
import org.JE.JE2.Objects.Scripts.Script;
import org.JE.JE2.Scene.Scene;

public interface IPhysicsLiability extends IDontDestroyOnLoad{

    @Override
    default void addLiability(Scene oldScene, Scene newScene, GameObject object, Script script){
        PhysicsBody pb = (PhysicsBody) script;
        pb.cloneAndAdd(newScene);
        pb.destroy();
        object.removeScript(pb);
    }

    @Override
    default Class<? extends Script> getApplicableClass() {
        return PhysicsBody.class;
    }
}
