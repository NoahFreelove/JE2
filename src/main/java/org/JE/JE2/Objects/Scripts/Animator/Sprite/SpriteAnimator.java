package org.JE.JE2.Objects.Scripts.Animator.Sprite;

import org.JE.JE2.IO.Logging.Errors.JE2Error;
import org.JE.JE2.IO.Logging.Logger;
import org.JE.JE2.Objects.Scripts.Base.Script;

import java.util.ArrayList;

public class SpriteAnimator extends Script {
    private final ArrayList<SpriteAnimationTimeline> spriteAnimationTimelines = new ArrayList<>();
    public int state = 0;

    public SpriteAnimator(){
        spriteAnimationTimelines.add(new SpriteAnimationTimeline());
    }

    public void addTimelines(SpriteAnimationTimeline... timelines){
        spriteAnimationTimelines.addAll(java.util.Arrays.asList(timelines));

    }

    @Override
    public void update() {
        if(getAttachedObject()==null)
            return;
        try {
            spriteAnimationTimelines.get(state).AnimUpdate(getAttachedObject().getSpriteRenderer());
        }
        catch (Exception e){
            Logger.log(new JE2Error("Sprite Animator", "Could not update animation as parent object could not be cast to a sprite"));
        }
    }

    @Override
    public void start() {

    }

    @Override
    public void awake() {}

    public void Play(){
        spriteAnimationTimelines.get(state).Play();
    }
    public void Restart(){
        spriteAnimationTimelines.get(state).Restart();
    }
    public void Pause(){
        spriteAnimationTimelines.get(state).Pause();}
    public void Stop(){
        spriteAnimationTimelines.get(state).Stop();}
    public void SetPlaybackPosition(int pos){
        spriteAnimationTimelines.get(state).position = pos;}

}
