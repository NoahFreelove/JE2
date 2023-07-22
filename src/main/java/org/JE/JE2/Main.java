package org.JE.JE2;

import org.JE.JE2.IO.Filepath;
import org.JE.JE2.IO.Logging.Logger;
import org.JE.JE2.IO.UserInput.Keyboard.KeyReleasedEvent;
import org.JE.JE2.IO.UserInput.Keyboard.Keyboard;
import org.JE.JE2.IO.UserInput.Mouse.Mouse;
import org.JE.JE2.Objects.Audio.AudioSourcePlayer;
import org.JE.JE2.Objects.GameObject;
import org.JE.JE2.Objects.Scripts.Pathfinding.NavigableArea;
import org.JE.JE2.Objects.Scripts.Pathfinding.PathfindingActor;
import org.JE.JE2.Objects.Scripts.Pathfinding.SimplePathfindingAgent;
import org.JE.JE2.Rendering.Debug.RenderColoredArea;
import org.JE.JE2.Rendering.Debug.SceneSwitcherUI;
import org.JE.JE2.Rendering.Shaders.Uniforms.UniformInt;
import org.JE.JE2.Rendering.Texture;
import org.JE.JE2.Scene.Scene;
import org.JE.JE2.UI.UIElements.Style.Color;
import org.JE.JE2.UI.UIScaler;
import org.JE.JE2.Utility.GarbageCollection;
import org.JE.JE2.Utility.MemoryReference;
import org.JE.JE2.Window.Window;
import org.JE.JE2.Window.WindowPreferences;
import org.joml.Vector2f;

import static org.JE.JE2.Examples.BasicScene.mainScene;

public class Main {

    public static void main(String[] args) {
        UIScaler.BASE_WINDOW_WIDTH = 1000;
        UIScaler.BASE_WINDOW_HEIGHT = 1000;
        WindowPreferences preferences = new WindowPreferences(
                1920,1080,
                "JE2",
                false, true, true);
        Manager.start(preferences);
        Logger.logErrors = true;
        Logger.logThreshold = 2;
        Scene s = Manager.activeScene();

        s.add(RenderColoredArea.getRadius(new Vector2f(5,0),2,30, Color.PASTEL_RED.clone().a(0.7f)));

        Manager.indexAndSet(mainScene());
        Manager.activeScene().addUI(SceneSwitcherUI.getWindow());

        GameObject go = new GameObject();
        MemoryReference<GameObject> ref = new MemoryReference<>(go);

        Keyboard.addKeyReleasedEvent((key, mods) -> {
            if(key == Keyboard.nameToCode("P")){
                //GarbageCollection.takeOutDaTrash();
                System.out.println(ref.hasBeenCollected());
            }
        });

        //Scene.addNow(RenderColoredArea.getRadius(new Vector2f(5,0),2,30, Color.PASTEL_BLUE.clone().a(0.7f)));

        //Window.setWindowIcon(Texture.createTexture("texture2.png",false));
    }
}