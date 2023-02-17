package JE;

import JE.IO.UserInput.Keyboard.Keyboard;
import JE.IO.Logging.Logger;
import JE.IO.UserInput.Mouse.Mouse;
import JE.Objects.GameObject;
import JE.Objects.Lights.PointLight;
import JE.Objects.Scripts.Base.Script;
import JE.Objects.Scripts.Physics.Raycast;
import JE.Rendering.Camera;
import JE.Rendering.Texture;
import JE.SampleScripts.FloorFactory;
import JE.SampleScripts.MovementController;
import JE.SampleScripts.PlayerScript;
import JE.Objects.Scripts.Physics.PhysicsBody;
import JE.Objects.Lights.AmbientLight;
import JE.Rendering.Renderers.ShapeRenderer;
import JE.Rendering.Shaders.ShaderProgram;
import JE.Scene.Scene;
import JE.UI.UIElements.Buttons.ImageButton;
import JE.UI.UIElements.Buttons.StyledButton;
import JE.UI.UIElements.Checkboxes.StyledCheckbox;
import JE.UI.UIElements.PreBuilt.FPSCounter;
import JE.UI.UIElements.Sliders.StyledSlider;
import JE.UI.UIElements.Style.Color;
import JE.UI.UIElements.UIElement;
import JE.UI.UIObjects.UIWindow;
import JE.Window.WindowPreferences;

import org.joml.Vector2f;
import org.joml.Vector2i;
import org.joml.Vector4i;

import java.util.ArrayList;
import java.util.Arrays;

import static org.lwjgl.nuklear.Nuklear.*;
import static org.lwjgl.opengl.GL11.GL_LINE_LOOP;

public class Main {

    public static void main(String[] args) {
        Manager.start(new WindowPreferences(new Vector2i(800,800), "JE2", false, true));
        Logger.logErrors = true;

        Scene scene = new Scene();

        GameObject player = GameObject.Sprite(ShaderProgram.lightSpriteShader(), new Texture("bin/texture1.png"));
        player.addScript(new PhysicsBody());
        player.addScript(new PlayerScript());
        PointLight p = new PointLight();
        p.offset = new Vector2f(0.5f,0.5f);
        p.radius = 5f;
        p.intensity = 2f;
        //player.addScript(p);

        MovementController mc = new MovementController();
        mc.physicsBased = true;
        mc.canMoveDown = false;
        player.addScript(mc);
        player.addScript(new Camera());
        player.setPosition(2,0 );
        scene.setCamera(player.getScript(Camera.class));
        GameObject ambientObject = new GameObject();
        AmbientLight ambient = new AmbientLight();
        ambientObject.addScript(ambient);
        scene.add(ambientObject);

        Manager.addKeyReleasedCallback((key, mods) -> {
            if(key == Keyboard.nameToCode("Q")){
                if(ambient.affectedLayers[0] == 0){
                    ambient.affectedLayers[0] = 1;
                }
                else ambient.affectedLayers[0] = 0;
            }
            else if(key == Keyboard.nameToCode("E")){
                Vector2f pos = new Vector2f(player.getTransform().position());
                Vector2f mousePos = Mouse.getMouseWorldPosition();
                Vector2f dir = mousePos.sub(pos).normalize();

                Raycast r = player.getScript(PhysicsBody.class).raycast(player.getTransform().position().add(0.1f,0.1f),pos.add(dir.mul(2)),0);
                System.out.println(r.gameObjectHit());
            }
        });

        ArrayList<UIElement> elements = new ArrayList<>();
        StyledSlider coolSlider = new StyledSlider();
        FPSCounter counter = new FPSCounter("FPS: ");
        elements.add(counter);
        elements.add(new StyledButton("Toggle Slider Activation", () -> coolSlider.setActive(!coolSlider.isActive())));
        elements.add(coolSlider);
        elements.add(new StyledCheckbox());
        elements.add(new ImageButton("bin/texture1.png"));
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
        go.getRenderer().getVAO().setShaderProgram(ShaderProgram.lightSpriteShader());
        go.getRenderer().getVAO().getShaderProgram().supportsTextures = false;
        go.getTransform().translateY(-1.5f);
        go.getRenderer().setDrawMode(GL_LINE_LOOP);
        go.getRenderer().baseColor = Color.BLUE;
        scene.add(go);

        scene.add(player);

        scene.add(FloorFactory.createFloor(new Vector2f(-2,-4), new Vector2f(6,1)));
        GameObject right = FloorFactory.createFloor(new Vector2f(4,-3), new Vector2f(1,4));
        //right.setLayer(1);
        scene.add(right);
        scene.add(FloorFactory.createFloor(new Vector2f(-3,-3), new Vector2f(1,4)));
        scene.add(FloorFactory.createFloor(new Vector2f(-2,1), new Vector2f(6,1)));

        Manager.setScene(scene);
        go.getPhysicsBody().activeFixture.setRestitution(0.5f);
        go.getPhysicsBody().activeFixture.setDensity(0.1f);
        go.getPhysicsBody().activeFixture.m_friction = 0.1f;

    }
}