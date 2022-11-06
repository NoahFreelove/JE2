package JE;

import JE.Input.KeyPressedEvent;
import JE.Input.Keyboard;
import JE.Objects.Base.Identity;
import JE.Objects.CameraRig;
import JE.Objects.Square;
import JE.Scene.Scene;

public class Main {
    public static void main(String[] args) {
        Manager.Run();

        Scene scene = new Scene();
        Square sprite = new Square();
        sprite.setIdentity(new Identity("Sprite", "sprite"));

        CameraRig cr = new CameraRig();
        cr.getTransform().zPos = 10;

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
        };
        Manager.AddKeyPressedCallback(kp);

        scene.add(cr);
        scene.add(sprite);
        Manager.SetScene(scene);

    }
}