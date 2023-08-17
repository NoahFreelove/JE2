package org.JE.JE2.Objects.Scripts.LambdaScript;

import org.JE.JE2.Objects.GameObject;
import org.JE.JE2.Objects.Scripts.Script;
import org.JE.JE2.Scene.Scene;

public class LambdaScript extends Script {
    ILambdaScript lambdaScript;
    public LambdaScript(){}
    public LambdaScript(ILambdaScript lambdaScript){
        this.lambdaScript = lambdaScript;
    }

    @Override
    public void start(){
        if(getAttachedObject() == null || lambdaScript == null)
            return;
        lambdaScript.start(getAttachedObject());
    }
    @Override
    public void update(){
        if(getAttachedObject() == null || lambdaScript == null)
            return;
        lambdaScript.update(getAttachedObject());
    }
    @Override
    public void awake(){
        if(getAttachedObject() == null || lambdaScript == null)
            return;
        lambdaScript.awake(getAttachedObject());
    }
    @Override
    public void unload(Scene oldScene, Scene newScene){
        if(getAttachedObject() == null || lambdaScript == null)
            return;
        lambdaScript.unload(oldScene, newScene);
    }
    @Override
    public void destroy(){
        if(getAttachedObject() == null || lambdaScript == null)
            return;
        lambdaScript.destroy(getAttachedObject());
    }
    @Override
    public void onForeignScriptAdded(Script script){
        if(getAttachedObject() == null || lambdaScript == null)
            return;
        lambdaScript.onForeignScriptAdded(script);
    }
    @Override
    public void onAddedToGameObject(GameObject gameObject){
        if(getAttachedObject() == null || lambdaScript == null)
            return;
        lambdaScript.onAddedToGameObject(gameObject);
    }
    @Override
    public void gameObjectAddedToScene(Scene scene){
        if(getAttachedObject() == null || lambdaScript == null)
            return;
        lambdaScript.gameObjectAddedToScene(scene);
    }
    @Override
    public void postRender(){
        if(getAttachedObject() == null || lambdaScript == null)
            return;
        lambdaScript.postRender(getAttachedObject());
    }


}

