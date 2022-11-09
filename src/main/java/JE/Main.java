package JE;

import JE.Input.KeyPressedEvent;
import JE.Input.Keyboard;
import JE.Objects.Base.Identity;
import JE.Objects.Base.Skybox;
import JE.Objects.CameraRig;
import JE.Objects.Components.Animator.Sprite.SpriteAnimationFrame;
import JE.Objects.Components.Animator.Sprite.SpriteAnimator;
import JE.Objects.Square;
import JE.Rendering.Texture;
import JE.Scene.Scene;
import org.joml.Vector2f;
import org.joml.Vector2i;
import org.joml.Vector4f;

public class Main {
    public static void main(String[] args) {
        Manager.Run();

        Scene scene = new Scene();
        Square sprite = new Square();
        sprite.setIdentity(new Identity("Sprite", "sprite"));

        CameraRig cr = new CameraRig();
        cr.getTransform().zPos = 10;
        SpriteAnimator spriteAnimator = new SpriteAnimator(new SpriteAnimationFrame(new Texture("bin/texture1.png",new Vector2i(64,64)), 200f),
                new SpriteAnimationFrame(new Texture("bin/texture2.png",new Vector2i(64,64)), 200f));
        sprite.addComponent(spriteAnimator);
        //spriteAnimator.Play();


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
            else if(key == Keyboard.nameToCode("R")){
                spriteAnimator.Restart();
            }
        };
        Manager.AddKeyPressedCallback(kp);

        scene.add(cr);
        scene.add(new Square(new Vector2f(1,0)));
        scene.add(sprite);
        scene.add(new Skybox(new Vector4f(0.27f,0.5f,0.64f,1)));

        Manager.SetScene(scene);

    }
}