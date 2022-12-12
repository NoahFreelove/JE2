package JE.Audio;

import JE.Scene.Scene;
import JE.Scene.World;

public final class AudioSourcePlayer extends AudioSource {
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

        playSound();
    }

    public void stop(){
        stopSound();
    }

    @Override
    public void destroy(){
        stopSound();
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
        /*worldRef = scene.world;
        worldRef.sounds.add(this);*/
    }
}
