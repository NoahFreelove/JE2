package JE;


import JE.Input.Keyboard;
import JE.Objects.Base.Sprites.Sprite;
import JE.Objects.Common.Player;
import JE.Objects.Lights.PointLight;
import JE.Rendering.Shaders.BuiltIn.LightObject.LightObjectShader;
import JE.Scene.Scene;
import org.joml.Vector2f;
import org.joml.Vector4f;

public class Main {

    public static void main(String[] args) {
        Manager.run();
        //Manager.setWindowSize(new Vector2i(1920, 1080));

        Scene scene = new Scene();
        Manager.setScene(scene);

        Player player = new Player();
        player.setPosition(-1,0);
        scene.add(player);
        scene.activeCamera = player.camera;

        PointLight light = new PointLight();
        light.getTransform().position = new Vector2f(0,0);
        light.color = new Vector4f(1,1,1,1);
        light.intensity = 10;
        light.radius = 5;

        scene.addLight(light);
        //scene.addGizmo(light.getRangeGizmo());

        Sprite sprite2 = new Sprite();
        sprite2.setPosition(-2,2);
        sprite2.renderer.baseColor = new Vector4f(1,1,0,1);
        sprite2.setShader(new LightObjectShader());

        //scene.add(sprite2);
        //worldSound.soundPlayer.play();

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