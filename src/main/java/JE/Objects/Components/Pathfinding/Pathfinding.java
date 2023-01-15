package JE.Objects.Components.Pathfinding;

import JE.Manager;
import JE.Objects.Base.GameObject;
import JE.Objects.Components.Base.Component;
import JE.Objects.Gizmos.Gizmo;
import JE.Objects.Gizmos.GizmoParent;
import org.joml.Vector2f;
import org.joml.Vector4f;

import static org.lwjgl.opengl.GL11.*;

public class Pathfinding extends Component {
    public Vector2f[] path = new Vector2f[0];
    public Vector2f target = new Vector2f();
    public int pathIndex = 0;
    public boolean clockwise = true;
    public boolean isLooping = true;
    public PathFindLoopType loopType = PathFindLoopType.LOOP;
    public boolean isPathfinding = false;
    public boolean isPathfindingComplete = false;
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
    }

    public void Next(){
        target = getNextPoint();
    }
    private void nextPointCheck(){
        if(parentObject.getTransform().position.distance(target) <= successRadius){
            Next();
        }
    }

    public Vector2f interpolate(Vector2f start, Vector2f end, float percent){
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
            if(Math.abs(start.x() - end.x()) <= successRadius)
            {
                return new Vector2f(start.x(), start.y() + (end.y() - start.y()) * percent);
            }
            return new Vector2f(start.x() + (end.x() - start.x()) * percent, start.y());
        }
        else if(pathType == PathType.YX)
        {

            if(Math.abs(start.y() - end.y()) <= successRadius)
            {
                return new Vector2f(start.x() + (end.x() - start.x()) * percent, start.y());
            }
            return new Vector2f(start.x(), start.y() + (end.y() - start.y()) * percent);
        }
        return new Vector2f(0,0);
    }

    @Override
    public void update() {
        if(!isPathfinding)
            return;

        parentObject.getTransform().position = interpolate(parentObject.getTransform().position, target, percent);
        percent = clamp(percent + speed*Manager.deltaTime(),0,1);

        nextPointCheck();
    }

    @Override
    public void start() {

    }
    @Override
    public void onAddedToGameObject(GameObject o){
        objectStartPoint = o.getTransform().position;
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
    public GizmoParent getRangeGizmo(){

        Gizmo[] pointGizmos = new Gizmo[path.length];
        for (int i = 0; i < path.length; i++) {
            pointGizmos[i] = new Gizmo();
            pointGizmos[i].getTransform().position = path[i];
            pointGizmos[i].getTransform().scale = new Vector2f(0.1f,0.1f);
            pointGizmos[i].renderer.baseColor = new Vector4f(0,0,1,1);
        }
        GizmoParent gp = new GizmoParent(pointGizmos);

        Vector2f[] outline = new Vector2f[4];
        // get the min and max x and y values
        float minX = path[0].x();
        float maxX = path[0].x();
        float minY = path[0].y();
        float maxY = path[0].y();
        for (Vector2f v :
                path) {
            if(v.x() < minX)
                minX = v.x();
            if(v.x() > maxX)
                maxX = v.x();
            if(v.y() < minY)
                minY = v.y();
            if(v.y() > maxY)
                maxY = v.y();
        }
        outline[0] = new Vector2f(minX, minY);
        outline[1] = new Vector2f(maxX+1, minY);
        outline[2] = new Vector2f(maxX+1, maxY+1);
        outline[3] = new Vector2f(minX, maxY+1);
        // make line loop not start at 0,0
        Vector2f[] outline2 = new Vector2f[outline.length + 1];
        for (int i = 0; i < outline.length; i++) {
            outline2[i] = outline[i];
        }
        outline2[outline2.length - 1] = outline[0];

        Gizmo outlineGizmo = new Gizmo();
        outlineGizmo.setVertices(outline2);
        outlineGizmo.setBaseColor(new Vector4f(0.6f,0.7f,0.9f,0.2f));
        outlineGizmo.setDrawMode(GL_POLYGON);
        gp.gizmos.add(outlineGizmo);
        return gp;
    }
}
