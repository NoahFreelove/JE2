package JE.Objects.Audio;

import JE.Audio.SoundPlayer;
import JE.Objects.Base.GameObject;

public class LocalSound extends GameObject {
    public final SoundPlayer soundPlayer;
    public LocalSound(String filepath, boolean loops){
        addComponent(soundPlayer = new SoundPlayer());
        soundPlayer.setAudio(filepath);
        soundPlayer.setLoops(loops);
    }
}
