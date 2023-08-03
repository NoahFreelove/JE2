package org.JE.JE2.Window;

public class GLAgent {
    public static final int COMPLETED = 2;
    public static final int IN_PROGRESS = 1;
    public static final int QUEUED = 0;
    public static final int NOT_QUEUED = -1;
    private final Runnable process;
    private int status = NOT_QUEUED;
    public GLAgent(Runnable process){
        this.process = process;
    }

    public void updateStatus(int newStatus){
        status = newStatus;
    }

    public Runnable PROCESS(){
        return process;
    }

    public int STATUS(){
        return status;
    }
}
