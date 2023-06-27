package org.JE.JE2;

import org.JE.JE2.IO.Logging.Logger;
import org.JE.JE2.IO.UserInput.Keyboard.KeyReleasedEvent;
import org.JE.JE2.IO.UserInput.Keyboard.Keyboard;
import org.JE.JE2.IO.UserInput.Mouse.Mouse;
import org.JE.JE2.Objects.Scripts.Pathfinding.NavigableArea;
import org.JE.JE2.Objects.Scripts.Pathfinding.PathfindingActor;
import org.JE.JE2.Objects.Scripts.Pathfinding.SimplePathfindingAgent;
import org.JE.JE2.Rendering.Debug.RenderColoredArea;
import org.JE.JE2.Rendering.Debug.SceneSwitcherUI;
import org.JE.JE2.Rendering.Shaders.Uniforms.UniformInt;
import org.JE.JE2.Rendering.Texture;
import org.JE.JE2.Scene.Scene;
import org.JE.JE2.UI.UIElements.Style.Color;
import org.JE.JE2.Window.Window;
import org.JE.JE2.Window.WindowPreferences;
import org.joml.Vector2f;

import static org.JE.JE2.Examples.BasicScene.mainScene;

public class Main {

    public static void main(String[] args) {
        WindowPreferences preferences = new WindowPreferences(
                1000,1000,
                "JE2",
                false, true, true);
        Manager.start(preferences);
        Logger.logErrors = true;
        Logger.logThreshold = 2;

        Manager.activeScene().add(RenderColoredArea.getRadius(new Vector2f(5,0),2,30, Color.PASTEL_RED.clone().a(0.7f)));
        Manager.indexAndSet(mainScene());
        //Manager.indexScene(mainScene());
        Manager.activeScene().addUI(SceneSwitcherUI.getWindow());


        //Scene.addNow(RenderColoredArea.getRadius(new Vector2f(5,0),2,30, Color.PASTEL_BLUE.clone().a(0.7f)));

        Window.setWindowIcon(Texture.createTexture("texture2.png",false));
    }
}