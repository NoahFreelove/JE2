package org.JE.JE2.Tests;

import org.JE.JE2.Main;
import org.JE.JE2.Manager;
import org.JE.JE2.Objects.GameObject;
import org.JE.JE2.Rendering.Shaders.ShaderProgram;
import org.JE.JE2.Rendering.Texture;
import org.JE.JE2.Resources.Bundles.TextureBundle;
import org.JE.JE2.Resources.ResourceManager;
import org.JE.JE2.Scene.Scene;
import org.JE.JE2.Scene.SceneLoading.SceneLoadType;
import org.JE.JE2.Scene.SceneLoading.StaticLoadSequence;
import org.JE.JE2.Window.WindowPreferences;

public class LoadingSceneTest {
    public static void main(String[] args) {
        WindowPreferences preferences = new WindowPreferences(800,800, "JE2", false, true);
        Manager.start(preferences);

        StaticLoadSequence sls = new StaticLoadSequence(new SceneLoadType(Main.mainScene()));
        //sls.forceLoadTimeMs = 3000;
        Manager.loadAndSetNextScene(sls);
    }
}
