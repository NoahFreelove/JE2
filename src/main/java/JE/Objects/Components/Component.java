package JE.Objects.Components;

import java.io.Serializable;

public abstract class Component implements Serializable {
    protected ComponentRestrictions restrictions = new ComponentRestrictions();

    public ComponentRestrictions getRestrictions(){
        return new ComponentRestrictions(restrictions);
    }

    private boolean active = true;

    public void setActive (boolean newState){
        if(!restrictions.canBeDisabled && !newState){
            return;
        }
        active = newState;
    }
    public boolean getActive(){return active;}

    public abstract void update();
    public abstract void start();
    public abstract void awake();


}
