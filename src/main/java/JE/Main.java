package JE;

import JE.Input.KeyPressedEvent;
import JE.Input.Keyboard;
import JE.Objects.Base.Identity;
import JE.Objects.Base.Skybox;
import JE.Objects.Base.Sprite;
import JE.Objects.CameraRig;
import JE.Objects.Components.Animator.Sprite.SpriteAnimationFrame;
import JE.Objects.Components.Animator.Sprite.SpriteAnimator;
import JE.Objects.Lights.SimpleLight;
import JE.Objects.Square;
import JE.Rendering.Texture;
import JE.Scene.Scene;
import org.joml.Vector2f;
import org.joml.Vector2i;
import org.joml.Vector4f;

public class Main {
    public static void main(String[] args) {
        Manager.Run();
        Manager.setWindowSize(new Vector2i(1920, 1080));

        Scene scene = new Scene();
        Sprite sprite = new Sprite(new Vector2f[]{
                new Vector2f(0,0),
                new Vector2f(1,0),
                new Vector2f(1,1),
                new Vector2f(0,1)
        },

                "bin/texture1.png",
                new Vector2i(64,64));

        Sprite sprite2 = new Sprite(new Vector2f[]{
                new Vector2f(0,0),
                new Vector2f(1,0),
                new Vector2f(1,1),
                new Vector2f(0,1)
        },

                "bin/texture2.png",
                new Vector2i(64,64));

        SimpleLight simpleLight = new SimpleLight(new Vector2f(0,0), new Vector2f(1,1), new Vector4f(1,0,0,1));

        sprite.getTransform().position = new Vector2f(-1,0);
        sprite.setIdentity(new Identity("Sprite", "sprite"));

        CameraRig cr = new CameraRig();
        cr.getTransform().zPos = 10;

        scene.activeCamera = cr.camera;

        KeyPressedEvent kp = (key, mods) -> {
            if(key == Keyboard.nameToCode("D")){
                sprite.getTransform().position.x+=1f;
                cr.getTransform().position.x+=1f;
            }
            else if(key == Keyboard.nameToCode("A")){
                sprite.getTransform().position.x-=1f;
                cr.getTransform().position.x-=1f;
            }
            else if(key == Keyboard.nameToCode("W")){
                sprite.getTransform().position.y+=1f;
                cr.getTransform().position.y+=1f;

            }
            else if(key == Keyboard.nameToCode("S")){
                sprite.getTransform().position.y-=1f;
                cr.getTransform().position.y-=1f;
            }
        };
        Manager.AddKeyPressedCallback(kp);

        scene.add(cr);
        scene.add(sprite2);
        scene.add(sprite);
        scene.add(simpleLight);
        scene.add(new Skybox(new Vector4f(0.27f,0.5f,0.64f,1)));

        Manager.SetScene(scene);

    }
}