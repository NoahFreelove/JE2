package org.JE.JE2.Objects.Scripts.Animator.Physical;

import org.JE.JE2.Objects.Scripts.Script;
import org.JE.JE2.Objects.Scripts.Transform;
import org.JE.JE2.Rendering.Renderers.RenderSegment;
import org.JE.JE2.Rendering.Renderers.TextureSegment;
import org.JE.JE2.Utility.Timeline.InterpolateFunctions.TransformLerpFunc;
import org.JE.JE2.Utility.Timeline.Timeline;
import org.JE.JE2.Utility.Timeline.Track;
import org.JE.JE2.Utility.Timeline.TrackPoint;
import org.joml.Vector2f;

public class CharacterAnimator extends Script {
    private RenderSegment[] parts;
    private Transform[] transforms;
    private Timeline animationTimeline;

    public CharacterAnimator(RenderSegment[] parts){
        this.parts = parts;
        transforms = RenderSegment.getTransforms(parts);
        animationTimeline = new Timeline();
        for (Transform transform : transforms) {
            TrackPoint<Transform> start = new TrackPoint<>(transform, 0);
            TrackPoint<Transform> end = new TrackPoint<>(transform, 1);
            Track<Transform> track = new Track<>(new TransformLerpFunc() {
            }, start, end);
            animationTimeline.addTrack(track);
        }
    }

    public void createAnim(RenderSegment ref, Vector2f... posPoints){
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

        Track<Transform> selected = (Track<Transform>) animationTimeline.getTrack(index);
        TrackPoint<Transform>[] points = new TrackPoint[posPoints.length];
        for (int i = 0; i < points.length; i++) {
            Transform t = new Transform();
            t.setPosition(posPoints[i]);
            float time = (float) i / (float) points.length;
            if(i == 0)
                time = 0;
            else if(i == points.length-1)
                time = 1;
            points[i] = new TrackPoint<>(t, time);
        }
        selected.setPoints(points);

    }

    @Override
    public void update() {
        if(!animationTimeline.playing())
            return;
        animationTimeline.update();
        for (int i = 0; i < parts.length; i++) {
            parts[i].setRelativeTransform((Transform) animationTimeline.getTrack(i).getRecentValue());
        }
    }
    public void play(){
        animationTimeline.play();
    }
    public void pause(){
        animationTimeline.pause();
    }
    public void setLoop(boolean loop){
        animationTimeline.setLoop(loop);
    }
}
