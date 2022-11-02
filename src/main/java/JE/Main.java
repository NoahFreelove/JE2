package JE;

import JE.Objects.GameObject;
import JE.Scene.Scene;
import JE.Scene.SceneState;

public class Main {
    public static void main(String[] args) {
        Manager.Start();
        Scene scene = new Scene();
        Manager.SetScene(scene);

    }
}