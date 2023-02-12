package JE.Scene;

import JE.Annotations.RequireNonNull;
import JE.Manager;
import JE.Objects.GameObject;
import JE.Objects.Identity;
import JE.Objects.Lights.Light;
import JE.Objects.Scripts.Base.Script;
import JE.Rendering.Camera;
import JE.UI.UIObjects.UIObject;
import JE.Utility.Watcher;

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

    public void loadFromFile(String filepath){
        World w = SceneLoader.loadFromFile(filepath).world;
        world.gameObjects = w.gameObjects;
    }

    private static class SceneLoader{
        public static Scene loadFromFile(String filePath){
            Scene scene = new Scene();
            try {
                Scanner scanner = new Scanner(new FileInputStream(filePath));
                boolean inScript = false;
                GameObject go = null;
                Script sc = null;

                while (scanner.hasNext()){
                    String next = scanner.nextLine().replace("\n","");

                    if(next.equals("SCRIPT")){
                        if(sc !=null)
                        {
                            if(go!=null)
                                go.addScript(sc);
                        }
                        inScript = true;
                        String className = scanner.nextLine();
                        sc = (Script) Class.forName(className).cast(FromString(scanner.nextLine()));
                    }

                    if(next.equals("OBJECT")) {

                        if (go != null)
                        {
                            if(inScript){
                                go.addScript(sc);
                            }
                            scene.add(go);
                        }
                        inScript = false;
                        go = new GameObject();

                        go.setIdentity(new Identity(scanner.nextLine(), scanner.nextLine()));
                        go = (GameObject) FromString(scanner.nextLine());
                        go.setChildren(new ArrayList<>());
                        go.setScripts(new CopyOnWriteArrayList<>());
                    }
                }

                if (go != null)
                {
                    if(inScript){
                        go.addScript(sc);
                    }

                    scene.add(go);
                }
            } catch (IOException | ClassNotFoundException e) {
                throw new RuntimeException(e);
            }

            for (GameObject gameObject :
                    scene.world.gameObjects) {
                for (Script script :
                        gameObject.getScripts()) {
                    script.onLoaded();
                }
            }
            return scene;
        }

        private static Object FromString( String s ) throws IOException, ClassNotFoundException {
            s = s.replace("[","").replace("]","");
            String[] split = s.split(", ");
            byte[] bytes = new byte[split.length];
            for (int i = 0; i < split.length; i++) {
                bytes[i] = Byte.parseByte(split[i]);
            }
            System.out.println("PARSED: " + Arrays.toString(bytes));

            ObjectInputStream Object_Input_Stream = new ObjectInputStream( new ByteArrayInputStream(bytes) );
            Object Demo_Object  = Object_Input_Stream.readObject();
            Object_Input_Stream.close();
            return Demo_Object;
        }

    }
}

