package org.JE.JE2.Utility;

import org.JE.JE2.Annotations.ReadOnly;

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
        Time.timeScale = JE2Math.clamp0(timeScale);
    }

    public static long systemTime() {
        return System.currentTimeMillis();
    }
}
