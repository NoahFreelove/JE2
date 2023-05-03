package org.JE.JE2.Examples;

import org.JE.JE2.IO.UserInput.Keyboard.Keyboard;
import org.JE.JE2.IO.UserInput.Mouse.Mouse;
import org.JE.JE2.Manager;
import org.JE.JE2.Objects.GameObject;
import org.JE.JE2.Objects.Lights.AmbientLight;
import org.JE.JE2.Objects.Lights.PointLight;
import org.JE.JE2.Objects.Scripts.CameraEffects.CameraShake;
import org.JE.JE2.Objects.Scripts.CameraEffects.PostProcessingVolume;
import org.JE.JE2.Objects.Scripts.LambdaScript.ILambdaScript;
import org.JE.JE2.Objects.Scripts.LambdaScript.LambdaScript;
import org.JE.JE2.Objects.Scripts.Physics.BoxTrigger;
import org.JE.JE2.Objects.Scripts.Physics.PhysicsBody;
import org.JE.JE2.Objects.Scripts.Physics.Raycast;
import org.JE.JE2.Objects.Scripts.Physics.TriggerEvent;
import org.JE.JE2.Rendering.Camera;
import org.JE.JE2.Rendering.Renderers.ShapeRenderer;
import org.JE.JE2.Rendering.Shaders.ShaderProgram;
import org.JE.JE2.Rendering.Texture;
import org.JE.JE2.Resources.Bundles.TextureBundle;
import org.JE.JE2.Resources.ResourceManager;
import org.JE.JE2.SampleScripts.FloorFactory;
import org.JE.JE2.SampleScripts.MovementController;
import org.JE.JE2.SampleScripts.PlayerScript;
import org.JE.JE2.Scene.Scene;
import org.JE.JE2.UI.UIElements.Label;
import org.JE.JE2.UI.UIElements.PreBuilt.SettingsGenerator;
import org.JE.JE2.UI.UIElements.Style.Color;
import org.JE.JE2.UI.UIObjects.UIWindow;
import org.JE.JE2.Utility.GarbageCollection;
import org.JE.JE2.Utility.Settings.Limits.*;
import org.JE.JE2.Utility.Settings.Setting;
import org.JE.JE2.Utility.Settings.SettingCategory;
import org.JE.JE2.Utility.Settings.SettingManager;
import org.JE.JE2.Window.WindowPreferences;
import org.joml.Vector2f;
import org.joml.Vector3f;

import static org.lwjgl.nuklear.Nuklear.*;
import static org.lwjgl.nuklear.Nuklear.NK_WINDOW_CLOSABLE;
import static org.lwjgl.opengl.GL11.GL_LINE_LOOP;

public class BasicScene {

    public static Scene mainScene(){
        Scene scene = new Scene();

        ResourceManager.warmupAssets(
                new String[]{
                        "PlayerTexture",
                        "PlayerNormal",
                        "floor"
                },
                new String[]{
                        "texture1.png",
                        "texture1_N.png",
                        "texture2.png",
                },
                new Class[]{
                        TextureBundle.class,
                        TextureBundle.class,
                        TextureBundle.class
                });

        GameObject player = GameObject.Sprite(ShaderProgram.spriteShader(),
                Texture.get("PlayerTexture"),
                Texture.get("PlayerNormal"));

        Camera playerCam = new Camera();
        player.addScript(new PhysicsBody());
        player.addScript(new PlayerScript());

        scene.add(PointLight.pointLightObject(new Vector2f(-5,-1), new Vector3f(1,1,1), 12, 2f).addScript(new LambdaScript(new ILambdaScript() {
            @Override
            public void update(GameObject parent) {
                //parent.getTransform().translateX(0.1f);
            }
        })));

        CameraShake cs = new CameraShake();

        MovementController mc = new MovementController();
        mc.physicsBased = true;
        mc.canMoveDown = false;
        player.addScript(mc);
        player.addScript(playerCam);
        player.setPosition(2,0);
        scene.setCamera(playerCam);
        cs.cameraReference = playerCam;
        //player.addScript(cs);

        scene.add(AmbientLight.ambientLightObject(1, Color.WHITE));

        Keyboard.addKeyReleasedEvent((key, mods) -> {
            if(key == Keyboard.nameToCode("E")){
                Vector2f pos = new Vector2f(player.getTransform().position());
                Vector2f mousePos = Mouse.getMouseWorldPosition2D();
                Vector2f dir = mousePos.sub(pos).normalize();
                Raycast r = player.getScript(PhysicsBody.class).raycast(player.getTransform().position().add(0.5f,0.1f),pos.add(dir.mul(2)),0);
                System.out.println((r.gameObjectHit() == null)? "Nothing" : r.gameObjectHit().identity());
            }
        });

        UIWindow uiWindow = new UIWindow("Cool window",
                NK_WINDOW_TITLE|NK_WINDOW_BORDER|NK_WINDOW_MINIMIZABLE|NK_WINDOW_SCALABLE|NK_WINDOW_MOVABLE|NK_WINDOW_CLOSABLE,
                new Vector2f(100,100));

        GameObject go = new GameObject();
        go.addScript(new ShapeRenderer());
        ((ShapeRenderer)go.getRenderer()).setPoints(new Vector2f[]{
                new Vector2f(0,0),
                new Vector2f(1,0),
                new Vector2f(1,1),
                new Vector2f(0,1)
        });

        go.addScript(new PostProcessingVolume());

        go.addScript(new PhysicsBody());
        go.getPhysicsBody().defaultRestitution = 0.8f;
        go.getPhysicsBody().defaultDensity = 0.1f;
        go.getPhysicsBody().defaultFriction = 0.05f;

        go.getRenderer().getVAO().setShaderProgram(ShaderProgram.spriteShader());
        go.getRenderer().getVAO().getShaderProgram().supportsTextures = false;
        go.getTransform().translateY(-1.5f);
        go.getRenderer().setDrawMode(GL_LINE_LOOP);
        go.getRenderer().baseColor = Color.BLUE;
        //scene.add(go);
        scene.add(player);

        scene.add(FloorFactory.createFloor(new Vector2f(-2,-4), new Vector2f(6,1)));
        GameObject rightWall = FloorFactory.createFloor(new Vector2f(4,-3), new Vector2f(1,4));
        scene.add(rightWall);
        scene.add(FloorFactory.createFloor(new Vector2f(-3,-3), new Vector2f(1,4)));
        scene.add(FloorFactory.createFloor(new Vector2f(-2,1), new Vector2f(6,1)));

        scene.add(BoxTrigger.triggerObject(new Vector2f(4, -3), new Vector2f(1, 5), new TriggerEvent() {
            @Override
            public void onTriggerEnter(GameObject other) {
                scene.remove(rightWall);
            }

            @Override
            public void onTriggerExit(GameObject other) {

            }

            @Override
            public void onTrigger(GameObject other) {

            }
        }));


        SettingManager settingManager = new SettingManager(new SettingCategory("Settings"));

        Setting<Integer> setting = new Setting<>("Int Count", 0);
        setting.setLimit(new IntLimit(0,Integer.MAX_VALUE));

        Setting<String> setting2 = new Setting<>("Some String", "Str");
        setting2.setLimit(new StringLimit(128));

        Setting<Boolean> setting3 = new Setting<>("Some Boolean", false);
        setting3.setLimit(new BooleanLimit());

        Setting<Float> setting4 = new Setting<>("Some Float", 0.2f);
        setting4.setLimit(new FloatLimit(0,100));

        Setting<Double> setting5 = new Setting<>("Some Double", 13.5);
        setting5.setLimit(new DoubleLimit(0,100));

        Setting<GameObject> setting6 = new Setting<>("Cool GameObject", new GameObject());
        settingManager.getCategory(0).addSettings(setting,setting2,setting3,setting4,setting5,setting6);

        uiWindow.addElement(SettingsGenerator.generateSettingsUI(settingManager));

        scene.addUI(uiWindow);

        Keyboard.addKeyReleasedEvent((key, mods) -> {
            if(key == Keyboard.nameToCode("F1")){
                setting.setValue(setting.getValue() + 1);
            }
            if(key == Keyboard.nameToCode("F2")){
                settingManager.saveToFile("settings.txt");
            }
            if(key == Keyboard.nameToCode("F3")){
                settingManager.tryLoadFromFile("settings.txt");
            }
            if(key == Keyboard.nameToCode("F4")){
                GarbageCollection.takeOutDaTrash();
            }
            if(key == Keyboard.nameToCode("F5")){
                Manager.setWindowPreferences(new WindowPreferences(2000,2000,"JE2"));
            }
        });

        return scene;
    }
}
