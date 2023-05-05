package org.JE.JE2;

import org.JE.JE2.IO.Logging.Logger;
import org.JE.JE2.Utility.FileDialogs;
import org.JE.JE2.Window.WindowPreferences;

import static org.JE.JE2.Examples.BasicScene.mainScene;

public class Main {

    public static void main(String[] args) {
        /*System.out.println(FileDialogs.getFile("File!", "C:\\", new String[]{
                "*.txt",
                "*.png",
                "*.jpg",
        }, false).getAbsolutePath());*/
        WindowPreferences preferences = new WindowPreferences(800,800, "JE2", true, true);
        Manager.start(preferences);
        Logger.logErrors = true;
        Logger.logPetty = true;
        Manager.setScene(mainScene());
    }
}