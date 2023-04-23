package org.JE.JE2.Objects.Audio;

import org.JE.JE2.Manager;
import org.JE.JE2.Resources.Bundles.AudioBundle;
import org.JE.JE2.Resources.Resource;
import org.JE.JE2.Resources.ResourceManager;
import org.JE.JE2.Scene.Scene;
import org.JE.JE2.Scene.World;

public non-sealed class AudioSourcePlayer extends AudioSource {
    private World worldRef;

    public AudioSourcePlayer(){}

    private AudioSourcePlayer(Resource<AudioBundle> resource, boolean newResource){
        super(resource);
        if(newResource)
        {
            ResourceManager.indexResource(resource);
        }
    }

    public static AudioSourcePlayer checkExistElseCreate(String name, int ID, String bytePath){
        Resource<AudioBundle> resource = (Resource<AudioBundle>) ResourceManager.getIfExists(AudioBundle.class, name, ID);
        if(resource != null){
            return new AudioSourcePlayer(resource, false);
        }
        else{
            return new AudioSourcePlayer(new Resource<>(AudioProcessor.processAudio(bytePath),name,ID), true);
        }
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

    public void restart(){
        stopSound();
        playSound();
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

    @Override
    public void gameObjectAddedToScene(Scene scene) {
        worldRef = scene.world;
        worldRef.sounds.add(this);
    }
}
