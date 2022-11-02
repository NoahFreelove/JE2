package JE.Scene;

import JE.Objects.GameObject;
import JE.Objects.Identity;
import JE.Rendering.Camera;

import java.util.ArrayList;

public class Scene {
    public Camera activeCamera = new Camera();
    public ArrayList<GameObject> gameObjects = new ArrayList<>(){{
        add(new GameObject(new Identity("Object", "tag")));
        add(new GameObject(new Identity("Object2", "tag")));
    }};


    public void clear(){
        gameObjects.clear();
    }

    public void add(GameObject newGameObject)
    {
        if(newGameObject == null)
            return;
        if(gameObjects.contains(newGameObject))
            return;
        gameObjects.add(newGameObject);
    }
    public void remove(GameObject gameObject)
    {
        if(gameObject == null)
            return;
        if(!gameObjects.contains(gameObject))
            return;
        gameObjects.remove(gameObject);
    }

    public void update(){
        for (GameObject gameObject : gameObjects) {
            gameObject.Update();
        }
    }

    public void start(){
        for (GameObject gameObject : gameObjects) {
            gameObject.Start();
        }
    }
}
