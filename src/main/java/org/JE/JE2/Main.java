package org.JE.JE2;

import org.JE.JE2.IO.Logging.Errors.JE2Error;
import org.JE.JE2.IO.Logging.Logger;
import org.JE.JE2.Window.WindowPreferences;

import static org.JE.JE2.Examples.BasicScene.mainScene;

public class Main {

    public static void main(String[] args) {
        WindowPreferences preferences = new WindowPreferences(400,400, "JE2", true, true);
        Manager.start(preferences);
        Logger.logErrors = true;
        Manager.setScene(mainScene());
    }
}