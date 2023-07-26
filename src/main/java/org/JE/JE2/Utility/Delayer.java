package org.JE.JE2.Utility;

import java.io.Serializable;

public class Delayer implements Serializable {
    public boolean triggered = false;
    private long duration;
    private long startTimestamp;
    private boolean forceTriggerable = false;
    public Delayer(long durationInMilliseconds){
        this.duration = durationInMilliseconds;
        this.startTimestamp = System.currentTimeMillis();
    }
    public Delayer(long durationInMilliseconds,boolean startTriggerable){
        this.duration = durationInMilliseconds;
        this.startTimestamp = System.currentTimeMillis();
        forceTriggerable = startTriggerable;
    }
    public boolean canTrigger(){
        long currTime = System.currentTimeMillis();
        if(currTime - startTimestamp >= duration || forceTriggerable){
            return true;
        }
        return false;
    }
    public boolean check(){
        if(triggered)
            return false;
        long currTime = System.currentTimeMillis();
        if(currTime - startTimestamp >= duration || forceTriggerable){
            triggered = true;
            forceTriggerable = false;
            return true;
        }
        return false;
    }
    public void reset(){
        triggered = false;
        forceTriggerable = false;
        startTimestamp = System.currentTimeMillis();
    }
    public void setDuration(long duration)
    {
        this.duration = duration;
    }
}
