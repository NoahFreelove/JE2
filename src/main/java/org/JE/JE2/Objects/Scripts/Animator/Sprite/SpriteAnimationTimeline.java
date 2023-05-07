package org.JE.JE2.Objects.Scripts.Animator.Sprite;

import org.JE.JE2.Rendering.Renderers.SpriteRenderer;

import java.util.ArrayList;
import java.util.Date;

public class SpriteAnimationTimeline {
    boolean isPlaying = false;
    boolean loops = true;
    float totalDuration = 0f;
    public int position;
    ArrayList<SpriteAnimationFrame> frames = new ArrayList<>();
    long updateDelta = 0;
    float currentFrameDuration = 0f;

    public SpriteAnimationTimeline(SpriteAnimationFrame... spriteAnimationFrames){
        System.out.println(spriteAnimationFrames.length);
        frames.addAll(java.util.Arrays.asList(spriteAnimationFrames));
        System.out.println(frames.size());

        for(SpriteAnimationFrame frame : frames){
            totalDuration += frame.duration;
        }
        updateDelta = new Date().getTime();
        calculateDuration();
    }

    public void restart(){
        isPlaying = true;
        position = 0;
        currentFrameDuration = 0;
    }
    public void play(){
        isPlaying = true;
    }
    public void pause(){
        isPlaying = false;
    }
    public void stop(){
        isPlaying = false;
        position = 0;
        currentFrameDuration = 0;
    }

    public void animUpdate(SpriteRenderer sprite){
        float newDelta = System.currentTimeMillis() - updateDelta;
        updateDelta = System.currentTimeMillis();


        if(!isPlaying)
            return;

        currentFrameDuration-=newDelta;

        if(currentFrameDuration<=0){
            position++;
            if(position >= frames.size()){
                position = 0;
                if(!loops)
                    stop();
            }
            if(position >= frames.size())
                return;
            currentFrameDuration = frames.get(position).duration;
            frames.get(position).Activate(sprite);
        }
    }

    public void setFrame(SpriteAnimationFrame newFrame, int index){
        if(index >= frames.size())
            return;
        frames.set(index, newFrame);
        calculateDuration();
    }

    public void addFrame(SpriteAnimationFrame frame){
        frames.add(frame);
        calculateDuration();
    }

    public void removeFrame(SpriteAnimationFrame frame){
        frames.remove(frame);
        calculateDuration();
    }
    public void removeFrame(int index){
        if(index >= frames.size())
            return;
        frames.remove(index);
        calculateDuration();
    }
    public void injectFrame(SpriteAnimationFrame frame, int index){
        if(index >= frames.size())
            return;
        frames.add(index, frame);
        calculateDuration();
    }

    private void calculateDuration(){
        totalDuration = 0f;
        for (SpriteAnimationFrame frame : frames) {
            totalDuration += frame.duration;
        }
    }

}
