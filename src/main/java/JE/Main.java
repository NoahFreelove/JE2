package JE;

import JE.Audio.Filters.LowPassFilter;
import JE.Input.Keyboard;
import JE.Objects.Audio.WorldSound;
import JE.Objects.Base.Sprites.Sprite;
import JE.Objects.Common.Player;
import JE.Objects.Lights.PointLight;
import JE.Scene.Scene;
import org.joml.Vector2f;
import org.joml.Vector2i;
import org.joml.Vector4f;

public class Main {
    public static WorldSound worldSound;

    public static void main(String[] args) {
        Manager.run();
        Manager.setWindowSize(new Vector2i(1920, 1080));

        Scene scene = new Scene();

        Player player = new Player(new Vector2f(-1, 0));

        Sprite sprite2 = new Sprite(new Vector2f[]{
                new Vector2f(0,0),
                new Vector2f(1,0),
                new Vector2f(1,1),
                new Vector2f(0,1)
        },
                "bin/texture2.png",
                new Vector2i(64,64));
        sprite2.getTransform().position = new Vector2f(-2,2);

        PointLight light = new PointLight(new Vector2f(0,0), new Vector4f(1f,1f,1f,1f),15, 5);

        scene.add(sprite2);
        scene.add(player);
        scene.addLight(light);
        scene.addGizmo(light.getRangeGizmo());

        worldSound = new WorldSound("bin/music.ogg",false);
        scene.add(worldSound);
        worldSound.soundPlayer.play();


        scene.activeCamera = player.camera;
        Manager.setScene(scene);

        Manager.AddKeyPressedCallback((key, mods) -> {
            if(key == Keyboard.nameToCode("F")){
                System.out.println(Manager.getFPS());
            }
        });

        /*ShaderTestSceneCustomData data = new ShaderTestSceneCustomData();
        data.baseColor = new Vector4f(0,1,0,1);
        data.sceneLights.add(new PointLight(new Vector2f(0,0), new Vector4f(1,1,1,1), 5f,5));
        ShaderDebugger.TestShader(new LightObjectShader(),data);*/
    }
}