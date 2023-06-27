package org.JE.JE2.Objects.Scripts.AI;

public class AIDecision {

    boolean hasSucceeded;
    boolean inProgress;

    AICondition successCondition;
    AIDecision subsequentDecision;

    public AIDecision(AICondition successCondition, AIDecision subsequentDecision) {
        this.successCondition = successCondition;
        this.subsequentDecision = subsequentDecision;
    }

    private void start(){
        inProgress = true;
    }
    protected void success(){
        hasSucceeded = true;
        if(subsequentDecision !=null)
            subsequentDecision.start();
    }

    protected void failed(){
        hasSucceeded = false;

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
            else if(successCondition.failed()){
                inProgress = false;
                failed();
            }
        }
    }

}
