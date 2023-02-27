package org.JE.JE2.Objects.Scripts.Pathfinding;

import org.JE.JE2.Manager;
import org.JE.JE2.Objects.GameObject;
import org.JE.JE2.Objects.Scripts.Base.Script;
import org.joml.Vector2f;

public class Pathfinding extends Script {
    public Vector2f[] path = new Vector2f[0];
    public Vector2f target = new Vector2f();
    public int pathIndex = 0;
    public boolean clockwise = true;
    public boolean isLooping = true;
    public PathFindLoopType loopType = PathFindLoopType.LOOP;
    public boolean isPathfinding = false;
    public boolean isPathfindingComplete = false;
    public Runnable onPathfindingComplete = () -> {};
    public boolean hasCalledOnPathfindingComplete = false;
    public Vector2f objectStartPoint = new Vector2f();
    public float speed = 1f;
    public float percent = 0f;
    public float successRadius = 0.01f;
    public PathType pathType = PathType.HYPOTENUSE;

    public Pathfinding(){
        path = new Vector2f[0];
    }

    public void generatePath(Vector2f start, Vector2f end){
        path = new Vector2f[]{start, end};
    }

    private Vector2f getNextPoint(){
        if(clockwise){
            if(pathIndex < path.length - 1){
                pathIndex++;
            }else{
                if(isLooping){
                    if(loopType == PathFindLoopType.LOOP) {
                        pathIndex = 0;
                    }
                    else if (loopType == PathFindLoopType.PINGPONG) {
                        clockwise = false;
                        pathIndex--;
                    }
                }else{
                    isPathfindingComplete = true;
                    if(!hasCalledOnPathfindingComplete)
                        onPathfindingComplete.run();
                    hasCalledOnPathfindingComplete = true;
                }
            }
        }
        else {
            if(pathIndex > 0){
                pathIndex--;
            }else{
                if(isLooping){
                    if(loopType == PathFindLoopType.LOOP){
                        pathIndex = path.length-1;
                    }
                    else if (loopType == PathFindLoopType.PINGPONG){
                        clockwise = true;
                        pathIndex++;
                    }
                }else{
                    isPathfindingComplete = true;
                    if(!hasCalledOnPathfindingComplete)
                        onPathfindingComplete.run();
                    hasCalledOnPathfindingComplete = true;
                }
            }
        }
        percent = 0;
        return path[pathIndex];
    }
    public void startPathfinding(){
        if(path.length > 0){
            target = path[0];
        }
        isPathfinding = true;
        Next();
    }
    public void Stop(){
        isPathfinding = false;
    }
    public void Resume(){
        isPathfinding = true;
    }
    public void Reset(){
        pathIndex = 0;
        percent = 0;
        isPathfindingComplete = false;
        isPathfinding = false;
        hasCalledOnPathfindingComplete = false;
    }

    public void Next(){
        target = getNextPoint();
    }
    private void nextPointCheck(){
        if(getAttachedObject().getTransform().position().distance(target) <= successRadius){
            Next();
        }
    }

    public Vector2f interpolate(Vector2f start, Vector2f end, float percent){

        boolean withinX = (Math.abs(start.x() - end.x()) <= successRadius);
        boolean withinY = (Math.abs(start.y() - end.y()) <= successRadius);

        if(pathType == PathType.HYPOTENUSE)
        {
            /*
            Logger.log("Start: " + start);
            Logger.log("End: " + end);
            Logger.log("Percent: " + percent);
            */
            return new Vector2f(start.x() + (end.x() - start.x()) * percent, start.y() + (end.y() - start.y()) * percent);
        }
        else if(pathType == PathType.XY)
        {

            if(withinY && withinX)
                return end;

            // If completed x
            if(withinX)
            {
                return new Vector2f(start.x(), start.y() + posNeg((end.y() - start.y()))  * speed);
            }
            // if not completed x
            // add movespeed to x
            return new Vector2f(start.x() + posNeg(end.x() - start.x()) * speed, start.y());
        }
        else if(pathType == PathType.YX)
        {

            if(withinX)
            {
                return new Vector2f(start.x() + posNeg(end.x() - start.x()) * speed, start.y());
            }
            return new Vector2f(start.x(), start.y() + posNeg(end.y() - start.y()) * speed);
        }
        return new Vector2f(0,0);
    }
    private float posNeg(float f){
        if(f < 0)
            return -1;
        return 1;
    }

    @Override
    public void update() {
        if(!isPathfinding)
            return;

        getAttachedObject().getTransform().setPosition(interpolate(getAttachedObject().getTransform().position(), target, percent));
        percent = clamp(percent + speed*Manager.deltaTime(),0,1);

        nextPointCheck();
    }

    @Override
    public void start() {

    }
    @Override
    public void onAddedToGameObject(GameObject o){
    }
    public void setIsRelative(boolean isRelative){
        for (Vector2f v :
                path) {
            if(isRelative){
                v.add(objectStartPoint);
            }
            else{
                v.sub(objectStartPoint);
            }
        }
    }

    private float clamp(float v, float min, float max){
        if(v > max)
            return max;
        return Math.max(v, min);
    }
}
