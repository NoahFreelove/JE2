package JE;

import JE.Objects.Base.Skybox;
import JE.Objects.Base.Sprite;
import JE.Objects.Components.Pathfinding.Pathfinding;
import JE.Objects.Lights.PointLight;
import JE.Objects.Player;
import JE.Rendering.Shaders.BuiltIn.LightObject.LightObjectShader;
import JE.Rendering.Shaders.BuiltIn.LightSprite.LightSpriteShader;
import JE.Rendering.Shaders.Debugging.ShaderDebugger;
import JE.Rendering.Shaders.Debugging.ShaderTestSceneCustomData;
import JE.Rendering.Shaders.ShaderProgram;
import JE.Scene.Scene;
import org.joml.Vector2f;
import org.joml.Vector2i;
import org.joml.Vector4f;

public class Main {
    public static void main(String[] args) {
        Manager.Run();
        //Manager.setWindowSize(new Vector2i(1920, 1080));

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

        Pathfinding pathComp = new Pathfinding();
        pathComp.generatePath(new Vector2f(3,0), new Vector2f(2,-2));
        pathComp.speed = 0.2f;
        sprite2.addComponent(pathComp);
        pathComp.startPathfinding();

        PointLight light = new PointLight(new Vector2f(0,0), new Vector4f(1f,1f,1f,1f),5);
        
        scene.add(sprite2);
        scene.add(player);
        scene.addLight(light);
        scene.addGizmo(light.getGizmo());
        scene.addGizmo(pathComp.getGizmos());

        Manager.SetScene(scene);
        Manager.getActiveScene().activeCamera = player.camera;



        /*ShaderTestSceneCustomData data = new ShaderTestSceneCustomData();
        data.baseColor = new Vector4f(0,1,0,1);
        data.sceneLights.add(new PointLight(new Vector2f(1,0), new Vector4f(1,1,1,1), 0.5f));
        ShaderDebugger.TestShader(new LightObjectShader(),data);*/


    }
}