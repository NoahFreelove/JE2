package JE.Objects.Components.Animator.Sprite;

import JE.Logging.Errors.JE2Error;
import JE.Logging.Logger;
import JE.Objects.Base.Sprites.Sprite;
import JE.Objects.Components.Base.Component;

import java.util.ArrayList;

public class SpriteAnimator extends Component {
    private final ArrayList<SpriteAnimationTimeline> spriteAnimationTimelines = new ArrayList<>();
    public int state = 0;

    public SpriteAnimator(){
        spriteAnimationTimelines.add(new SpriteAnimationTimeline());
    }
    public SpriteAnimator(SpriteAnimationFrame... frames){
        spriteAnimationTimelines.add(new SpriteAnimationTimeline(frames));
    }

    public SpriteAnimator(SpriteAnimationTimeline... timelines){
        spriteAnimationTimelines.addAll(java.util.Arrays.asList(timelines));
    }

    @Override
    public void update() {
        if(parentObject==null)
            return;
        try {
            spriteAnimationTimelines.get(state).AnimUpdate((Sprite)parentObject);
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
