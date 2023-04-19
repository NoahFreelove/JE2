package org.JE.JE2.Tests;

import org.JE.JE2.IO.Logging.Logger;
import org.JE.JE2.IO.UserInput.Keyboard.KeyReleasedEvent;
import org.JE.JE2.IO.UserInput.Keyboard.Keyboard;
import org.JE.JE2.Manager;
import org.JE.JE2.Objects.GameObject;
import org.JE.JE2.Rendering.Camera;
import org.JE.JE2.Rendering.Renderers.SpriteRenderer;
import org.JE.JE2.Rendering.Shaders.ShaderProgram;
import org.JE.JE2.Rendering.Texture;
import org.JE.JE2.Resources.Bundles.ResourceBundle;
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
                new String[]{"texture1.png",
                        "texture1_N.png"
                },
                new String[]{
                        "player",
                        "player_normal"
                },
                new Class[]{
                        TextureBundle.class,
                        TextureBundle.class
                });

        Logger.logErrors = true;
        Logger.logPetty = true;
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
        GameObject object = GameObject.Sprite(ShaderProgram.spriteShaderSHARED,
                Texture.checkExistElseCreate("player",-1,"texture1.png"),
                Texture.checkExistElseCreate("player_normal",-1,"texture1_N.png"));

        object.setPosition(pos);
        return object;
    }
}
