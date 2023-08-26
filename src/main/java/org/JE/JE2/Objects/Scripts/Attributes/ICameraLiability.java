package org.JE.JE2.Objects.Scripts.Attributes;

import org.JE.JE2.Objects.GameObject;
import org.JE.JE2.Objects.Scripts.Physics.PhysicsBody;
import org.JE.JE2.Objects.Scripts.Script;
import org.JE.JE2.Rendering.Camera;
import org.JE.JE2.Scene.Scene;

public interface ICameraLiability extends IDontDestroyOnLoad {
    @Override
    default void addLiability(Scene oldScene, Scene newScene, GameObject object, Script script){
        Camera camera = (Camera) script;
        if(camera.getActive()){
            newScene.setCamera(camera);
        }
    }

    @Override
    default Class<? extends Script> getApplicableClass() {
        return Camera.class;
    }
}
