package org.JE.JE2.Utility;

public class MethodLimiter {
    private final Delayer delayer;
    public MethodLimiter(long millisBetweenCalls){
        delayer = new Delayer(millisBetweenCalls);
    }

    public boolean check(){
        if(delayer.check())
        {
            delayer.reset();
            return true;
        }
        else
            return false;
    }
}
