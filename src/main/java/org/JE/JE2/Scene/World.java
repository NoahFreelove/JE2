package org.JE.JE2.Scene;

import org.JE.JE2.Annotations.Nullable;
import org.JE.JE2.Objects.Audio.AudioSourcePlayer;
import org.JE.JE2.Objects.GameObject;
import org.JE.JE2.Objects.Lights.Light;
import org.JE.JE2.UI.UIObjects.UIObject;

import org.jbox2d.common.Vec2;

import java.io.Serializable;
import java.util.concurrent.CopyOnWriteArrayList;

public class World implements Serializable {
    public CopyOnWriteArrayList<Light> lights = new CopyOnWriteArrayList<>();
    public CopyOnWriteArrayList<GameObject> gameObjects = new CopyOnWriteArrayList<>();
    public CopyOnWriteArrayList<AudioSourcePlayer> sounds = new CopyOnWriteArrayList<>();
    public CopyOnWriteArrayList<UIObject> UI = new CopyOnWriteArrayList<>();
    public org.jbox2d.dynamics.World physicsWorld = new org.jbox2d.dynamics.World(new Vec2(0,-9.8f));
    public World(){
        physicsWorld.setAllowSleep(false);
    }

    @Nullable
    public GameObject getObjectByID(long id){
        for (GameObject gameObject : gameObjects) {
            if(gameObject.identity().uniqueID == id)
                return gameObject;
        }
        return null;
    }
}
