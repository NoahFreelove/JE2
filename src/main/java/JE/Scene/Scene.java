package JE.Scene;

import JE.Objects.Base.GameObject;
import JE.Objects.Lights.PointLight;
import JE.Rendering.Camera;

import java.awt.*;
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
