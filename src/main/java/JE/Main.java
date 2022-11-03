package JE;

import JE.Objects.Sprite;
import JE.Scene.Scene;
import JE.Window.WindowPreferences;
import org.joml.Vector2i;

public class Main {
    public static void main(String[] args) {
        Manager.Start(new WindowPreferences(new Vector2i(800,600), "Window", false));

        Scene scene = new Scene();
        Sprite sprite = new Sprite();
        Manager.SetScene(scene);

        //Manager.Run();

    }
}