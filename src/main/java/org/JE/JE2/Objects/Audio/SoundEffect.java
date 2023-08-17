package org.JE.JE2.Objects.Audio;

import org.JE.JE2.IO.Logging.Logger;
import org.JE.JE2.Objects.Scripts.Script;
import org.JE.JE2.Resources.Bundles.AudioBundle;
import org.JE.JE2.Resources.Resource;

/*
    When playing a sound effect in game a frequent occurance is that the same sound effect is played multiple times in quick succession.
    This causes the sound to restart abruptly and sound bad. This class is meant to solve that problem by allowing you to create a
    SoundEffect which will go through a list of AudioSourcePlayers and play the next one in the list that is not currently playing.
 */
public class SoundEffect extends Script {

    private int maxInstances = 1;
    private AudioSourcePlayer[] audioSources;
    public SoundEffect(){}

    public SoundEffect(AudioSourcePlayer audioSourcePlayer, int maxInstances){
        this.maxInstances = maxInstances;
        audioSources = new AudioSourcePlayer[maxInstances];
        for(int i = 0; i < maxInstances; i++){
            audioSources[i] = audioSourcePlayer.clonePlayer();
        }
    }

    public void playAvailable(){
        for(int i = 0; i < maxInstances; i++){
            if(!audioSources[i].isPlaying()){
                audioSources[i].play();
                return;
            }
        }
        Logger.log("Sound Effect could not play because all available instances are currently playing", Logger.WARN);
    }

    public void playAny(){
        audioSources[0].play();
    }

    public void changeInstanceSize(int num){
        for (AudioSourcePlayer audioSourcePlayer: audioSources) {
            audioSourcePlayer.stop();
        }
        Resource<AudioBundle> bundle = audioSources[0].getAudioResource();
        AudioSourcePlayer[] newAudioSources = new AudioSourcePlayer[num];
        for(int i = 0; i < num; i++){
            newAudioSources[i] = new AudioSourcePlayer(bundle);
        }
        audioSources = newAudioSources;
    }
}
