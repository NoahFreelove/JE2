package JE.Objects.Audio;

import JE.Audio.SoundPlayer;
import JE.Objects.Base.GameObject;

public class Sound2D extends GameObject {
    public final SoundPlayer soundPlayer;
    public Sound2D(String filepath, boolean loops){
        addComponent(soundPlayer = new SoundPlayer(filepath,loops));
        soundPlayer.play();
    }
}
