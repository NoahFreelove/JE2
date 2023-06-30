package org.JE.JE2.Examples;

import org.JE.JE2.IO.Filepath;
import org.JE.JE2.IO.Logging.Logger;
import org.JE.JE2.IO.UserInput.Keyboard.Keyboard;
import org.JE.JE2.Manager;
import org.JE.JE2.Objects.GameObject;
import org.JE.JE2.Rendering.Camera;
import org.JE.JE2.Rendering.Shaders.ShaderRegistry;
import org.JE.JE2.Rendering.Texture;
import org.JE.JE2.Resources.Bundles.TextureBundle;
import org.JE.JE2.Resources.ResourceLoadingPolicy;
import org.JE.JE2.Resources.ResourceManager;
import org.JE.JE2.Scene.Scene;
import org.JE.JE2.Window.WindowPreferences;
import org.joml.Vector2f;

public class ObjectStressTest {
    public static void main(String[] args) {
        int maxObjects = 100;
        ResourceManager.policy = ResourceLoadingPolicy.CHECK_BY_NAME;
        Manager.start(new WindowPreferences(800,800, "JE2", false, true));

        ResourceManager.warmupAssets(
                new String[]{
                        "player",
                        "player_normal"
                },
                new Filepath[]{new Filepath("texture1.png",true),
                        new Filepath("texture1_N.png",true)
                },
                new Class[]{
                        TextureBundle.class,
                        TextureBundle.class
                });

        Logger.logErrors = true;
        Logger.logThreshold = 0;
        GameObject cameraRig = new GameObject();
        Camera cam = new Camera();
        cameraRig.addScript(cam);
        Scene activeScene = new Scene();
        activeScene.setCamera(cam);
        activeScene.add(cameraRig);

        Keyboard.addKeyReleasedEvent((key, mods) -> {
            for (int i = 0; i < maxObjects; i++) {
                //Vector2f offset = new Vector2f(i * 0.033f, i * 0.33f);
                Vector2f offset = new Vector2f(-3 + i * 0.1f,-3 + i * 0.1f);
                activeScene.add(objectFactory(offset));
            }
        });

        Manager.setScene(activeScene);
    }

    private static GameObject objectFactory(Vector2f pos){
        GameObject object = GameObject.Sprite(ShaderRegistry.Sprite,
                Texture.get("player"),
                Texture.get("player_normal"));

        object.setPosition(pos);
        return object;
    }
}
