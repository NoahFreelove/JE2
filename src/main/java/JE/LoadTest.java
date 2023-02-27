package JE;

import JE.Objects.GameObject;
import JE.Objects.Lights.PointLight;
import JE.Rendering.Camera;
import JE.Scene.Scene;
import JE.Window.WindowPreferences;
import org.joml.Vector2f;
import org.joml.Vector3f;

public class LoadTest {
    public static void main(String[] args){
        Manager.start(new WindowPreferences(1280,720, "JE2", false, true));
        GameObject cam = new GameObject();
        Camera camera = new Camera();
        cam.addScript(camera);
        Scene loaded = new Scene();
        loaded.load("src/main/resources/scene.JEScene");

        loaded.add(PointLight.pointLightObject(new Vector2f(0,0), new Vector3f(0.8f,0.8f,0.8f), 3, 5));
        loaded.add(cam);
        loaded.setCamera(camera);
        Manager.setScene(loaded);
    }
}
