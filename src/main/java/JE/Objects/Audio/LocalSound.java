package JE.Objects.Audio;

import JE.Audio.AudioSourcePlayer;
import JE.Objects.Base.GameObject;

public class LocalSound extends GameObject {
    public final AudioSourcePlayer soundPlayer;
    public LocalSound(){
        addComponent(soundPlayer = new AudioSourcePlayer());
    }
}
