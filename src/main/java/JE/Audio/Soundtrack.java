package JE.Audio;

import JE.Manager;
import JE.Utility.Watcher;

import java.util.Arrays;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * JE2 - Soundtrack
 * Soundtracks are meant for world ambiance. You can switch between tracks as you please.
 */
public class Soundtrack {
    CopyOnWriteArrayList<AudioSourcePlayer> trackList = new CopyOnWriteArrayList<>();
    int currentTrack = 0;
    AudioSourcePlayer activeTrack;
    Watcher trackEndWatcher = () -> {
        //System.out.println(activeTrack.getDecimal());
        if(activeTrack.getDecimal() >=0.999f)
        {
            //nextTrack();
        }
    };

    public Soundtrack(AudioSourcePlayer... tracks){
        System.out.println(tracks[0].getDuration());
        System.out.println(tracks[1].getDuration());
        trackList.addAll(Arrays.asList(tracks));



        trackList.forEach(soundPlayer -> soundPlayer.setLoops(false));
        activeTrack = trackList.get(0);
    }
    public void addToWorld(){
        Manager.activeScene().watchers.add(trackEndWatcher);
    }

    public void addTrack(AudioSourcePlayer track){
        trackList.add(track);
    }
    public void removeTrack(AudioSourcePlayer track){
        trackList.remove(track);
    }
    public void removeTrack(int index){
        trackList.remove(index);
    }
    public void playTrack(int index){
        trackList.get(currentTrack).stop();
        currentTrack = index;
        trackList.get(currentTrack).play();
        activeTrack = trackList.get(currentTrack);
    }
    public void start(){
        currentTrack = 0;
        trackList.get(0).play();
        activeTrack = trackList.get(0);
    }
    public void nextTrack(){
        trackList.get(currentTrack).stop();
        currentTrack++;
        if(currentTrack >= trackList.size())
            currentTrack = 0;

        playTrack(currentTrack);
    }
}
