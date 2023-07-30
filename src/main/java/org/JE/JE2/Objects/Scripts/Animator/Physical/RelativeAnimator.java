package org.JE.JE2.Objects.Scripts.Animator.Physical;

import org.JE.JE2.Objects.GameObject;
import org.JE.JE2.Objects.Scripts.Script;
import org.JE.JE2.Utility.JE2Math;
import org.JE.JE2.Utility.Time;
import org.joml.Vector2f;

public class RelativeAnimator extends Script {
    private GameObject object;
    private Vector2f[] relativePosPoints;
    private float timePerPoint;
    private float t;
    private int point;

    private boolean completed = false;
    public RelativeAnimator(Vector2f[] relativePosPoints, float timePerPoint) {
        this.relativePosPoints = relativePosPoints;
        this.timePerPoint = timePerPoint;
        point = 0;
        t = 0;
    }

    Vector2f objectStartPos;
    @Override
    public void update() {
        if(object == null)
            return;

        if(point >= relativePosPoints.length)
        {
            completed = true;
            return;
        }

        float timeMultiplier = 1/timePerPoint * Time.deltaTime();


        Vector2f startPos;
        if(point == 0)
        {
            startPos = new Vector2f(0,0);
            if(t == 0)
                objectStartPos = new Vector2f(object.getTransform().position());
        }
        else
            startPos = relativePosPoints[point-1];
        if(point < relativePosPoints.length){
            Vector2f targetPos = relativePosPoints[point];
            // Interpolate between the two points with t representing time between 0 and 1
            Vector2f newPos = lerp(startPos, targetPos, t);
            newPos.x += objectStartPos.x;
            newPos.y += objectStartPos.y;
            object.getTransform().setPosition(newPos);
        }

        t+= timeMultiplier;
        t = JE2Math.clamp(t);

        if(t == 1){
            point++;
            t = 0;
            //objectStartPos = new Vector2f(object.getTransform().position());
        }

    }

    private Vector2f lerp(Vector2f a, Vector2f b, float t){
        return new Vector2f(a.x() + (b.x() - a.x()) * t, a.y() + (b.y() - a.y()) * t);
    }

    @Override
    public void onAddedToGameObject(GameObject gameObject) {
        this.object = gameObject;
    }

    public GameObject getObject() {
        return object;
    }

    public Vector2f[] getRelativePosPoints() {
        return relativePosPoints;
    }

    public float getTimePerPoint() {
        return timePerPoint;
    }

    public float getT() {
        return t;
    }

    public int getPoint() {
        return point;
    }

    public boolean hasCompleted(){
        return completed;
    }
}
