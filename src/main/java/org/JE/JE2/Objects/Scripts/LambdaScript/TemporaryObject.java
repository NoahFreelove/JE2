package org.JE.JE2.Objects.Scripts.LambdaScript;

import org.JE.JE2.Objects.GameObject;
import org.JE.JE2.Objects.Scripts.Script;
import org.JE.JE2.Scene.Scene;

public class TemporaryObject extends Script {
    Scene worldRef;

    public static GameObject generateObject(){
        GameObject go = new GameObject();
        go.addScript(new TemporaryObject());
        return go;
    }

    public void remove(){
        if(worldRef != null)
            worldRef.remove(getAttachedObject());
    }


    @Override
    public void gameObjectAddedToScene(Scene scene) {
        worldRef = scene;
    }
}
