package org.JE.JE2;

import org.JE.JE2.IO.Logging.LogEntry;
import org.JE.JE2.IO.Logging.Logger;
import org.JE.JE2.Utility.FileDialogs;
import org.JE.JE2.Utility.InternetRequests;
import org.JE.JE2.Utility.RunnableGeneric;
import org.JE.JE2.Window.WindowPreferences;

import java.util.Arrays;
import java.util.HashMap;

import static org.JE.JE2.Examples.BasicScene.mainScene;

public class Main {

    public static void main(String[] args) {
        Logger.logEverything();
        Logger.quietLog = true;
        Logger.log(Arrays.toString(FileDialogs.getFiles("File!", "C:\\", new String[]{"txt","png","jpg",}, true)));
        Logger.log("1");
        Logger.log("2");
        Logger.log("3");
        Logger.log("4");

        System.out.println(Arrays.toString(Logger.getEntries()));
        /*InternetRequests.makeRequestAsync("https://api.github.com/users/NoahFreelove", "GET", null, "application/json", new RunnableGeneric<HashMap<String, Object>>() {
            @Override
            public void invoke(HashMap<String, Object> obj) {
                Logger.log(obj.toString());
            }
        });*/

        WindowPreferences preferences = new WindowPreferences(800,800, "JE2", true, true);
        Manager.start(preferences);
        Logger.logErrors = true;
        Manager.setScene(mainScene());
    }
}