package org.JE.JE2.Examples;

import org.JE.JE2.IO.Filepath;
import org.JE.JE2.IO.UserInput.Keyboard.KeyReleasedEvent;
import org.JE.JE2.IO.UserInput.Keyboard.Keyboard;
import org.JE.JE2.IO.UserInput.Mouse.Mouse;
import org.JE.JE2.Objects.GameObject;
import org.JE.JE2.Objects.Lights.PointLight;
import org.JE.JE2.Objects.Scripts.Animator.Physical.CharacterAnimator;
import org.JE.JE2.Objects.Scripts.Animator.Physical.RelativeAnimator;
import org.JE.JE2.Objects.Scripts.Attributes.DontDestroyOnLoad;
import org.JE.JE2.Objects.Scripts.LambdaScript.ILambdaScript;
import org.JE.JE2.Objects.Scripts.Pathfinding.NavigableArea;
import org.JE.JE2.Objects.Scripts.Pathfinding.PathfindingActor;
import org.JE.JE2.Objects.Scripts.Pathfinding.SimplePathfindingAgent;
import org.JE.JE2.Objects.Scripts.Physics.Collision.BoxTrigger;
import org.JE.JE2.Objects.Scripts.Physics.Collision.TriggerEvent;
import org.JE.JE2.Objects.Scripts.Physics.PhysicsBody;
import org.JE.JE2.Objects.Scripts.Physics.Raycast;
import org.JE.JE2.Objects.Scripts.ScreenEffects.PostProcess.PostProcessRegistry;
import org.JE.JE2.Objects.Scripts.ScreenEffects.PostProcess.PostProcessingVolume;
import org.JE.JE2.Rendering.Camera;
import org.JE.JE2.Rendering.Debug.QuickDebugUI;
import org.JE.JE2.Rendering.Renderers.ShapeRenderer;
import org.JE.JE2.Rendering.Renderers.SpriteRenderer;
import org.JE.JE2.Rendering.Shaders.ShaderProgram;
import org.JE.JE2.Rendering.Texture;
import org.JE.JE2.Resources.Bundles.TextureBundle;
import org.JE.JE2.Resources.ResourceManager;
import org.JE.JE2.SampleScripts.FloorFactory;
import org.JE.JE2.SampleScripts.MovementController;
import org.JE.JE2.SampleScripts.PlayerScript;
import org.JE.JE2.Scene.Scene;
import org.JE.JE2.UI.UIElements.Group;
import org.JE.JE2.UI.UIElements.Label;
import org.JE.JE2.UI.UIElements.PreBuilt.FPSCounter;
import org.JE.JE2.UI.UIElements.Sliders.Slider;
import org.JE.JE2.UI.UIElements.Style.Color;
import org.JE.JE2.UI.UIElements.UIImage;
import org.JE.JE2.UI.UIObjects.UIWindow;
import org.JE.JE2.Utility.ForceNonNull;
import org.JE.JE2.Utility.JE2Math;
import org.JE.JE2.Utility.Time;
import org.joml.Vector2f;

import static org.lwjgl.nuklear.Nuklear.*;

public class BasicScene {
    static GameObject pl;
    static{
        pl = PointLight.pointLightObject(new Vector2f(1,-1), 0f,0f,0.2f,8, 1);
    }

    public static Scene mainScene(){
        Scene scene = new Scene();
        scene.name = "Basic Scene";
        ResourceManager.warmupAssets(
                new String[]{
                        "PlayerTexture",
                        "PlayerNormal",
                        "floor",
                        "fire",
                },
                new Filepath[]{
                        new Filepath("texture1.png", true),
                        new Filepath("texture1_N.png", true),
                        new Filepath("texture2.png", true),
                        new Filepath("fire.png", true),
                },
                TextureBundle.class);

        /*Texture newText = TextureUtils.saturateTexture(Texture.checkExistElseCreate("fire",new Filepath("fire.png",true)), "playerTint", 1f);
        player.getSpriteRenderer().setTexture(newText);*/

        GameObject player = GameObject.Sprite(ShaderProgram.spriteShader(),
                Texture.get("PlayerTexture"),
                Texture.get("PlayerNormal"));

       /* RelativeAnimator ra = new RelativeAnimator(new Vector2f[]{
                new Vector2f(0,-1),
                new Vector2f(1,-1),
                new Vector2f(1,0),
                new Vector2f(0,0)
        },1);
        player.addScript(ra);*/
        Camera playerCam = new Camera();

        //addParticles(scene);

        addAnim(new ForceNonNull<>(SpriteRenderer.class).forceNonNull(player.getSpriteRenderer()));

        /*asp.setExternalScriptBehaviourPost(new ILambdaScript() {
            @Override
            public void update(GameObject parent) {
                parent.getTransform().translateX(0.5f * Time.deltaTime());

                asp.setRelativeAudioBasedOnCamera(playerCam);
            }
        });

        GameObject sound = RenderColoredArea.getArea(0,0,1,1, Color.RED);

        sound.addScript(asp);
        sound.setPosition(-1,-1);

        scene.add(sound);*/
        //asp.play(true);


        player.addScript(new DontDestroyOnLoad());
        //player.addScript(tr);
        Keyboard.addKeyReleasedEvent(new KeyReleasedEvent() {
            @Override
            public void invoke(int key, int mods) {
                if(Keyboard.nameToCode("1") == key){

                }
            }
        });

        pl.addScript(new ILambdaScript() {
            @Override
            public void update(GameObject parent) {
                parent.setPosition(player.getTransform().position());
                if(Keyboard.isKeyPressed(Keyboard.nameToCode("LEFT")))
                {
                    parent.getTransform().translateX(-5f * Time.deltaTime());
                }
                if(Keyboard.isKeyPressed(Keyboard.nameToCode("RIGHT")))
                {
                    parent.getTransform().translateX(5f * Time.deltaTime());
                }
                if(Keyboard.isKeyPressed(Keyboard.nameToCode("UP")))
                {
                    parent.getTransform().translateY(5f * Time.deltaTime());
                }
                if(Keyboard.isKeyPressed(Keyboard.nameToCode("DOWN")))
                {
                    parent.getTransform().translateY(-5f * Time.deltaTime());
                }
                //System.out.println(pl.getScript(PointLight.class).isObjectInsideRadius(player));
            }
        });

        player.getRenderer().getShaderProgram().supportsTextures = true;

        player.setIdentity("Player", "player");

        /*SpriteAnimator sa = new SpriteAnimator();
        sa.addTimelines(new SpriteAnimationTimeline(
                new SpriteAnimationFrame(Texture.get("PlayerTexture"), Texture.get("PlayerNormal"), 500),
                new SpriteAnimationFrame(Texture.get("floor"), Texture.get("PlayerNormal"), 500)));
        player.addScript(sa);*/

        //sa.play();

        //playerCam.backgroundColor = Color.createColorHex("#87ceeb");
        PhysicsBody pb = new PhysicsBody();
        player.addScript(pb);
        player.addScript(new PlayerScript());
        //player.getTransform().translateX(1);


        MovementController mc = new MovementController();
        mc.physicsBased = true;
        mc.absoluteYPositioning = true;
        mc.canMoveDown = true;
        mc.enableJump = false;
        pb.setGravity(0);

        player.addScript(mc);
        player.addScript(playerCam);

        player.setPosition(2,0);
        scene.setCamera(playerCam);
        scene.add(pl);

        /*HashMap<String, HashMap<String,String>> save = player.save();
        GameObject newObj = new GameObject();
        newObj.load(save);

        scene.add(newObj);*/

        Keyboard.addKeyReleasedEvent((key, mods) -> {
            if(key == Keyboard.nameToCode("E")){
                Vector2f pos = new Vector2f(player.getTransform().position());
                Vector2f mousePos = Mouse.getMouseWorldPosition2D();
                Vector2f dir = mousePos.sub(pos).normalize();
                Raycast r = (new ForceNonNull<>(PhysicsBody.class).forceNonNull(player.getScript(PhysicsBody.class)))
                        .raycast(player.getTransform().position().add(0.5f,0.1f),pos.add(dir.mul(2)),0);
                System.out.println((r.gameObjectHit() == null)? "Nothing" : r.gameObjectHit().name);
            }
        });

        PostProcessingVolume ppv = new PostProcessingVolume(new Vector2f(1,1));
        ppv.setScreenShader(new ShaderProgram(PostProcessRegistry.invertShaderModule));
        //new ShaderProgram(PostProcessRegistry.invertShaderModule)
        GameObject ppo = new GameObject();
        ppo.setPosition(2,-1);
        ppo.addScript(ppv);
        scene.add(ppo);

        //addPhysicsObject(scene, player);
        addFloors(scene);
        createUI(scene);
        scene.add(player);

        scene.saveSceneToZip(new Filepath("scene.zip"));

        return scene;
    }

    private static void addAnim(SpriteRenderer player){
        CharacterAnimator ca = new CharacterAnimator(player.getTextureSegments());
        ca.setLoop(true);
        Vector2f startPos = player.getAttachedObject().getTransform().position();
        ca.createAnim(player.getTextureSegment(),startPos, new Vector2f(1,1),new Vector2f(startPos));
        player.getAttachedObject().addScript(ca);
        Keyboard.addKeyReleasedEvent((key, mods) -> {
            if(key == Keyboard.nameToCode("L")){
                ca.play();
            }
        });
    }

    private static void createUI(Scene scene) {
        UIWindow uiWindow = new UIWindow("Adjust Lighting",
                NK_WINDOW_TITLE | NK_WINDOW_BORDER | NK_WINDOW_MINIMIZABLE | NK_WINDOW_SCALABLE | NK_WINDOW_MOVABLE | NK_WINDOW_CLOSABLE,
                new Vector2f(100, 100));

        uiWindow.addElement(new UIImage(Texture.createTexture("icon",new Filepath("texture1.png",true),false)));

        uiWindow.setBackgroundColor(Color.BROWN);

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

        Label timeScaleLabel = new Label("Time Scale");
        Slider timeScale = new Slider(1f, 0f, 2f, 0.01f);
        timeScale.onChange = Time::setTimeScale;

        scene.addUI(QuickDebugUI.quickDebugWindow(new Group(timeScaleLabel,timeScale)));


        scene.addUI(uiWindow);
        scene.addUI(FPSCounter.generateFPSBox(new Vector2f(1000 - 90, 1000 - 40)));
    }

    private static void addPhysicsObject(Scene scene, GameObject player) {
        GameObject go = new GameObject();
        go.addScript(new ShapeRenderer());
        ((ShapeRenderer)go.getRenderer()).setPoints(new Vector2f[]{
                new Vector2f(0,0),
                new Vector2f(1,0),
                new Vector2f(1,1),
                new Vector2f(0,1)
        });

        go.addScript(new PhysicsBody());
        go.getPhysicsBody().setGravity(0);
        /*go.getPhysicsBody().defaultRestitution = 0.8f;
        go.getPhysicsBody().defaultDensity = 0.1f;
        go.getPhysicsBody().defaultFriction = 0.05f;*/

        go.getRenderer().setShaderProgram(ShaderProgram.spriteShader());
        go.getRenderer().getShaderProgram().supportsTextures = false;
        go.getTransform().translateY(-1.5f);
        go.getRenderer().material.setBaseColor(Color.BLUE);
        scene.add(go);

        NavigableArea na = new NavigableArea(new Vector2f(-2,-3), new Vector2f(4,1));
        SimplePathfindingAgent spa = new SimplePathfindingAgent(na);
        PathfindingActor pa = new PathfindingActor(spa, JE2Math.floatExp(5,-2));
        pa.setTargetObject(player);
        pa.setSnapInRange(false);
        pa.setSuccessRange(1.5f);
        go.addScript(pa);
        //scene.add(na.getDebugArea());
    }

    private static void addFloors(Scene scene) {
        scene.add(FloorFactory.createFloor(new Vector2f(-2,-4), new Vector2f(6,1)));

        scene.add(FloorFactory.createFloor(new Vector2f(-3,-4), new Vector2f(1,6)));
        scene.add(FloorFactory.createFloor(new Vector2f(-2,1), new Vector2f(6,1)));
        scene.add(FloorFactory.createFloor(new Vector2f(4,-4), new Vector2f(3,1)));
        scene.add(FloorFactory.createFloor(new Vector2f(7,-3), new Vector2f(1,1)));
        scene.add(FloorFactory.createFloor(new Vector2f(8,-2), new Vector2f(1,1)));
        scene.add(FloorFactory.createFloor(new Vector2f(9,-1), new Vector2f(1,1)));
        scene.add(FloorFactory.createFloor(new Vector2f(10,0), new Vector2f(1,1)));
        scene.add(FloorFactory.createFloor(new Vector2f(11,0), new Vector2f(1,6)));
        scene.add(FloorFactory.createFloor(new Vector2f(1,6), new Vector2f(11,1)));

        scene.add(FloorFactory.createFloor(new Vector2f(4,1), new Vector2f(3,1)));

        GameObject rightWall = FloorFactory.createFloor(new Vector2f(4,-3), new Vector2f(1,4));
        scene.add(rightWall);
        scene.add(BoxTrigger.triggerObject(new Vector2f(4, -3), new Vector2f(1, 5), new TriggerEvent() {
            @Override
            public void onTriggerEnter(GameObject other) {
                scene.remove(rightWall);
            }
        }));
    }
}
