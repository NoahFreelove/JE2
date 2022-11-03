package JE;

import JE.Objects.Identity;
import JE.Objects.Sprite;
import JE.Scene.Scene;
import JE.Window.WindowPreferences;
import org.joml.Vector2f;
import org.joml.Vector2i;

public class Main {
    public static void main(String[] args) {
        Manager.Start(new WindowPreferences(new Vector2i(800,600), "Window", true, true));

        Vector2f[] points = {
          new Vector2f(-1,-1), new Vector2f(1,-1), new Vector2f(0,1)
        };

        Scene scene = new Scene();
        Sprite sprite = new Sprite(points);
        sprite.setIdentity(new Identity("Sprite", "sprite"));
        scene.add(sprite);
        Manager.SetScene(scene);
    }
}