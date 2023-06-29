package org.JE.JE2.Utility;

import org.JE.JE2.Annotations.ReadOnly;
import org.JE.JE2.Window.Window;

public class Time {

    @ReadOnly
    public static float deltaTime = 0;
    private static float realDeltaTime = 0;
    private static float timeScale = 1;

    public static float deltaTime(){
        return realDeltaTime * timeScale;
    }

    public static float deltaTimeIndependent(){
        return realDeltaTime;
    }


    public static void setDeltaTime(float deltaTime) {
        Time.realDeltaTime = deltaTime;
        Time.deltaTime = deltaTime;
    }

    public static void setTimeScale(float timeScale) {
        Time.timeScale = timeScale;
    }
}
