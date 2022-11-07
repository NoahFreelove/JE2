package JE;

import JE.Input.KeyPressedEvent;
import JE.Input.Keyboard;
import JE.Objects.Base.Identity;
import JE.Objects.CameraRig;
import JE.Objects.Components.Animator.Sprite.SpriteAnimationFrame;
import JE.Objects.Components.Animator.Sprite.SpriteAnimator;
import JE.Objects.Square;
import JE.Rendering.Texture;
import JE.Scene.Scene;
import org.joml.Vector2f;
import org.joml.Vector2i;

public class Main {
    public static void main(String[] args) {
        Manager.Run();

        Scene scene = new Scene();
        Square sprite = new Square();

        sprite.setIdentity(new Identity("Sprite", "sprite"));

        CameraRig cr = new CameraRig();
        cr.getTransform().zPos = 10;
        SpriteAnimator spriteAnimator = new SpriteAnimator(new SpriteAnimationFrame(new Texture("bin/cat.png",new Vector2i(158,209)), 2000f),
                new SpriteAnimationFrame(new Texture("bin/texture.png",new Vector2i(128,128)), 1000f));
        sprite.addComponent(spriteAnimator);
        spriteAnimator.Play();


        scene.activeCamera = cr.camera;

        KeyPressedEvent kp = (key, mods) -> {
            if(key == Keyboard.nameToCode("D")){
                sprite.getTransform().position.x+=1f;
            }
            else if(key == Keyboard.nameToCode("A")){
                sprite.getTransform().position.x-=1f;
            }
            else if(key == Keyboard.nameToCode("W")){
                sprite.getTransform().position.y+=1f;
            }
            else if(key == Keyboard.nameToCode("S")){
                sprite.getTransform().position.y-=1f;
            }
            else if(key == Keyboard.nameToCode("R")){
                spriteAnimator.Restart();
            }
        };
        Manager.AddKeyPressedCallback(kp);

        Square square2 = new Square(new Vector2f(3,1));
        scene.add(cr);
        scene.add(sprite);
        scene.add(square2);

        Manager.SetScene(scene);

    }
}