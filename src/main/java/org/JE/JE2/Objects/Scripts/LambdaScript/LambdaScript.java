package org.JE.JE2.Objects.Scripts.LambdaScript;

import org.JE.JE2.Objects.GameObject;
import org.JE.JE2.Objects.Scripts.Script;

public class LambdaScript extends Script {
    ILambdaScript lambdaScript;
    public LambdaScript(ILambdaScript lambdaScript){
        this.lambdaScript = lambdaScript;
    }

    @Override
    public void update(){
        if(getAttachedObject() == null || lambdaScript == null)
            return;
        lambdaScript.update(getAttachedObject());
    }
    @Override
    public void start(){
        if(getAttachedObject() == null || lambdaScript == null)
            return;
        lambdaScript.start(getAttachedObject());
    }
}

