package org.JE.JE2.Objects.Scripts.Base;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;

public class ScriptRestrictions implements Serializable {
    public boolean canHaveMultiple = true;
    public boolean canBeDisabled = true;
    public boolean canBeRemoved = true;
    public ArrayList<Class> permittedClasses = new ArrayList<>();
    public boolean restrictClasses = false;

    public ScriptRestrictions(){}


    public ScriptRestrictions(boolean canHaveMultiple, boolean canBeDisabled, boolean canBeRemoved){
        this.canHaveMultiple = canHaveMultiple;
        this.canBeDisabled = canBeDisabled;
        this.canBeRemoved = canBeRemoved;
    }
    public ScriptRestrictions(boolean canHaveMultiple, boolean canBeDisabled, boolean canBeRemoved, Class[] permittedClasses, boolean restrictClasses){
        this.canHaveMultiple = canHaveMultiple;
        this.canBeDisabled = canBeDisabled;
        this.canBeRemoved = canBeRemoved;
        this.restrictClasses = restrictClasses;
        this.permittedClasses.addAll(Arrays.asList(permittedClasses));
    }
}
