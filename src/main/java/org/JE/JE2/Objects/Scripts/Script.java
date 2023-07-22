package org.JE.JE2.Objects.Scripts;

import org.JE.JE2.Annotations.ActPublic;
import org.JE.JE2.IO.Logging.Errors.ScriptError;
import org.JE.JE2.IO.Logging.Logger;
import org.JE.JE2.Objects.GameObject;
import org.JE.JE2.Objects.Scripts.LambdaScript.ILambdaScript;
import org.JE.JE2.Scene.Scene;
import org.JE.JE2.Utility.Loadable;

import java.io.Serializable;

/**
 JE2 - Script
 @author Noah Freelove

 A script is like a GameObject as it has it's own start and update methods.
 However, A script has no transform and has to be attached to an object.
 You can think of it as a script you attach to your objects.
 Scripts MUST have a default constructor if you want to use *future* JE2 save and load features
 **/
public class Script implements Serializable, Loadable {
    @ActPublic
    protected ScriptRestrictions restrictions = new ScriptRestrictions();
    protected ILambdaScript externalScriptBehaviourPre = new ILambdaScript() {};
    protected ILambdaScript externalScriptBehaviourPost = new ILambdaScript() {};

    private transient GameObject parentObject;

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

    public void update(){}
    public void postRender(){}
    public void start(){}
    public void awake(){}
    public void unload(Scene oldScene, Scene newScene){}
    public void destroy(){}
    public void onForeignScriptAdded(Script script){}
    public void onAddedToGameObject(GameObject gameObject){}
    public void gameObjectAddedToScene(Scene scene){}

    public GameObject getAttachedObject(){
        return parentObject;
    }

    public void setAttachedObject(GameObject newParent){
        if(parentObject !=null)
            parentObject.removeScript(this);
        this.parentObject = newParent;
    }

    public void load() {
        this.restrictions = new ScriptRestrictions();
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
}
