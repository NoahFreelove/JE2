package JE;


import JE.Logging.Logger;
import JE.Objects.Common.Player;
import JE.Objects.Lights.PointLight;
import JE.Scene.Scene;
import JE.UI.UIElements.*;
import JE.UI.UIElements.Buttons.ImageButton;
import JE.UI.UIElements.Buttons.StyledButton;
import JE.UI.UIElements.Buttons.TextImageButton;
import JE.UI.UIElements.Checkboxes.StyledCheckbox;
import JE.UI.UIElements.Sliders.Slider;
import JE.UI.UIElements.Sliders.StyledSlider;
import JE.UI.UIObjects.UIWindow;
import org.joml.Vector2f;
import org.joml.Vector2i;
import org.joml.Vector4f;

import java.util.ArrayList;

import static org.lwjgl.nuklear.Nuklear.*;
import static org.lwjgl.nuklear.Nuklear.NK_WINDOW_CLOSABLE;

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
        light.intensity = 10f;
        light.radius = 5;

        scene.addLight(light);
        //scene.addGizmo(light.getRangeGizmo());

        ArrayList<UIElement> elements = new ArrayList<>();
        StyledSlider coolSlider = new StyledSlider();
        elements.add(new ImageButton("bin/texture1.png").setDimensions(new Vector2f(256,256)));
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

        /*ShaderTestSceneCustomData data = new ShaderTestSceneCustomData();
        data.baseColor = new Vector4f(0,1,0,1);
        data.sceneLights.add(new PointLight(new Vector2f(0,0), new Vector4f(1,1,1,1), 5f,5));
        ShaderDebugger.TestShader(new LightSpriteShader(),data);*/
    }
}