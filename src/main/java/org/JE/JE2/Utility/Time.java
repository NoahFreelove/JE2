package org.JE.JE2.Utility;

import org.JE.JE2.Annotations.ReadOnly;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.TimeZone;

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
    private static Instant referenceTimestamp = Instant.parse("2000-01-01T00:00:00Z");
    public static long millis2000(){
        return ChronoUnit.MILLIS.between(referenceTimestamp, Instant.now());
    }
}
