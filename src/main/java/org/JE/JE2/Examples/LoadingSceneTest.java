package org.JE.JE2.Examples;

import org.JE.JE2.Manager;
import org.JE.JE2.Scene.SceneLoading.SceneLoadType;
import org.JE.JE2.Scene.SceneLoading.StaticLoadSequence;
import org.JE.JE2.Window.WindowPreferences;

public class LoadingSceneTest {
    public static void main(String[] args) {
        WindowPreferences preferences = new WindowPreferences(800,800, "JE2", false, true);
        Manager.start(preferences);

        StaticLoadSequence sls = new StaticLoadSequence(new SceneLoadType(BasicScene.mainScene()));
        //sls.forceLoadTimeMs = 3000;
        Manager.loadAndSetNextScene(sls);
    }
}
