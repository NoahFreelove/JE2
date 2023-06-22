package org.JE.JE2.Objects.Scripts.AI;

public class AIDecision {

    AICondition successCondition;
    boolean hasSucceeded;
    boolean inProgress;

    AIDecision subsequentDecision;

    public AIDecision(AICondition successCondition, AIDecision subsequentDecision) {
        this.successCondition = successCondition;
        this.subsequentDecision = subsequentDecision;
    }

    public void start(){
        inProgress = true;
    }
    protected void success(){
        hasSucceeded = true;
        if(subsequentDecision !=null)
            subsequentDecision.start();
    }

    public void check(){
        if(!inProgress)
            start();
        else {
            if (hasSucceeded)
            {
                if(subsequentDecision !=null)
                    subsequentDecision.check();
            }
            if(successCondition.passed()){
                inProgress = false;
                success();
            }
        }
    }

}
