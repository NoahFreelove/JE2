package org.JE.JE2;

import org.JE.JE2.IO.Logging.Logger;
import org.JE.JE2.Objects.Scripts.Pathfinding.NavigableArea;
import org.JE.JE2.Objects.Scripts.Pathfinding.PathfindingActor;
import org.JE.JE2.Objects.Scripts.Pathfinding.SimplePathfindingAgent;
import org.JE.JE2.Rendering.Debug.RenderColoredArea;
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

        Manager.setScene(mainScene());

        Scene.addNow(RenderColoredArea.getRadius(new Vector2f(5,0),1, Color.RED.clone().a(0.2f)));

        Window.setWindowIcon(Texture.createTexture("texture2.png",false));
    }
}