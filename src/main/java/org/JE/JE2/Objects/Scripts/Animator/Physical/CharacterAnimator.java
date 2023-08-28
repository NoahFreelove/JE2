package org.JE.JE2.Objects.Scripts.Animator.Physical;

import org.JE.JE2.Objects.Scripts.Script;
import org.JE.JE2.Objects.Scripts.Transform;
import org.JE.JE2.Rendering.Renderers.RenderSegment;
import org.JE.JE2.Utility.JE2Math;
import org.JE.JE2.Utility.Timeline.InterpolateFunctions.TransformLerpFunc;
import org.JE.JE2.Utility.Timeline.Timeline;
import org.JE.JE2.Utility.Timeline.Track;
import org.JE.JE2.Utility.Timeline.TrackPoint;
import org.joml.Vector2f;

import java.util.ArrayList;

public class CharacterAnimator extends Script {
    private RenderSegment[] parts;
    private Transform[] transforms;
    private ArrayList<Timeline> animationTimelines = new ArrayList<>();
    private int activeTimeline = 0;
    public CharacterAnimator(RenderSegment[] parts){
        this.parts = parts;
        transforms = RenderSegment.getTransforms(parts);
    }

    public void createAnim(RenderSegment ref, Vector2f[] posPoints, float[] times){
        if(posPoints.length != times.length)
        {
            throw new IllegalArgumentException("Character Animator: Points and Times array lengths do not match");
        }
        int index = -1;
        for (int i = 0; i < parts.length; i++) {
            if(ref == parts[i])
            {
                index = i;
                break;
            }
        }
        if(index == -1)
            return;

        TrackPoint<Transform>[] points = new TrackPoint[posPoints.length];
        for (int i = 0; i < points.length; i++) {
            Transform t = new Transform();
            t.setPosition(posPoints[i]);
            points[i] = new TrackPoint<>(t, times[i]);
        }
        Track<Transform> selected = new Track<>(new TransformLerpFunc(){},points);
        Timeline newTimeline = new Timeline();
        newTimeline.addTrack(selected);
        animationTimelines.add(newTimeline);
    }

    @Override
    public void update() {
        if(!animationTimelines.get(activeTimeline).playing())
            return;
        animationTimelines.get(activeTimeline).update();
        for (int i = 0; i < parts.length; i++) {
            parts[i].setAnimationTransform((Transform) animationTimelines.get(activeTimeline).getTrack(i).getRecentValue());
        }
    }
    public void play(){
        animationTimelines.get(activeTimeline).play();
    }
    public void pause(){
        animationTimelines.get(activeTimeline).pause();
    }
    public void reset(){
        animationTimelines.get(activeTimeline).reset();
    }
    public void stop(){
        animationTimelines.get(activeTimeline).stop();
    }
    public void setLoop(boolean loop){
        animationTimelines.get(activeTimeline).setLoop(loop);
    }
    public Timeline getActiveAnimationTimeline(){return animationTimelines.get(activeTimeline);}
    public void setActiveTimeline(int i){
        activeTimeline = JE2Math.clamp(i,0,animationTimelines.size()-1);
    }
}
