package JE.Objects.Scripts.Base;

import JE.Logging.Errors.ScriptError;
import JE.Logging.Logger;
import JE.Objects.GameObject;
import JE.Scene.Scene;

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
    protected ScriptRestrictions restrictions = new ScriptRestrictions();
    private GameObject parentObject;

    public ScriptRestrictions getRestrictions(){
        return restrictions;
    }
    public void setRestrictions(ScriptRestrictions restrictions){
        this.restrictions = restrictions;
    }

    private boolean active = true;
    public boolean updateOnScriptUpdate = true;

    public Script(){}

    public void setActive (boolean newState){
        if(!restrictions.canBeDisabled && !newState){
            Logger.log(new ScriptError(this, "Script restrictions do not let it be disabled."));
            return;
        }
        active = newState;
    }
    public boolean getActive(){return active;}

    public void update(){}
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
}
