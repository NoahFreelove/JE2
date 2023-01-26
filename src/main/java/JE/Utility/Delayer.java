package JE.Utility;

public class Delayer {
    public boolean activated = false;
    private long duration;
    private long startTimestamp;
    public Delayer(long durationInMilliseconds){
        this.duration = durationInMilliseconds;
        this.startTimestamp = System.currentTimeMillis();
    }
    public boolean check(){
        if(activated)
            return false;
        long currTime = System.currentTimeMillis();
        if(currTime - startTimestamp >= duration){
            activated = true;
            return true;
        }
        return false;
    }
    public void reset(){
        activated = false;
        startTimestamp = System.currentTimeMillis();
    }
}
