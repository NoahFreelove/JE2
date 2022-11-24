package JE.Objects.Components;

import JE.Logging.Errors.ComponentError;
import JE.Logging.Logger;
import JE.Objects.Base.GameObject;

import java.io.Serializable;

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

    public void setActive (boolean newState){
        if(!restrictions.canBeDisabled && !newState){
            Logger.log(new ComponentError(this, "Component restrictions do not let it be disabled."));
            return;
        }
        active = newState;
    }
    public boolean getActive(){return active;}

    public void Update(){}
    public void Start(){}
    public void awake(){}
    public void onAddedToGameObject(GameObject gameObject){}


}
