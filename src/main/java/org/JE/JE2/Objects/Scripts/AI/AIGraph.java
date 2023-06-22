package org.JE.JE2.Objects.Scripts.AI;

import org.JE.JE2.Objects.Scripts.Script;

public class AIGraph extends Script {

    AIDecision[] functions;
    boolean[] runStatus;

    public AIGraph(){
        functions = new AIDecision[0];
        runStatus = new boolean[0];
    }

    public AIGraph(AIDecision decision)
    {
        functions = new AIDecision[]{decision};
        runStatus = new boolean[1];
    }


    public void runFunction(int index){
        runStatus[index] = true;
    }

    @Override
    public void update() {
        for (int i = 0; i < runStatus.length; i++) {
            if(runStatus[i])
                functions[i].check();
        }
    }
}
