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
import org.JE.JE2.Utility.Time;
import org.JE.JE2.Utility.Timeline.InterpolateFunctions.Vector2InterpolateFunc;
import org.JE.JE2.Utility.Timeline.Timeline;
import org.JE.JE2.Utility.Timeline.Track;
import org.JE.JE2.Utility.Timeline.TrackPoint;
import org.JE.JE2.Utility.Watcher;
import org.JE.JE2.Window.Window;
import org.JE.JE2.Window.WindowPreferences;
import org.joml.Vector2f;
import org.joml.Vector3f;

import static org.JE.JE2.Examples.BasicScene.mainScene;

public class Main {

    public static void main(String[] args) {
        UIScaler.BASE_WINDOW_WIDTH = 1280;
        UIScaler.BASE_WINDOW_HEIGHT = 720;
        WindowPreferences preferences = new WindowPreferences(
                1920,1080,
                "JE2",
                false, true, true);

        Logger.logErrors = true;
        Logger.logThreshold = 2;

        Manager.start(preferences);
        Manager.indexAndSet(mainScene());
        Manager.activeScene().addUI(SceneSwitcherUI.getWindow());


        Timeline timeline = new Timeline();
        timeline.setEnd(2);
        TrackPoint<Vector2f> start = new TrackPoint<>(new Vector2f(0,0), 0);
        TrackPoint<Vector2f> mid = new TrackPoint<>(new Vector2f(1,1), 0.5f);
        TrackPoint<Vector2f> end = new TrackPoint<>(new Vector2f(2,2), 1);
        TrackPoint<Vector2f> end2 = new TrackPoint<>(new Vector2f(0,0), 2);

        Track<Vector2f> positionTrack = new Track<>(new Vector2InterpolateFunc() {},start,mid,end,end2);
        timeline.addTrack(positionTrack);

        Manager.activeScene().watchers.add(() -> {
            timeline.update(Time.deltaTime());
            //Logger.log(positionTrack.getRecentValue());

        });
    }
}