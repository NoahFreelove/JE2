package JE;

import JE.Objects.Base.Identity;
import JE.Objects.Base.Sprite;
import JE.Objects.Square;
import JE.Scene.Scene;
import JE.Window.WindowPreferences;
import org.joml.Vector2f;
import org.joml.Vector2i;

public class Main {
    public static void main(String[] args) {
        Manager.Start(new WindowPreferences(new Vector2i(1920,1080), "Window", true, true));
        Scene scene = new Scene();
        Square sprite = new Square();
        sprite.setIdentity(new Identity("Sprite", "sprite"));
        sprite.getTransform().position.x += 0;
        sprite.getTransform().position.y += 0;

        scene.add(sprite);
        Manager.SetScene(scene);
    }
}