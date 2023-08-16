package org.JE.JE2.Objects.Scripts.Animator.Sprite;

import org.JE.JE2.IO.Logging.Errors.JE2Error;
import org.JE.JE2.IO.Logging.Logger;
import org.JE.JE2.Objects.Scripts.Script;
import org.JE.JE2.Rendering.Renderers.TextureSegment;

import java.util.ArrayList;

public class SpriteAnimator extends Script {
    private final ArrayList<SpriteAnimationTimeline> spriteAnimationTimelines = new ArrayList<>();
    public int state = 0;
    private final TextureSegment target;
    public SpriteAnimator(TextureSegment target){
        this.target = target;
    }

    public void addTimelines(SpriteAnimationTimeline... timelines){
        spriteAnimationTimelines.addAll(java.util.Arrays.asList(timelines));

    }

    @Override
    public void update() {
        if(getAttachedObject()==null)
            return;
        try {
            if(state>= spriteAnimationTimelines.size())
            {
                outOfRangeError("update",0);
                return;
            }
            spriteAnimationTimelines.get(state).animUpdate(target);
        }
        catch (NullPointerException e){
            Logger.log(new JE2Error("Sprite Animator Error: Could not update animation as parent object does not have a sprite renderer"));
        }
    }
    public void play(){
        if(state>= spriteAnimationTimelines.size())
        {
            outOfRangeError("play",Logger.ERROR);
            return;
        }
        spriteAnimationTimelines.get(state).play();
    }
    public void restart(){
        if(state>= spriteAnimationTimelines.size())
        {
            outOfRangeError("restart",Logger.ERROR);
            return;
        }
        spriteAnimationTimelines.get(state).restart();
    }
    public void pause(){
        if(state>= spriteAnimationTimelines.size())
        {
            outOfRangeError("pause",Logger.ERROR);
            return;
        }
        spriteAnimationTimelines.get(state).pause();}
    public void stop(){
        if(state>= spriteAnimationTimelines.size())
        {
            outOfRangeError("stop",Logger.ERROR);
            return;
        }
        spriteAnimationTimelines.get(state).stop();
    }
    public void setPlaybackPosition(int pos){
        if(state>= spriteAnimationTimelines.size())
        {
            outOfRangeError("set playback position", Logger.ERROR);
            return;
        }
        spriteAnimationTimelines.get(state).position = pos;
    }

    private void outOfRangeError(String action, int level){
        Logger.log(new JE2Error("Sprite Animator Error: There are only " + spriteAnimationTimelines.size() + " timelines but timeline " + (state+1) + " is trying to " + action),level);

    }
}
