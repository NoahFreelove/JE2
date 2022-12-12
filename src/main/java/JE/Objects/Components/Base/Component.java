package JE.Objects.Components.Base;

import JE.Logging.Errors.ComponentError;
import JE.Logging.Logger;
import JE.Objects.Base.GameObject;
import JE.Scene.Scene;

import java.io.Serializable;

/**
 JE2 - Component
 @author Noah Freelove

 A component is like a GameObject as it has it's own start and update methods.
 However, A component has no transform and has to be attached to an object.
 You can think of it as a script you attach to your objects.
 Components MUST have a default constructor if you want to use *future* JE2 save and load features
 **/
public class Component implements Serializable {
    protected ComponentRestrictions restrictions = new ComponentRestrictions();
    public GameObject parentObject;

    public ComponentRestrictions getRestrictions(){
        return restrictions;
    }
    public void setRestrictions(ComponentRestrictions restrictions){
        this.restrictions = restrictions;
    }

    private boolean active = true;

    public Component(){}

    public void setActive (boolean newState){
        if(!restrictions.canBeDisabled && !newState){
            Logger.log(new ComponentError(this, "Component restrictions do not let it be disabled."));
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
    public void onAddedToGameObject(GameObject gameObject){}
    public void gameObjectAddedToScene(Scene scene){}


}
