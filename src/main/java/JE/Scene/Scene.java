package JE.Scene;

import JE.Objects.Base.GameObject;
import JE.Objects.Lights.Light;
import JE.Rendering.Camera;


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

    public void remove(GameObject gameObject)
    {
        if(gameObject == null)
            return;
        if(!world.gameObjects.contains(gameObject))
            return;
        world.gameObjects.remove(gameObject);
    }

    public void addLight(Light light){
        if(light == null)
            return;
        if(world.lights.contains(light))
            return;
        world.lights.add(light);
    }

    public void removeLight(Light light){
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
