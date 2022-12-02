package JE.Audio;

import JE.Objects.Components.Base.Component;

public class SoundPlayer extends Component {
    public final Sound sound;

    public SoundPlayer(String filepath, boolean loops) {
        sound = new Sound(filepath, loops);
    }

    public void play(){
        sound.play();
    }

    public void stop(){
        sound.stop();
    }

    @Override
    public void destroy(){
        stop();
    }

    @Override
    public void unload(){
        stop();
    }

    public void restart(){
        sound.stop();
        sound.play();
    }
}
