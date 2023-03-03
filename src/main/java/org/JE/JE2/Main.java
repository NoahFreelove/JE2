package org.JE.JE2;

import org.JE.JE2.IO.Logging.Logger;
import org.JE.JE2.IO.UserInput.Keyboard.Keyboard;
import org.JE.JE2.IO.UserInput.Mouse.Mouse;
import org.JE.JE2.Objects.GameObject;
import org.JE.JE2.Objects.Lights.PointLight;
import org.JE.JE2.Objects.Scripts.Physics.PhysicsBody;
import org.JE.JE2.Objects.Scripts.Physics.Raycast;
import org.JE.JE2.Rendering.Camera;
import org.JE.JE2.Rendering.Renderers.ShapeRenderer;
import org.JE.JE2.Rendering.Shaders.ShaderProgram;
import org.JE.JE2.Rendering.Texture;
import org.JE.JE2.Resources.ResourceLoader;
import org.JE.JE2.SampleScripts.FloorFactory;
import org.JE.JE2.SampleScripts.MovementController;
import org.JE.JE2.SampleScripts.PlayerScript;
import org.JE.JE2.Scene.Scene;
import org.JE.JE2.UI.UIElements.Buttons.ImageButton;
import org.JE.JE2.UI.UIElements.Buttons.StyledButton;
import org.JE.JE2.UI.UIElements.Checkboxes.StyledCheckbox;
import org.JE.JE2.UI.UIElements.PreBuilt.FPSCounter;
import org.JE.JE2.UI.UIElements.Sliders.StyledSlider;
import org.JE.JE2.UI.UIElements.Style.Color;
import org.JE.JE2.UI.UIElements.TextField;
import org.JE.JE2.UI.UIElements.UIElement;
import org.JE.JE2.UI.UIObjects.UIWindow;
import org.JE.JE2.Window.WindowPreferences;
import org.joml.Vector2f;
import org.joml.Vector3f;

import java.util.ArrayList;

import static org.lwjgl.nuklear.Nuklear.*;
import static org.lwjgl.opengl.GL11.GL_LINE_LOOP;

public class Main {

    public static void main(String[] args) {
        Manager.start(new WindowPreferences(800,800, "JE2", false, true));

        Logger.logErrors = true;

        Scene scene = new Scene();

        GameObject player = GameObject.Sprite(ShaderProgram.lightSpriteShader(), new Texture(ResourceLoader.getBytes("texture1.png")), new Texture(ResourceLoader.getBytes("texture1_N.png")));
        player.addScript(new PhysicsBody());
        player.addScript(new PlayerScript());

        scene.add(PointLight.pointLightObject(new Vector2f(1,-1), new Vector3f(1,1,1), 10, 1f));

        MovementController mc = new MovementController();
        mc.physicsBased = true;
        mc.canMoveDown = false;
        player.addScript(mc);
        player.addScript(new Camera());
        player.setPosition(2,0 );
        scene.setCamera(player.getScript(Camera.class));

        //scene.add(AmbientLight.ambientLightObject(1, Color.WHITE));

        Manager.addKeyReleasedCallback((key, mods) -> {
           if(key == Keyboard.nameToCode("E")){
                Vector2f pos = new Vector2f(player.getTransform().position());
                Vector2f mousePos = Mouse.getMouseWorldPosition();
                Vector2f dir = mousePos.sub(pos).normalize();
                Raycast r = player.getScript(PhysicsBody.class).raycast(player.getTransform().position().add(0.5f,0.1f),pos.add(dir.mul(2)),0);
                System.out.println(r.gameObjectHit());
            }
        });

        ArrayList<UIElement> elements = new ArrayList<>();
        TextField t = new TextField(32);
        t.setValue("adasd");
        elements.add(t);
        StyledSlider coolSlider = new StyledSlider();
        FPSCounter counter = new FPSCounter("FPS: ");
        elements.add(counter);
        elements.add(new StyledButton("Toggle Slider Activation", () -> coolSlider.setActive(!coolSlider.isActive())));
        elements.add(coolSlider);
        elements.add(new StyledCheckbox());
        elements.add(new ImageButton(ResourceLoader.getBytes("texture1.png")));
        scene.addUI(new UIWindow("Cool window",
                NK_WINDOW_TITLE|NK_WINDOW_BORDER|NK_WINDOW_MINIMIZABLE|NK_WINDOW_SCALABLE|NK_WINDOW_MOVABLE|NK_WINDOW_CLOSABLE,
                new Vector2f(100,100), elements));

        GameObject go = new GameObject();
        go.addScript(new ShapeRenderer());
        ((ShapeRenderer)go.getRenderer()).setPoints(new Vector2f[]{
                new Vector2f(0,0),
                new Vector2f(1,0),
                new Vector2f(1,1),
                new Vector2f(0,1)
        });
        go.addScript(new PhysicsBody());
        go.getPhysicsBody().defaultRestitution = 0.8f;
        go.getPhysicsBody().defaultDensity = 0.1f;
        go.getPhysicsBody().defaultFriction = 0.05f;

        go.getRenderer().getVAO().setShaderProgram(ShaderProgram.lightSpriteShader());
        go.getRenderer().getVAO().getShaderProgram().supportsTextures = false;
        go.getTransform().translateY(-1.5f);
        go.getRenderer().setDrawMode(GL_LINE_LOOP);
        go.getRenderer().baseColor = Color.BLUE;
        scene.add(go);
        scene.add(player);

        scene.add(FloorFactory.createFloor(new Vector2f(-2,-4), new Vector2f(6,1)));
        scene.add(FloorFactory.createFloor(new Vector2f(4,-3), new Vector2f(1,4)));
        scene.add(FloorFactory.createFloor(new Vector2f(-3,-3), new Vector2f(1,4)));
        scene.add(FloorFactory.createFloor(new Vector2f(-2,1), new Vector2f(6,1)));

        Manager.setScene(scene);

    }
}