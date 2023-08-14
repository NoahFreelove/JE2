package org.JE.JE2.Utility.Timeline;

import org.JE.JE2.Objects.Scripts.Script;
import org.JE.JE2.Utility.JE2Math;
import org.JE.JE2.Utility.Time;

import java.util.ArrayList;

public class Timeline extends Script {
    private float time = 0;
    private float start = 0;
    private float end = 1;
    private boolean playing = false;
    private final ArrayList<Track<?>> tracks = new ArrayList<>();

    public Timeline() {
    }

    public void addTrack(Track<?> track){
        tracks.add(track);
    }

    @Override
    public void update(){
        update(Time.deltaTime());
    }


    public void update(float deltaTime){
        if(!playing)
            return;
        time = JE2Math.clamp(time+deltaTime,start,end);
        for (Track<?> track : tracks) {
            track.update(time);
        }
    }

    public Track<?> getTrack(int i){
        if(i < tracks.size() && i >= 0)
            return tracks.get(i);
        return null;
    }

    public Track<?>[] getTracks(){
        return tracks.toArray(new Track[0]);
    }

    public float getTime() {
        return time;
    }

    public void reset() {
        this.time = 0;
        this.playing = true;
    }

    public void play(){
        this.playing = true;
    }

    public void pause(){
        this.playing = false;
    }

    public void stop(){
        this.playing = false;
        this.time = 0;
    }

    public float getStart() {
        return start;
    }

    public void setStart(float start) {
        this.start = start;
    }

    public float getEnd() {
        return end;
    }

    public void setEnd(float end) {
        this.end = end;
    }
}
