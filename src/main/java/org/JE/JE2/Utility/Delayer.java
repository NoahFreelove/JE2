package org.JE.JE2.Utility;

import java.io.Serializable;

public class Delayer implements Serializable {
    public boolean triggered = false;
    private long duration;
    private long startTimestamp;
    public Delayer(long durationInMilliseconds){
        this.duration = durationInMilliseconds;
        this.startTimestamp = System.currentTimeMillis();
    }
    public boolean check(){
        if(triggered)
            return false;
        long currTime = System.currentTimeMillis();
        if(currTime - startTimestamp >= duration){
            triggered = true;
            return true;
        }
        return false;
    }
    public void reset(){
        triggered = false;
        startTimestamp = System.currentTimeMillis();
    }
    public void setDuration(long duration)
    {
        this.duration = duration;
    }
}
