package org.JE.JE2.Utility;

public class MethodTimer {
    public long startTime;
    public long endTime;

    public MethodTimer(){
        startTime = System.nanoTime();
    }

    public void end(){
        endTime = System.nanoTime();
    }

    public long getDurationNanoseconds(){
        return endTime - startTime;
    }

    public long getDurationMicroseconds(){
        return getDurationNanoseconds() / 1000;
    }

    public long getDurationMilliseconds(){
        return getDurationNanoseconds() / 1000000;
    }

    public long getDurationSeconds(){
        return getDurationNanoseconds() / 1000000000;
    }
}
