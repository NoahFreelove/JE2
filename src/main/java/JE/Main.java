package JE;

import JE.IO.Logging.Logger;
import JE.IO.UserInput.Keyboard.Keyboard;
import JE.IO.UserInput.Mouse.Mouse;
import JE.Objects.GameObject;
import JE.Objects.Lights.AmbientLight;
import JE.Objects.Lights.PointLight;
import JE.Objects.Scripts.Physics.PhysicsBody;
import JE.Objects.Scripts.Physics.Raycast;
import JE.Rendering.Camera;
import JE.Rendering.Renderers.ShapeRenderer;
import JE.Rendering.Shaders.ShaderProgram;
import JE.Rendering.Texture;
import JE.Resources.ResourceLoader;
import JE.SampleScripts.FloorFactory;
import JE.SampleScripts.MovementController;
import JE.SampleScripts.PlayerScript;
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