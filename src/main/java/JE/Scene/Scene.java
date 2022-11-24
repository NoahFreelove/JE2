package JE.Scene;

import JE.Objects.Base.GameObject;
import JE.Objects.Gizmos.Gizmo;
import JE.Objects.Gizmos.GizmoParent;
import JE.Objects.Lights.PointLight;
import JE.Rendering.Camera;

import java.util.ArrayList;


public class Scene {
    public Camera activeCamera = new Camera();

    public final World world = new World();

    public void clear(){
        world.gameObjects.clear();
    }

    public void add(GameObject newGameObject)
    {
        if(newGameObject == null)
            return;
        if(world.gameObjects.contains(newGameObject))
            return;
        world.gameObjects.add(newGameObject);
    }

    public void add(GameObject... newGameObjects)
    {
        for (GameObject newGameObject : newGameObjects) {
            add(newGameObject);
        }
    }
    public void add(ArrayList<GameObject> newGameObjects)
    {
        for (GameObject newGameObject : newGameObjects) {
            add(newGameObject);
        }
    }

    public void remove(GameObject gameObject)
    {
        if(gameObject == null)
            return;
        if(!world.gameObjects.contains(gameObject))
            return;
        world.gameObjects.remove(gameObject);
    }

    public void addLight(PointLight light){
        if(light == null)
            return;
        if(world.lights.contains(light))
            return;
        world.lights.add(light);
    }
    public void addLight(PointLight... lights){
        for(PointLight light : lights){
            addLight(light);
        }
    }
    public void addLight(ArrayList<PointLight> lights){
        for(PointLight light : lights){
            addLight(light);
        }
    }

    public void removeLight(PointLight light){
        if(light == null)
            return;
        if(!world.lights.contains(light))
            return;
        world.lights.remove(light);
    }

    public void addGizmo(Gizmo gizmo){
        if(gizmo == null)
            return;
        if(world.gizmos.contains(gizmo))
            return;
        world.gizmos.add(gizmo);
    }

    public void addGizmo(Gizmo... gizmos){
        for(Gizmo gizmo : gizmos){
            addGizmo(gizmo);
        }
    }
    public void addGizmo(ArrayList<Gizmo> gizmos){
        for(Gizmo gizmo : gizmos){
            addGizmo(gizmo);
        }
    }
    public void addGizmo(GizmoParent gp){
        addGizmo(gp.gizmos);
    }

    public void removeGizmo(Gizmo gizmo){
        if(gizmo == null)
            return;
        if(!world.gizmos.contains(gizmo))
            return;
        world.gizmos.remove(gizmo);
    }

    public void update(){
        for (GameObject gameObject : world.gameObjects) {
            gameObject.Update();
            gameObject.ComponentUpdate();
        }
    }

    public void start(){
        for (GameObject gameObject : world.gameObjects) {
            gameObject.Start();
            gameObject.ComponentStart();
        }
    }
}
