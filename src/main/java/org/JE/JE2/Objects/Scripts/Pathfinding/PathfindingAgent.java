package org.JE.JE2.Objects.Scripts.Pathfinding;

import org.joml.Vector2f;

public interface PathfindingAgent {

    Vector2f nextDirection();
    void setTarget(Vector2f point);
    void setPosition(Vector2f point);
    Vector2f getTarget();
    Vector2f getPosition();

}
