package org.JE.JE2.Objects.Scripts.Attributes;

import org.JE.JE2.Objects.GameObject;
import org.JE.JE2.Objects.Identity;
import org.JE.JE2.Objects.Scripts.Physics.PhysicsBody;
import org.JE.JE2.Objects.Scripts.Script;
import org.JE.JE2.Rendering.Camera;
import org.JE.JE2.Scene.Scene;

public class DontDestroyOnLoad extends Script {

    @Override
    public void update(){
        //Logger.log(getAttachedObject().getTransform().position(), 2);
    }
    @Override
    public void unload(Scene oldScene, Scene newScene) {
        GameObject o = getAttachedObject();
        if (o == null) return;
        o.linkedScene = newScene;

        if(!newScene.setIfExists(Identity.createFakeID(o.name, o.tag, o.uniqueID()), o, true)){
            newScene.add(o);
            addLiabilities(oldScene, newScene);
        }
    }

    /**
     * For you to implement if your scripts require extra attention when being moved to a new scene.
     */
    public void addLiabilities(Scene oldScene, Scene newScene){
        Camera c = getAttachedObject().getScript(Camera.class);
        if (c != null)
            newScene.setCamera(c);

        PhysicsBody pb = getAttachedObject().getScript(PhysicsBody.class);
        if(pb !=null)
            pb.cloneAndAdd(newScene);

    }

}
