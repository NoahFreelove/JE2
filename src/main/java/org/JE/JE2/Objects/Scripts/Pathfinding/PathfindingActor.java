package org.JE.JE2.Objects.Scripts.Pathfinding;

import org.JE.JE2.Objects.GameObject;
import org.JE.JE2.Objects.Scripts.Script;
import org.JE.JE2.Utility.FloatExp;
import org.joml.Vector2f;

public class PathfindingActor extends Script {
    private PathfindingAgent pathfinder;
    private float speed;
    private boolean enableMovement = true;
    private GameObject targetObject;
    /*
    Snap in range isn't really noticeable for any successRange value under 0.1f
     */
    private float successRange = FloatExp.get(5,-2);
    private boolean snapInRange = true;
    public PathfindingActor(PathfindingAgent pathfinder, float speed) {
        this.pathfinder = pathfinder;
        this.speed = speed;
    }

    @Override
    public void update() {
        pathfinder.setPosition(getAttachedObject().getTransform().position());
        if(targetObject != null){
            pathfinder.setTarget(targetObject.getTransform().position());
        }
        if(enableMovement)
            moveTowards(pathfinder.nextDirection());
    }

    Vector2f addVec = new Vector2f();
    private void moveTowards(Vector2f dir){

        if(pathfinder.getPosition().distance(pathfinder.getTarget()) < successRange){
            if(snapInRange){
                getAttachedObject().getTransform().setPosition(pathfinder.getTarget());
            }
            return;
        }

        addVec.set(0,0).add(dir);
        addVec.mul(speed);
        getAttachedObject().getTransform().translate(addVec);
    }

    public void setTarget(Vector2f target){
        this.targetObject = null;
        pathfinder.setTarget(target);
    }
    public void setTargetObject(GameObject target){
        this.targetObject = target;
    }

    public void setAbleToMove(boolean val){
        this.enableMovement = val;
    }

    public float getSpeed() {
        return speed;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    public float getSuccessRange() {
        return successRange;
    }

    public void setSuccessRange(float successRange) {
        this.successRange = successRange;
    }

    public boolean isSnapInRange() {
        return snapInRange;
    }

    public void setSnapInRange(boolean snapInRange) {
        this.snapInRange = snapInRange;
    }
}
