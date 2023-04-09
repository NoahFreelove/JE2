package org.JE.JE2.Tests;

import org.JE.JE2.Manager;
import org.JE.JE2.Objects.GameObject;
import org.JE.JE2.Objects.Lights.PointLight;
import org.JE.JE2.Rendering.Camera;
import org.JE.JE2.Scene.Scene;
import org.JE.JE2.Window.WindowPreferences;
import org.joml.Vector2f;
import org.joml.Vector3f;

public class LoadTest {
    public static void main(String[] args){
        Manager.start(new WindowPreferences(1280,720, "JE2", false, true));
        GameObject cam = new GameObject();
        Camera camera = new Camera();
        cam.addScript(camera);
        Scene loaded = new Scene();

        loaded.add(PointLight.pointLightObject(new Vector2f(0,0), new Vector3f(0.8f,0.8f,0.8f), 3, 5));
        loaded.add(cam);
        loaded.setCamera(camera);
        Manager.setScene(loaded);
    }
}
