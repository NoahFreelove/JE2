package org.JE.JE2.Scene;

import org.JE.JE2.Annotations.RequireNonNull;
import org.JE.JE2.Manager;
import org.JE.JE2.Objects.GameObject;
import org.JE.JE2.Objects.Identity;
import org.JE.JE2.Objects.Lights.Light;
import org.JE.JE2.Objects.Scripts.Base.Script;
import org.JE.JE2.Objects.Scripts.Common.Transform;
import org.JE.JE2.Rendering.Camera;
import org.JE.JE2.Resources.ResourceLoader;
import org.JE.JE2.UI.UIObjects.UIObject;
import org.JE.JE2.Utility.Watcher;

import java.io.*;
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

        for (Script s :
                newGameObject.getScripts()) {
            if (s instanceof Light light)
            {
                addLight(light);
            }
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

    public Scene load(String filepath){
        ArrayList<String> lines = new ArrayList<>(List.of(ResourceLoader.getBytesAsString(filepath)));

        /*try {
            Scanner scanner = new Scanner(new File());

            // read all lines to String[]
            while (scanner.hasNextLine()) {
                lines.add(scanner.nextLine());
            }
        }catch (Exception e){
            System.out.println("error while reading from file: " + filepath);
            e.printStackTrace();
        }*/

        GameObject gameObject = new GameObject();
        for (String line : lines) {
            if(line.equals("start"))
                gameObject = new GameObject();
            else if(line.equals("end"))
                add(gameObject);
            else if(line.startsWith("id:"))
                gameObject.setIdentity((Identity)deserialize(line.substring(3)));
            else{
                Script readScript = (Script) deserialize(line);
                readScript.load();

                if(readScript instanceof Transform t){
                    t.setAttachedObject(gameObject);
                    gameObject.setScript(0,t);
                }
                else {
                    gameObject.addScript(readScript);
                }
            }
        }

        return this;
    }

    private static Object deserialize(String input){
        try {
            byte[] bytes = Base64.getDecoder().decode(input);
            ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
            ObjectInputStream ois = new ObjectInputStream(bais);
            Object o = ois.readObject();
            ois.close();
            return o;
        }
        catch (Exception ignore){
            System.out.println("error deserializing: " + input);
            return new Object();
        }
    }

}

