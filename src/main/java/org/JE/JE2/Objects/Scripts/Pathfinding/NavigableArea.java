package org.JE.JE2.Objects.Scripts.Pathfinding;


import org.JE.JE2.Objects.GameObject;
import org.JE.JE2.Rendering.Debug.RenderColoredArea;
import org.joml.Vector2f;

public class NavigableArea {

    /*
    Simple Pathfinding Agent
     */
    Vector2f boundOne;
    Vector2f boundTwo;

    /*
    Foundation for A*
     */
    float nodeSize;
    int width;
    int height;
    boolean[][] navigableArea;

    public NavigableArea(Vector2f boundOne, Vector2f boundTwo) {
        this.boundOne = boundOne;
        this.boundTwo = boundTwo;
    }

    public NavigableArea(Vector2f boundOne, Vector2f boundTwo, float nodeSize) {
        this.boundOne = boundOne;
        this.boundTwo = boundTwo;
        this.nodeSize = nodeSize;

        float deltaY = boundTwo.y() - boundOne.y();
        height = ((int) (deltaY/nodeSize)) + 1;

        float deltaX = boundTwo.x() - boundOne.x();
        width = ((int) (deltaX / nodeSize)) + 1;

        navigableArea = new boolean[width][height];
    }

    public GameObject getDebugArea(){
        return RenderColoredArea.getArea(boundOne.x(),boundOne.y(),boundTwo.x()-boundOne.x(), boundTwo.y() - boundOne.y());
    }

    public boolean withinBounds(Vector2f point){
        return point.x() >= boundOne.x() && point.x() <= boundTwo.x() && point.y() >= boundOne.y() && point.y() <= boundTwo.y();
    }
}
