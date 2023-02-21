package JE.Scene;

import JE.Annotations.RequireNonNull;
import JE.Manager;
import JE.Objects.GameObject;
import JE.Objects.Identity;
import JE.Objects.Lights.Light;
import JE.Objects.Scripts.Base.Script;
import JE.Rendering.Camera;
import JE.Resources.ResourceLoader;
import JE.UI.UIObjects.UIObject;
import JE.Utility.Watcher;

import java.awt.*;
import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;


public class Scene implements Serializable {

    private Camera activeCamera = new Camera();

    public final World world = new World();

    public CopyOnWriteArrayList<Watcher> watchers = new CopyOnWriteArrayList<>();

    public void clear(){
        world.gameObjects.clear();
        world.lights.clear();
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
        for (GameObject child :
                newGameObject.getChildren()) {
            add(child);
        }
        newGameObject.linkedScene = this;
        newGameObject.scriptParentAdded(this);
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

    @RequireNonNull
    public void remove(GameObject gameObject)
    {
        Objects.requireNonNull(gameObject);
        if(!world.gameObjects.contains(gameObject))
            return;
        gameObject.scriptDestroy();

        for (GameObject child :
                gameObject.getChildren()) {
            child.scriptDestroy();
        }
        world.gameObjects.remove(gameObject);
    }

    public void addLight(Light light){
        if(light == null)
            return;
        if(world.lights.contains(light))
            return;
        world.lights.add(light);
    }
    public void addLight(Light... lights){
        for(Light light : lights){
            addLight(light);
        }
    }
    public void addLight(ArrayList<Light> lights){
        for(Light light : lights){
            addLight(light);
        }
    }
    public void addUI(UIObject ui){
        if(ui == null)
            return;
        if(world.UI.contains(ui))
            return;
        world.UI.add(ui);
    }
    public void addUI(UIObject... ui){
        for(UIObject ui1 : ui){
            addUI(ui1);
        }
    }
    public void removeUI(UIObject ui){
        if(ui == null)
            return;
        if(!world.UI.contains(ui))
            return;
        world.UI.remove(ui);
    }

    public void removeLight(Light light){
        if(light == null)
            return;
        if(!world.lights.contains(light))
            return;
        world.lights.remove(light);
    }

    public void update(){
        update(true);
    }

    public void update(boolean physicsUpdate) {
        if (physicsUpdate){
            world.physicsWorld.step(Manager.deltaTime(), 6, 2);
            world.gameObjects.forEach(GameObject::physicsUpdate);
        }
        world.gameObjects.forEach(GameObject::scriptUpdate);
        world.UI.forEach(UIObject::update);
    }

    public void start(){
        world.gameObjects.forEach(GameObject::scriptStart);
    }

    public void unload(Scene oldScene, Scene newScene) {
        world.gameObjects.forEach(gameObject -> gameObject.scriptUnload(oldScene,newScene));
    }

    public Camera mainCamera() {
        return activeCamera;
    }
    public void setCamera(Camera cam){
        this.activeCamera = cam;
    }

    public Scene load(String worldPath){
        String fullPath = ResourceLoader.get(worldPath);
        return this;
    }

}

