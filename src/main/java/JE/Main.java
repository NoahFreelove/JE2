package JE;

import JE.IO.UserInput.Keyboard.Keyboard;
import JE.Logging.Logger;
import JE.Objects.Base.GameObject;
import JE.Objects.Base.Sprite;
import JE.Objects.Components.Physics.BoxCollider;
import JE.Sample.Objects.Player;
import JE.Objects.Components.Physics.PhysicsBody;
import JE.Objects.Lights.AmbientLight;
import JE.Rendering.Renderers.ShapeRenderer;
import JE.Rendering.Shaders.ShaderProgram;
import JE.Rendering.Texture;
import JE.Scene.Scene;
import JE.UI.UIElements.Buttons.StyledButton;
import JE.UI.UIElements.Checkboxes.StyledCheckbox;
import JE.UI.UIElements.UIImage;
import JE.UI.UIElements.PreBuilt.FPSCounter;
import JE.UI.UIElements.Sliders.StyledSlider;
import JE.UI.UIElements.Style.Color;
import JE.UI.UIElements.UIElement;
import JE.UI.UIObjects.UIWindow;
import JE.Window.WindowPreferences;
import org.jbox2d.dynamics.BodyType;
import org.joml.Vector2f;
import org.joml.Vector2i;
import org.joml.Vector4i;

import java.util.ArrayList;
import java.util.Arrays;

import static org.lwjgl.nuklear.Nuklear.*;
import static org.lwjgl.opengl.GL11.GL_LINE_LOOP;

public class Main {

    public static void main(String[] args) {
        Manager.start(new WindowPreferences(new Vector2i(1000,1000), "JE2", false, true));
        Logger.logErrors = false;

        Scene scene = new Scene();
        Manager.setScene(scene);

        Player player = new Player();
        player.setPosition(2,1);
        scene.activeCamera = player.camera;
        scene.activeCamera.viewportSize = new Vector4i(0,0,1000,1000);

        /*PointLight pointLight = new PointLight();
        pointLight.getTransform().setPosition(new Vector2f(0,0));
        pointLight.intensity = 15;
        pointLight.radius = 15;
        scene.addLight(pointLight);*/

        /*AreaLight areaLight = new AreaLight();
        scene.addLight(areaLight);*/

        AmbientLight ambient = new AmbientLight();
        scene.addLight(ambient);

        Manager.addKeyReleasedCallback((key, mods) -> {
            if(key == Keyboard.nameToCode("Q")){
                if(ambient.affectedLayers[0] == 0){
                    ambient.affectedLayers[0] = 1;
                }
                else ambient.affectedLayers[0] = 0;
            }
            else if(key == Keyboard.nameToCode("E")){
                System.out.println(Arrays.toString(player.getChildren()));
            }
        });

        ArrayList<UIElement> elements = new ArrayList<>();
        StyledSlider coolSlider = new StyledSlider();
        FPSCounter counter = new FPSCounter("FPS: ");
        elements.add(counter);
        //elements.add(new ImageButton("bin/texture1.png").setDimensions(new Vector2f(256,256)));
        elements.add(new StyledButton("Toggle Slider Activation", () -> coolSlider.setActive(!coolSlider.isActive())));
        elements.add(coolSlider);
        elements.add(new StyledCheckbox());
        elements.add(new UIImage("bin/texture1.png"));
        scene.addUI(new UIWindow("Cool window",
                NK_WINDOW_TITLE|NK_WINDOW_BORDER|NK_WINDOW_MINIMIZABLE|NK_WINDOW_SCALABLE|NK_WINDOW_MOVABLE|NK_WINDOW_CLOSABLE,
                new Vector2f(100,100), elements));

        GameObject go = new GameObject();
        go.addComponent(new ShapeRenderer());
        ((ShapeRenderer)go.renderer).setPoints(new Vector2f[]{
                new Vector2f(0,0),
                new Vector2f(1,0),
                new Vector2f(1,1),
                new Vector2f(0,1)
        });
        go.getTransform().translateY(-1.5f);
        go.renderer.setDrawMode(GL_LINE_LOOP);
        //go.addComponent(new PhysicsBody().create(BodyType.DYNAMIC, go.getTransform().position(), new Vector2f(1,1)));
        go.addComponent(new BoxCollider().create(go.getTransform().position(), new Vector2f(1,1)));
        go.renderer.baseColor = Color.BLUE;
        go.setParent(player);
        scene.add(player);

        Sprite floor2 = new Sprite();
        floor2.setTexture(new Texture("bin/texture2.png"));
        floor2.setScale(6,1);
        floor2.setPosition(-1,-4f);
        floor2.addComponent(new PhysicsBody().create(BodyType.STATIC, floor2.getTransform().position(), new Vector2f(6,1)));
        scene.add(floor2);

        for (int i = 0; i < 5; i++) {
            Sprite staticFloor = new Sprite(ShaderProgram.lightSpriteShader());
            staticFloor.setTexture(new Texture("bin/texture2.png"));
            staticFloor.setScale(1,1);
            staticFloor.setPosition(i+5,i-3);
            staticFloor.addComponent(new PhysicsBody().create(BodyType.STATIC, staticFloor.getTransform().position(), new Vector2f(1,1)));
            scene.add(staticFloor);
        }
    }

}