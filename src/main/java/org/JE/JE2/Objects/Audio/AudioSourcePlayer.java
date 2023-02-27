package org.JE.JE2.Objects.Audio;

import org.JE.JE2.Manager;
import org.JE.JE2.Scene.Scene;
import org.JE.JE2.Scene.World;

public non-sealed class AudioSourcePlayer extends AudioSource {
    private World worldRef;
    public float range = 1f;

    public AudioSourcePlayer() {
        super();
    }
    public AudioSourcePlayer(String filepath) {
        super();
        setAudio(filepath);
    }

    public void play(){
        if(worldRef == null)
        {
            worldRef = Manager.activeScene().world;
            worldRef.sounds.add(this);
        }
        playSound();
    }

    public void stop(){
        stopSound();
    }

    @Override
    public void destroy(){
        stopSound();
        if(worldRef !=null)
            worldRef.sounds.remove(this);
    }

    @Override
    public void unload(Scene oldScene, Scene newScene){
        stopSound();
    }

    public void restart(){
        stopSound();
        playSound();
    }

    @Override
    public void gameObjectAddedToScene(Scene scene) {
        worldRef = scene.world;
        worldRef.sounds.add(this);
    }
}