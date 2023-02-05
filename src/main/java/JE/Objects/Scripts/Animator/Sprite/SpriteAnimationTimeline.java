package JE.Objects.Scripts.Animator.Sprite;

import JE.Rendering.Renderers.SpriteRenderer;

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
        frames.addAll(java.util.Arrays.asList(spriteAnimationFrames));
        for(SpriteAnimationFrame frame : frames){
            totalDuration += frame.duration;
        }
        updateDelta = new Date().getTime();
        calculateDuration();
    }

    public void Restart(){
        isPlaying = true;
        position = 0;
        currentFrameDuration = 0;
    }
    public void Play(){
        isPlaying = true;
    }
    public void Pause(){
        isPlaying = false;
    }
    public void Stop(){
        isPlaying = false;
        position = 0;
        currentFrameDuration = 0;
    }

    public void AnimUpdate(SpriteRenderer sprite){
        float newDelta = new Date().getTime() - updateDelta;
        updateDelta = new Date().getTime();

        if(!isPlaying)
            return;

        currentFrameDuration-=newDelta;

        if(currentFrameDuration<=0){
            position++;
            if(position >= frames.size()){
                position = 0;
                if(!loops)
                    Stop();
            }
            currentFrameDuration = frames.get(position).duration;
            frames.get(position).Activate(sprite);
        }
    }

    public void SetFrame(int index, SpriteAnimationFrame newFrame){
        if(index >= frames.size())
            return;
        frames.set(index, newFrame);
        calculateDuration();
    }

    public void AddFrame(SpriteAnimationFrame frame){
        frames.add(frame);
        calculateDuration();
    }

    public void RemoveFrame(SpriteAnimationFrame frame){
        frames.remove(frame);
        calculateDuration();
    }
    public void RemoveFrame(int index){
        if(index >= frames.size())
            return;
        frames.remove(index);
        calculateDuration();
    }

    private void calculateDuration(){
        totalDuration = 0f;
        for (SpriteAnimationFrame frame : frames) {
            totalDuration += frame.duration;
        }
    }

}
