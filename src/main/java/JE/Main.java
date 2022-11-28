package JE;

import JE.Input.KeyPressedEvent;
import JE.Input.Keyboard;
import JE.Objects.Base.Sprite;
import JE.Objects.CameraRig;
import JE.Objects.Components.Pathfinding.Pathfinding;
import JE.Objects.Gizmos.MoveGizmo;
import JE.Objects.Lights.PointLight;
import JE.Objects.Player;
import JE.Rendering.Shaders.BuiltIn.LightObject.LightObjectShader;
import JE.Rendering.Shaders.Debugging.ShaderDebugger;
import JE.Rendering.Shaders.Debugging.ShaderTestSceneCustomData;
import JE.Scene.Scene;
import JE.Scene.SceneState;
import JE.UI.UIHandler;
import org.joml.Vector2f;
import org.joml.Vector2i;
import org.joml.Vector4f;

public class Main {
    static SceneState st;
    public static void main(String[] args) {
        Manager.run();
        Manager.setWindowSize(new Vector2i(1920, 1080));

        Scene scene = new Scene();

        Player player = new Player(new Vector2f(-1, 0));
        scene.addGizmo(new MoveGizmo(player));

        Sprite sprite2 = new Sprite(new Vector2f[]{
                new Vector2f(0,0),
                new Vector2f(1,0),
                new Vector2f(1,1),
                new Vector2f(0,1)
        },
                "bin/texture2.png",
                new Vector2i(64,64));
        sprite2.getTransform().position = new Vector2f(-2,2);

        Pathfinding pathComp = new Pathfinding();
        pathComp.path = new Vector2f[]{
                new Vector2f(-2,-2),
                new Vector2f(-2,2),
                new Vector2f(2,2),
                new Vector2f(2,-2),
        };
        pathComp.speed = 0.2f;
        sprite2.addComponent(pathComp);
        pathComp.startPathfinding();

        PointLight light = new PointLight(new Vector2f(0,0), new Vector4f(1f,1f,1f,1f),15, 5);

        scene.add(sprite2);
        scene.add(player);
        scene.add(new UIHandler());
        scene.addLight(light);
        scene.addGizmo(light.getRangeGizmo());
        scene.addGizmo(pathComp.getRangeGizmo());

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