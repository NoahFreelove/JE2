package JE.Objects.Components.Pathfinding;

import JE.Manager;
import JE.Objects.Base.GameObject;
import JE.Objects.Components.Component;
import JE.Objects.Gizmos.Gizmo;
import org.joml.Vector2f;
import org.joml.Vector4f;

public class Pathfinding extends Component {
    public Vector2f[] path = new Vector2f[0];
    public Vector2f target = new Vector2f();
    public int pathIndex = 0;
    public boolean clockwise = true;
    public boolean isLooping = true;
    public boolean isPathfinding = false;
    public boolean isPathfindingComplete = false;
    public boolean isPathfindingFailed = false;
    public Vector2f objectStartPoint = new Vector2f();
    public float speed = 1f;
    public float percent = 0f;
    public float successRadius = 0.01f;
    public PathType pathType = PathType.YX;

    public Pathfinding(){
        path = new Vector2f[0];
    }
    public Pathfinding(Vector2f[] pathPoints){
        this.path = pathPoints;
    }
    public Pathfinding(Vector2f start, Vector2f end){
        path = new Vector2f[]{start, end};
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
                    clockwise = false;
                    pathIndex--;
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
                    clockwise = true;
                    pathIndex++;
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
        isPathfindingFailed = false;
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
            /*System.out.println("Start: " + start);
            System.out.println("End: " + end);
            System.out.println("Percent: " + percent);*/
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
    public void Update() {
        if(!isPathfinding)
            return;

        parentObject.getTransform().position = interpolate(parentObject.getTransform().position, target, percent);
        percent = clamp(percent + speed*Manager.DeltaTime(),0,1);

        nextPointCheck();
    }

    @Override
    public void Start() {

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
    public Gizmo[] getGizmos(){
        Gizmo[] gizmos = new Gizmo[path.length];
        for (int i = 0; i < path.length; i++) {
            gizmos[i] = new Gizmo();
            gizmos[i].getTransform().position = path[i];
            gizmos[i].getTransform().scale = new Vector2f(0.1f,0.1f);
            gizmos[i].renderer.baseColor = new Vector4f(0,0,1,1);
        }
        return gizmos;
    }
}
