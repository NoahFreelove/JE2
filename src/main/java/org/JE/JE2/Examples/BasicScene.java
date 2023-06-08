package org.JE.JE2.Examples;

import org.JE.JE2.IO.UserInput.Keyboard.Keyboard;
import org.JE.JE2.IO.UserInput.Mouse.Mouse;
import org.JE.JE2.Objects.GameObject;
import org.JE.JE2.Objects.Lights.AmbientLight;
import org.JE.JE2.Objects.Lights.PointLight;
import org.JE.JE2.Objects.Scripts.Animator.Sprite.SpriteAnimationFrame;
import org.JE.JE2.Objects.Scripts.Animator.Sprite.SpriteAnimationTimeline;
import org.JE.JE2.Objects.Scripts.Animator.Sprite.SpriteAnimator;
import org.JE.JE2.Objects.Scripts.ScreenEffects.Physical.CameraShake;
import org.JE.JE2.Objects.Scripts.ScreenEffects.PostProcess.PostProcessRegistry;
import org.JE.JE2.Objects.Scripts.ScreenEffects.PostProcess.PostProcessingVolume;
import org.JE.JE2.Objects.Scripts.LambdaScript.ILambdaScript;
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
import org.JE.JE2.UI.UIElements.PreBuilt.FPSCounter;
import org.JE.JE2.UI.UIElements.Sliders.Slider;
import org.JE.JE2.UI.UIElements.Style.Color;
import org.JE.JE2.UI.UIObjects.UIWindow;
import org.JE.JE2.Window.Window;
import org.joml.Vector2f;
import org.lwjgl.system.windows.LARGE_INTEGER;

import java.math.BigInteger;

import static org.lwjgl.nuklear.Nuklear.*;
import static org.lwjgl.nuklear.Nuklear.NK_WINDOW_CLOSABLE;

public class BasicScene {
    static GameObject pl;
    static{
        pl = PointLight.pointLightObject(new Vector2f(1,-1), 0f,0f,0.2f,8, 1);
    }

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

        pl.addScript(new ILambdaScript() {
            @Override
            public void update(GameObject parent) {
                if(Keyboard.isKeyPressed(Keyboard.nameToCode("LEFT")))
                {
                    parent.getTransform().translateX(-5f * Window.deltaTime());
                }
                if(Keyboard.isKeyPressed(Keyboard.nameToCode("RIGHT")))
                {
                    parent.getTransform().translateX(5f * Window.deltaTime());
                }
                if(Keyboard.isKeyPressed(Keyboard.nameToCode("UP")))
                {
                    parent.getTransform().translateY(5f * Window.deltaTime());
                }
                if(Keyboard.isKeyPressed(Keyboard.nameToCode("DOWN")))
                {
                    parent.getTransform().translateY(-5f * Window.deltaTime());
                }
                //System.out.println(pl.getScript(PointLight.class).isObjectInsideRadius(player));
            }
        });

        player.getRenderer().getVAO().getShaderProgram().supportsTextures = true;

        player.setIdentity("Player", "player");

        SpriteAnimator sa = new SpriteAnimator();
        sa.addTimelines(new SpriteAnimationTimeline(
                new SpriteAnimationFrame(Texture.get("PlayerTexture"), Texture.get("PlayerNormal"), 500),
                new SpriteAnimationFrame(Texture.get("floor"), Texture.get("PlayerNormal"), 500)));
        player.addScript(sa);

        //sa.play();

        Camera playerCam = new Camera();
        //playerCam.backgroundColor = Color.createColorHex("#87ceeb");
        player.addScript(new PhysicsBody());
        player.addScript(new PlayerScript());


        CameraShake cs = new CameraShake();

        MovementController mc = new MovementController();
        mc.physicsBased = true;
        mc.canMoveDown = false;

        player.addScript(mc);
        player.addScript(playerCam);
        /*Manager.queueGLFunction(new Runnable() {
            @Override
            public void run() {
                player.getSpriteRenderer().getTexture().resource.setID(Window.colorTexture);
                player.getSpriteRenderer().getNormalTexture().resource.setID(Window.colorTexture);
            }
        },true);*/

        player.setPosition(2,0);
        scene.setCamera(playerCam);
        cs.cameraReference = playerCam;
        scene.add(AmbientLight.ambientLightObject(1,Color.WHITE));
        scene.add(pl);
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

        addPhysicsObject(scene);
        addFloors(scene);
        //createUI(scene);
        scene.add(player);

        return scene;
    }

    private static void createUI(Scene scene) {
        UIWindow uiWindow = new UIWindow("Adjust Lighting",
                NK_WINDOW_TITLE | NK_WINDOW_BORDER | NK_WINDOW_MINIMIZABLE | NK_WINDOW_SCALABLE | NK_WINDOW_MOVABLE | NK_WINDOW_CLOSABLE,
                new Vector2f(100, 100));

        Label Constant = new Label("Constant");
        Slider constant = new Slider(0f, 0f, 0.2f, 0.01f);
        constant.onChange = value -> {
            pl.getScripts(PointLight.class).forEach(pl -> pl.constant = value);
        };

        Label Linear = new Label("Linear");
        Slider linear = new Slider(0f, 0f, 0.2f, 0.01f);
        linear.onChange = value -> {
            pl.getScripts(PointLight.class).forEach(pl -> pl.linear = value);
        };

        Label Quadratic = new Label("Quadratic");
        Slider quadratic = new Slider(0f, 0f, 0.1f, 0.01f);
        quadratic.onChange = value -> {
            pl.getScripts(PointLight.class).forEach(pl -> pl.quadratic = value);
        };

        Label radiusLabel = new Label("Radius");
        Slider radius = new Slider(3f, 0f, 10f, 0.01f);
        radius.onChange = value -> {
            pl.getScripts(PointLight.class).forEach(pl -> pl.radius = value);
        };

        Label intensityLabel = new Label("Intensity");
        Slider intensity = new Slider(1f, 0f, 10f, 0.01f);
        intensity.onChange = value -> {
            pl.getScripts(PointLight.class).forEach(pl -> pl.intensity = value);
        };

        Label xPosLabel = new Label("X Position");
        Slider xPos = new Slider(1f, -10f, 10f, 0.01f);
        xPos.onChange = value -> {
            pl.setPosition(value, pl.getTransform().position().y);
        };

        Label yPosLabel = new Label("Y Position");
        Slider yPos = new Slider(-1f, -10f, 10f, 0.01f);
        yPos.onChange = value -> {
            pl.setPosition(pl.getTransform().position().x, value);
        };

        uiWindow.addElement(Constant, constant, Linear, linear, Quadratic, quadratic, radiusLabel, radius, intensityLabel, intensity);
        //uiWindow.addElement(xPosLabel, xPos, yPosLabel, yPos);

        scene.addUI(uiWindow);
        scene.addUI(FPSCounter.generateFPSBox(new Vector2f(1000 - 90, 1000 - 40)));
    }

    private static void addPhysicsObject(Scene scene) {
        GameObject go = new GameObject();
        go.addScript(new ShapeRenderer());
        ((ShapeRenderer)go.getRenderer()).setPoints(new Vector2f[]{
                new Vector2f(0,0),
                new Vector2f(1,0),
                new Vector2f(1,1),
                new Vector2f(0,1)
        });

        go.addScript(new PostProcessingVolume(new ShaderProgram(
                PostProcessRegistry.POST_PROCESS_VERTEX,
                PostProcessRegistry.BLUR_FRAG
        ), new Vector2f(3,3)));

        go.addScript(new PhysicsBody());
        go.getPhysicsBody().defaultRestitution = 0.8f;
        go.getPhysicsBody().defaultDensity = 0.1f;
        go.getPhysicsBody().defaultFriction = 0.05f;

        go.getRenderer().getVAO().setShaderProgram(ShaderProgram.spriteShader());
        go.getRenderer().getVAO().getShaderProgram().supportsTextures = false;
        go.getTransform().translateY(-1.5f);
        go.getRenderer().material.setBaseColor(Color.BLUE);
        scene.add(go);
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
