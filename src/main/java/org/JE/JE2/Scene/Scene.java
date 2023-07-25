package org.JE.JE2.Scene;

import org.JE.JE2.Annotations.RequireNonNull;
import org.JE.JE2.Manager;
import org.JE.JE2.Objects.GameObject;
import org.JE.JE2.Objects.Identity;
import org.JE.JE2.Objects.Lights.Light;
import org.JE.JE2.Objects.Scripts.LambdaScript.ILambdaScript;
import org.JE.JE2.Objects.Scripts.LambdaScript.LambdaScript;
import org.JE.JE2.Objects.Scripts.Physics.PhysicsBody;
import org.JE.JE2.Objects.Scripts.Script;
import org.JE.JE2.Rendering.Camera;
import org.JE.JE2.Rendering.Texture;
import org.JE.JE2.UI.UIObjects.UIObject;
import org.JE.JE2.UI.UIObjects.UIWindow;
import org.JE.JE2.Utility.Time;
import org.JE.JE2.Utility.Watcher;

import java.io.*;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

public class Scene implements Serializable {

    private Camera activeCamera = new Camera();

    public final World world = new World();

    public int buildIndex = -1;
    public String name = "scene";

    public CopyOnWriteArrayList<Watcher> watchers = new CopyOnWriteArrayList<>();

    public boolean sortByZOnAdd = true;

    public Scene(){}
    public Scene(int buildIndex){
        this.buildIndex = buildIndex;
    }

    public void clear(){
        world.gameObjects.clear();
        world.lights.clear();
        world.physicsWorld = new org.jbox2d.dynamics.World(new org.jbox2d.common.Vec2(0,-9.8f));
    }

    /**
     * For when you don't care anymore and just want to add an object to the damn scene
     * @param go the object in question
     */
    public static void addNow(GameObject go){
        Manager.activeScene().add(go);
    }

    public void addTo(GameObject newGameObject, int index){
        if(newGameObject == null)
            return;
        if(world.gameObjects.contains(newGameObject)) {
            return;

        }

        for (Script s :
                newGameObject.getScripts()) {
            if (s instanceof Light light)
            {
                addLight(light);
            }
        }

        world.gameObjects.add(index,newGameObject);
        for (GameObject child :
                newGameObject.getChildren()) {
            addTo(child, index);
        }
        newGameObject.linkedScene = this;
        newGameObject.scriptParentAdded(this);
        if(sortByZOnAdd)
            sortByZ();
    }

    public void add(Script s){
        GameObject temp = new GameObject();
        temp.addScript(s);
        add(temp);
    }
    public void add(ILambdaScript s){
        add(new LambdaScript(s));
    }

    public void add(GameObject newGameObject)
    {
        addTo(newGameObject, world.gameObjects.size());
    }

    private void sortByZ() {
        world.gameObjects.sort(Comparator.comparingDouble(o -> o.getTransform().zPos()));
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
        gameObject.setActive(false);

        if(!world.gameObjects.contains(gameObject))
            return;

        gameObject.scriptDestroy();

        for (GameObject child :
                gameObject.getChildren()) {
            child.scriptDestroy();
        }
        for (PhysicsBody body :
                gameObject.getScripts(PhysicsBody.class)) {
            world.physicsWorld.destroyBody(body.body);
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
        update(world.supportPhysics);
    }

    public void update(boolean physicsUpdate) {
        if (physicsUpdate){
            world.physicsWorld.step(Time.deltaTime(), 8, 3);
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
        world.UI.forEach(ui ->{
            if(ui instanceof UIWindow window){
                if(!window.destroyOnLoad)
                    newScene.addUI(window);
            }
        });
    }

    public Camera mainCamera() {
        return activeCamera;
    }
    public void setCamera(Camera cam){
        this.activeCamera = cam;
    }

    public boolean setIfExists(Identity id, GameObject object, boolean strict){
        int i = 0;
        for (GameObject go : world.gameObjects) {
            if(strict){
                if(go.uniqueID() == id.uniqueID)
                {
                    world.gameObjects.set(i,object);
                    return true;
                }
            }else{
                if(go.uniqueID() == id.uniqueID || (go.name.equals(id.name) && go.tag.equals(id.tag)))
                {
                    world.gameObjects.set(i,object);
                    return true;
                }
            }
            i++;
        }
        return false;
    }
}

