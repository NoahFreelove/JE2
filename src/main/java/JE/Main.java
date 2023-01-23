package JE;


import JE.Logging.Logger;
import JE.Objects.Base.GameObject;
import JE.Objects.Common.Player;
import JE.Objects.Common.Square;
import JE.Objects.Lights.PointLight;
import JE.Rendering.Renderers.ShapeRenderer;
import JE.Scene.Scene;
import JE.UI.UIElements.*;
import JE.UI.UIElements.Buttons.ImageButton;
import JE.UI.UIElements.Buttons.StyledButton;
import JE.UI.UIElements.Buttons.TextImageButton;
import JE.UI.UIElements.Checkboxes.StyledCheckbox;
import JE.UI.UIElements.PreBuilt.FPSCounter;
import JE.UI.UIElements.Sliders.Slider;
import JE.UI.UIElements.Sliders.StyledSlider;
import JE.UI.UIElements.Style.Color;
import JE.UI.UIObjects.UIWindow;
import JE.Window.WindowPreferences;
import org.joml.Vector2f;
import org.joml.Vector2i;
import org.joml.Vector4f;

import java.util.ArrayList;

import static org.lwjgl.nuklear.Nuklear.*;
import static org.lwjgl.nuklear.Nuklear.NK_WINDOW_CLOSABLE;
import static org.lwjgl.opengl.GL11.*;

public class Main {

    public static void main(String[] args) {
        Manager.start(new WindowPreferences(new Vector2i(1920,1080), "JE2", false, true));

        Scene scene = new Scene();
        Manager.setScene(scene);

        Player player = new Player();
        player.setPosition(-1,0);
        scene.add(player);
        scene.activeCamera = player.camera;

        PointLight light = new PointLight();
        light.getTransform().position = new Vector2f(0,0);
        light.color = new Vector4f(1,1,1,1);
        light.intensity = 10f;
        light.radius = 5;

        scene.addLight(light);

        ArrayList<UIElement> elements = new ArrayList<>();
        StyledSlider coolSlider = new StyledSlider();
        FPSCounter counter = new FPSCounter("FPS: ");
        elements.add(counter);
        //elements.add(new ImageButton("bin/texture1.png").setDimensions(new Vector2f(256,256)));
        elements.add(new StyledButton("Toggle Slider Activation", () -> coolSlider.setActive(!coolSlider.isActive())));
        elements.add(coolSlider);
        elements.add(new StyledCheckbox());
        scene.addUI(new UIWindow("Cool window",
                NK_WINDOW_TITLE|NK_WINDOW_BORDER|NK_WINDOW_MINIMIZABLE|NK_WINDOW_SCALABLE|NK_WINDOW_MOVABLE|NK_WINDOW_CLOSABLE,
                new Vector2f(100,100), elements));

        /*Manager.AddKeyPressedCallback((key, mods) -> {
            if(key == Keyboard.nameToCode("F")){
                Logger.log(Manager.getFPS());
            }
        });*/
        GameObject go = new GameObject();
        go.addComponent(new ShapeRenderer());
        ((ShapeRenderer)go.renderer).setPoints(new Vector2f[]{
                // create hexagon
                new Vector2f(0, 0.5f),
                new Vector2f(0.5f, 0.25f),
                new Vector2f(0.5f, -0.25f),
                new Vector2f(0, -0.5f),
                new Vector2f(-0.5f, -0.25f),
                new Vector2f(-0.5f, 0.25f),
                new Vector2f(0, 0.5f)
        });
        go.renderer.setDrawMode(GL_LINE_LOOP);
        go.renderer.baseColor = Color.BLUE.getVec4();
        scene.add(go);
        /*ShaderTestSceneCustomData data = new ShaderTestSceneCustomData();
        data.baseColor = new Vector4f(0,1,0,1);
        data.sceneLights.add(new PointLight(new Vector2f(0,0), new Vector4f(1,1,1,1), 5f,5));
        ShaderDebugger.TestShader(new LightSpriteShader(),data);*/
    }
}