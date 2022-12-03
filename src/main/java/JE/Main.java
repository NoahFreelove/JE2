package JE;

import JE.Audio.Filters.HighPassFilter;
import JE.Audio.Filters.LowPassFilter;
import JE.Input.Keyboard;
import JE.Objects.Audio.Sound2D;
import JE.Objects.Base.Sprites.Sprite;
import JE.Objects.Common.Player;
import JE.Objects.Lights.PointLight;
import JE.Scene.Scene;
import org.joml.Vector2f;
import org.joml.Vector2i;
import org.joml.Vector4f;

public class Main {
    public static Sound2D worldSound;

    public static void main(String[] args) {
        Manager.run();
        Manager.setWindowSize(new Vector2i(1920, 1080));

        Scene scene = new Scene();

        Player player = new Player(new Vector2f(-1, 0));
        //scene.addGizmo(new MoveGizmo(player));

        Sprite sprite2 = new Sprite(new Vector2f[]{
                new Vector2f(0,0),
                new Vector2f(1,0),
                new Vector2f(1,1),
                new Vector2f(0,1)
        },
                "bin/texture2.png",
                new Vector2i(64,64));
        sprite2.getTransform().position = new Vector2f(-2,2);

        /*Pathfinding pathComp = new Pathfinding();
        pathComp.path = new Vector2f[]{
                new Vector2f(-2,-2),
                new Vector2f(-2,2),
                new Vector2f(2,2),
                new Vector2f(2,-2),
        };
        pathComp.speed = 0.2f;
        sprite2.addComponent(pathComp);
        pathComp.startPathfinding();*/

        PointLight light = new PointLight(new Vector2f(0,0), new Vector4f(1f,1f,1f,1f),15, 5);

        scene.add(sprite2);
        scene.add(player);
        scene.addLight(light);
        scene.addGizmo(light.getRangeGizmo());

        /*Sound2D sound = new Sound2D("bin/sound.ogg",false);
        sound.soundPlayer.sound.setFilter(new SoundFilter());
        scene.add(sound);*/
        worldSound = new Sound2D("bin/music.ogg",false);
        worldSound.soundPlayer.sound.setFilter(new LowPassFilter(0));
        scene.add(worldSound);

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