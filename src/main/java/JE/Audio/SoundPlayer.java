package JE.Audio;

import JE.Manager;
import JE.Scene.Scene;
import JE.Scene.World;

public final class SoundPlayer extends Sound {
    private World worldRef;
    public float range = 1f;

    public SoundPlayer(String filepath, boolean loops) {
        super(filepath, loops);
    }

    public void play(){
        if(Manager.getActiveScene().world != worldRef)
            return;
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
    public void unload(){
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
