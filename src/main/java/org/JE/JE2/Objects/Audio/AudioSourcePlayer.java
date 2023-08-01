package org.JE.JE2.Objects.Audio;

import org.JE.JE2.IO.Filepath;
import org.JE.JE2.Manager;
import org.JE.JE2.Rendering.Camera;
import org.JE.JE2.Rendering.Texture;
import org.JE.JE2.Resources.Bundles.AudioBundle;
import org.JE.JE2.Resources.Bundles.TextureBundle;
import org.JE.JE2.Resources.Resource;
import org.JE.JE2.Resources.ResourceManager;
import org.JE.JE2.Scene.Scene;
import org.JE.JE2.Scene.World;
import org.JE.JE2.Utility.JE2Math;

public non-sealed class AudioSourcePlayer extends AudioSource {
    private World worldRef;

    private boolean playOnNextLoad = false;

    public AudioSourcePlayer(){}

    private AudioSourcePlayer(Resource<AudioBundle> resource, boolean newResource){
        super(resource);
        if(newResource)
        {
            ResourceManager.indexResource(resource);
        }
    }

    public static AudioSourcePlayer checkExistElseCreate(String name, int ID, Filepath fp){
        Resource<AudioBundle> resource = (Resource<AudioBundle>) ResourceManager.getIfExists(AudioBundle.class, name, ID);
        if(resource != null){
            return new AudioSourcePlayer(resource, false);
        }
        else{
            return new AudioSourcePlayer(new Resource<>(AudioProcessor.processAudio(fp),name,ID), true);
        }
    }

    public static AudioSourcePlayer createPlayer(String name, Filepath fp){
        AudioSourcePlayer asp = checkExistElseCreate("",-1,fp);
        asp.getAudioResource().setName(name);
        return asp;
    }

    public static AudioSourcePlayer get(String name){
        return new AudioSourcePlayer((Resource<AudioBundle>) ResourceManager.getIfExists(AudioBundle.class, name, -1), false);
    }

    public void play(boolean playOnNextSceneLoad){
        if(playOnNextSceneLoad)
        {
            playOnNextLoad = true;
        }
        else
            play();
    }

    @Override
    public void start() {
        if(playOnNextLoad){
            play();
        }
        playOnNextLoad = false;
    }

    public void play(){
        System.out.println(getAudioResource().getID());
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

    public void setRelativeAudioBasedOnCamera(Camera target){

        float xListener = target.getAttachedObject().getTransform().position().x();
        float yListener = target.getAttachedObject().getTransform().position().y();

        float xSource = this.getAttachedObject().getTransform().position().x();
        float ySource = this.getAttachedObject().getTransform().position().y();

        float xRelative = xSource - xListener;
        float yRelative = ySource - yListener;

        float horizPan = xRelative / target.getWidth();
        float vertPan = yRelative / target.getHeight();

        horizPan = JE2Math.clamp(horizPan,-1,1);
        vertPan = JE2Math.clamp(vertPan,-1,1);

        float distance = (float) Math.sqrt(xRelative * xRelative + yRelative * yRelative);

        float volume = 1.0f; // Default full volume
        if (distance > fadeOutDistance) {
            // Apply fading based on distance
            float fadeOutFactor = (distance - fadeOutDistance) / (maxDistance - fadeOutDistance);
            volume = 1.0f - fadeOutFactor; // Gradually reduce the volume
        }
        // If in a 5 radius around source, dont immediately swap fully to left or right ear,
        // instead gradually swap to left or right ear
        if (distance < panStrength) {
            float panFactor = (panStrength - distance) / panStrength;
            horizPan *= panFactor;
            vertPan *= panFactor;
        }


        setGain(volume);

        setPositionWorld(horizPan,vertPan);


    }
}
