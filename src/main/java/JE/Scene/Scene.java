package JE.Scene;

import JE.Objects.Base.GameObject;
import JE.Objects.Gizmos.Gizmo;
import JE.Objects.Gizmos.GizmoParent;
import JE.Objects.Lights.PointLight;
import JE.Rendering.Camera;
import JE.Utility.Watcher;

import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;


public class Scene {

    public Camera activeCamera = new Camera();

    public final World world = new World();

    public CopyOnWriteArrayList<Watcher> watchers = new CopyOnWriteArrayList<>();

    public void clear(){
        world.gameObjects.clear();
        world.lights.clear();
        world.gizmos.clear();
        world.physicsWorld = new org.jbox2d.dynamics.World(new org.jbox2d.common.Vec2(0,-9.8f));
    }

    public void add(GameObject newGameObject)
    {
        if(newGameObject == null)
            return;
        if(world.gameObjects.contains(newGameObject)) {
            return;
        }
        world.gameObjects.add(newGameObject);
        newGameObject.componentGameObjectAddedToScene(this);
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
        world.gameObjects.forEach(GameObject::update);
        world.gameObjects.forEach(GameObject::componentUpdate);
    }

    public void start(){
        world.gameObjects.forEach(GameObject::start);
        world.gameObjects.forEach(GameObject::componentStart);
    }

    public void unload(Scene oldScene, Scene newScene) {
        world.gameObjects.forEach(GameObject::unload);
        world.gameObjects.forEach(gameObject -> gameObject.componentUnload(oldScene,newScene));
    }
}
