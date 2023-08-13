package org.JE.JE2.Objects.Scripts;

import org.JE.JE2.Annotations.ActPublic;
import org.JE.JE2.IO.Logging.Errors.ScriptError;
import org.JE.JE2.IO.Logging.Logger;
import org.JE.JE2.Objects.GameObject;
import org.JE.JE2.Objects.Scripts.LambdaScript.ILambdaScript;
import org.JE.JE2.Scene.Scene;
import java.io.Serializable;

/**
 JE2 - Script
 @author Noah Freelove

 A script is like a GameObject as it has it's own start and update methods.
 However, A script has no transform and has to be attached to an object.
 You can think of it as a script you attach to your objects.
 Scripts MUST have a default constructor if you want to use *future* JE2 save and load features
 **/
public class Script implements Serializable {
    @ActPublic
    protected ScriptRestrictions restrictions = new ScriptRestrictions();
    protected ILambdaScript externalScriptBehaviourPre = new ILambdaScript() {};
    protected ILambdaScript externalScriptBehaviourPost = new ILambdaScript() {};

    private transient GameObject parentObject;

    private boolean allowSaving = true;

    public ScriptRestrictions getRestrictions(){
        if(restrictions == null)
            this.restrictions = new ScriptRestrictions();
        return restrictions;
    }

    @ActPublic
    private boolean active = true;

    public boolean updateOnScriptUpdate = true;

    public Script(){}

    public void setActive(boolean newState){
        if(!restrictions.canBeDisabled && !newState){
            Logger.log(new ScriptError(this, "Script restrictions do not let it be disabled."));
            return;
        }
        active = newState;
    }

    public void setRestrictions(ScriptRestrictions restrictions){
        this.restrictions = restrictions;
    }

    public boolean getActive(){return active;}

    public void update(){} // Every frame
    public void postRender(){} // After the scene is rendered
    public void start(){} // When the scene starts
    public void awake(){} // When active state is changed to true
    public void unload(Scene oldScene, Scene newScene){} // When scene is changed
    public void destroy(){} // When parent is removed from scene or script is removed from parent
    public void onForeignScriptAdded(Script script){} // When parent has a script added
    public void onAddedToGameObject(GameObject gameObject){} // When this script is added to a game object
    public void gameObjectAddedToScene(Scene scene){} // When parent is added to scene

    public GameObject getAttachedObject(){
        return parentObject;
    }

    public GameObject linked(){
        return parentObject;
    }

    public GameObject parent(){
        return parentObject;
    }


    public void setAttachedObject(GameObject newParent){
        if(parentObject !=null)
            parentObject.removeScript(this);
        this.parentObject = newParent;
    }

    public ILambdaScript getExternalScriptBehaviourPre() {
        return externalScriptBehaviourPre;
    }

    public void setExternalScriptBehaviourPre(ILambdaScript externalScriptBehaviourPre) {
        this.externalScriptBehaviourPre = externalScriptBehaviourPre;
    }

    public ILambdaScript getExternalScriptBehaviourPost() {
        return externalScriptBehaviourPost;
    }

    public void setExternalScriptBehaviourPost(ILambdaScript externalScriptBehaviourPost) {
        this.externalScriptBehaviourPost = externalScriptBehaviourPost;
    }

    public boolean allowSaving() {
        return allowSaving;
    }

    public void setAllowSaving(boolean allowSaving) {
        this.allowSaving = allowSaving;
    }
}
