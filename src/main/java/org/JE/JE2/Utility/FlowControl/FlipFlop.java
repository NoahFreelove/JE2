package org.JE.JE2.Utility.FlowControl;

public class FlipFlop {

    boolean state;
    public FlipFlop(){}
    private FlipFlop(boolean state){
        this.state = state;
    }

    public boolean flip(){
        return state = !state;
    }
}
