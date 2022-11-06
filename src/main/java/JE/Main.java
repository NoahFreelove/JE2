package JE;

import JE.Objects.Base.Identity;
import JE.Objects.CameraRig;
import JE.Objects.Components.Component;
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

        scene.add(cr);
        scene.add(sprite);
        Manager.SetScene(scene);

    }
}