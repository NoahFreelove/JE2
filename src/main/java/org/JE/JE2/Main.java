package org.JE.JE2;

import org.JE.JE2.IO.Logging.Logger;
import org.JE.JE2.Rendering.Texture;
import org.JE.JE2.Window.Window;
import org.JE.JE2.Window.WindowPreferences;

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
        Window.setWindowIcon(Texture.createTexture("texture2.png",false));
    }
}