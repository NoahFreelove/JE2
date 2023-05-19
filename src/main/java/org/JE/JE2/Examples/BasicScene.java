package org.JE.JE2.Examples;

import org.JE.JE2.IO.UserInput.Keyboard.Keyboard;
import org.JE.JE2.IO.UserInput.Mouse.Mouse;
import org.JE.JE2.Manager;
import org.JE.JE2.Objects.GameObject;
import org.JE.JE2.Objects.Lights.AmbientLight;
import org.JE.JE2.Objects.Lights.PointLight;
import org.JE.JE2.Objects.Scripts.Animator.Sprite.SpriteAnimationFrame;
import org.JE.JE2.Objects.Scripts.Animator.Sprite.SpriteAnimationTimeline;
import org.JE.JE2.Objects.Scripts.Animator.Sprite.SpriteAnimator;
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
import org.JE.JE2.Resources.DataLoader;
import org.JE.JE2.Resources.ResourceManager;
import org.JE.JE2.SampleScripts.FloorFactory;
import org.JE.JE2.SampleScripts.MovementController;
import org.JE.JE2.SampleScripts.PlayerScript;
import org.JE.JE2.Scene.Scene;
import org.JE.JE2.UI.Font;
import org.JE.JE2.UI.UIElements.Label;
import org.JE.JE2.UI.UIElements.PreBuilt.FPSCounter;
import org.JE.JE2.UI.UIElements.PreBuilt.SettingsGenerator;
import org.JE.JE2.UI.UIElements.Spacer;
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

        GameObject player = GameObject.Sprite(ShaderProgram.lightSpriteShader(),
                Texture.get("PlayerTexture"),
                Texture.get("PlayerNormal"));

        player.getRenderer().getVAO().getShaderProgram().supportsTextures = true;

        player.setIdentity("Player", "player");

        SpriteAnimator sa = new SpriteAnimator();
        sa.addTimelines(new SpriteAnimationTimeline(
                new SpriteAnimationFrame(Texture.get("PlayerTexture"), Texture.get("PlayerNormal"), 500),
                new SpriteAnimationFrame(Texture.get("floor"), Texture.get("PlayerNormal"), 500)));
        player.addScript(sa);

        //sa.play();

        Camera playerCam = new Camera();
        player.addScript(new PhysicsBody());
        player.addScript(new PlayerScript());

        scene.add(PointLight.pointLightObject(new Vector2f(1,-1), 1f,0.09f,0.032f,25, 5).addScript(new LambdaScript(new ILambdaScript() {
            @Override
            public void update(GameObject parent) {
                //System.out.println("Released for: " + Mouse.buttonReleasedForSeconds(0));
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

        //scene.add(AmbientLight.ambientLightObject(1, Color.WHITE));

        Keyboard.addKeyReleasedEvent((key, mods) -> {
            if(key == Keyboard.nameToCode("E")){
                Vector2f pos = new Vector2f(player.getTransform().position());
                Vector2f mousePos = Mouse.getMouseWorldPosition2D();
                Vector2f dir = mousePos.sub(pos).normalize();
                Raycast r = player.getScript(PhysicsBody.class).raycast(player.getTransform().position().add(0.5f,0.1f),pos.add(dir.mul(2)),0);
                System.out.println((r.gameObjectHit() == null)? "Nothing" : r.gameObjectHit().identity());
            }
        });

        addPhysicsObject();
        addFloors(scene);
        //createUI(scene);
        scene.add(player);

        return scene;
    }

    private static void createUI(Scene scene) {
        UIWindow uiWindow = new UIWindow("Cool window",
                NK_WINDOW_TITLE|NK_WINDOW_BORDER|NK_WINDOW_MINIMIZABLE|NK_WINDOW_SCALABLE|NK_WINDOW_MOVABLE|NK_WINDOW_CLOSABLE,
                new Vector2f(100,100));
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

        Label small = new Label("arial");
        small.getStyle().font = new Font(DataLoader.getBytes("arial.ttf"), false);
        small.getStyle().font.fontHeight = 25;

        Label large = new Label("comic sans");
        large.getStyle().font = new Font(DataLoader.getBytes("comic.ttf"), false);
        large.getStyle().font.fontHeight = 45;

        uiWindow.addElement(large);
        uiWindow.addElement(new Spacer());
        uiWindow.addElement(small);

        uiWindow.addElement(SettingsGenerator.generateSettingsUI(settingManager));

        scene.addUI(uiWindow);
        scene.addUI(FPSCounter.generateFPSBox(new Vector2f(800-90,800-40)));
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
    }

    private static void addPhysicsObject() {
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
        go.getRenderer().material.setBaseColor(Color.BLUE);
        //scene.add(go);
    }

    private static void addFloors(Scene scene) {
        scene.add(FloorFactory.createFloor(new Vector2f(-2,-4), new Vector2f(6,1)));
        GameObject rightWall = FloorFactory.createFloor(new Vector2f(4,-3), new Vector2f(1,4));
        scene.add(rightWall);
        scene.add(FloorFactory.createFloor(new Vector2f(-3,-4), new Vector2f(1,6)));
        scene.add(FloorFactory.createFloor(new Vector2f(-2,1), new Vector2f(6,1)));
        scene.add(FloorFactory.createFloor(new Vector2f(4,-4), new Vector2f(3,1)));
        scene.add(FloorFactory.createFloor(new Vector2f(7,-3), new Vector2f(1,1)));
        scene.add(FloorFactory.createFloor(new Vector2f(8,-2), new Vector2f(1,1)));
        scene.add(FloorFactory.createFloor(new Vector2f(9,-1), new Vector2f(1,1)));
        scene.add(FloorFactory.createFloor(new Vector2f(10,0), new Vector2f(1,1)));
        scene.add(FloorFactory.createFloor(new Vector2f(11,0), new Vector2f(1,6)));
        scene.add(FloorFactory.createFloor(new Vector2f(11,6), new Vector2f(-10,1)));

        scene.add(FloorFactory.createFloor(new Vector2f(4,1), new Vector2f(3,1)));

        scene.add(BoxTrigger.triggerObject(new Vector2f(4, -3), new Vector2f(1, 5), new TriggerEvent() {
            @Override
            public void onTriggerEnter(GameObject other) {
                scene.remove(rightWall);
            }
        }));
    }
}
