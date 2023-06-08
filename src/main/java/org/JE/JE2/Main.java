package org.JE.JE2;

import org.JE.JE2.IO.Logging.Logger;
import org.JE.JE2.Rendering.Texture;
import org.JE.JE2.Window.Window;
import org.JE.JE2.Window.WindowPreferences;

import java.math.BigDecimal;

import static org.JE.JE2.Examples.BasicScene.mainScene;

public class Main {

    public static void main(String[] args) {
        WindowPreferences preferences = new WindowPreferences(500,500, "JE2", true, true);
        Manager.start(preferences);
        Logger.logErrors = true;

        Manager.setScene(mainScene());
        Window.setWindowIcon(Texture.checkExistElseCreate("",-1,"texture2.png",false));
    }
}