package org.JE.JE2.Objects.Scripts.Pathfinding;

import org.joml.Vector2f;

public class SimplePathfindingAgent implements PathfindingAgent {

    private NavigableArea area;
    private Vector2f target = new Vector2f();
    private Vector2f position = new Vector2f();
    private Vector2f direction = new Vector2f();

    public SimplePathfindingAgent(NavigableArea area) {
        this.area = area;
    }


    public NavigableArea getArea() {
        return area;
    }

    public void setArea(NavigableArea area) {
        this.area = area;
    }

    public Vector2f getTarget() {
        return target;
    }

    @Override
    public Vector2f getPosition() {
        return position;
    }

    @Override
    public void setTarget(Vector2f target) {
        this.target = target;
    }

    @Override
    public void setPosition(Vector2f point) {
        this.position = point;
    }


    Vector2f disposable = new Vector2f();

    @Override
    public Vector2f nextDirection() {
        disposable.set(0,0);
        // checks for bounds and /0 errors
        if(!area.withinBounds(target) || !area.withinBounds(position))
            return disposable;

        if(position.equals(target.x(),target.y()))
            return disposable;

        disposable.set(target);

        // Actual logic for getting direction
        direction = disposable.sub(position).normalize();

        return direction;
    }
}
