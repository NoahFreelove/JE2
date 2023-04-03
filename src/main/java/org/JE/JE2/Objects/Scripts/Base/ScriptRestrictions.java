package org.JE.JE2.Objects.Scripts.Base;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

public class ScriptRestrictions implements Serializable {
    public final boolean canHaveMultiple;
    public final boolean canBeDisabled;
    public final boolean canBeRemoved;
    public final ArrayList<Class<?>> permittedClasses = new ArrayList<>();
    public final boolean restrictClasses;

    public ScriptRestrictions(){
        this.canHaveMultiple = true;
        this.canBeDisabled = true;
        this.canBeRemoved = true;
        this.restrictClasses = false;
    }

    public ScriptRestrictions(boolean canHaveMultiple, boolean canBeDisabled, boolean canBeRemoved){
        this.canHaveMultiple = canHaveMultiple;
        this.canBeDisabled = canBeDisabled;
        this.canBeRemoved = canBeRemoved;
        this.restrictClasses = false;
    }
    public ScriptRestrictions(boolean canHaveMultiple, boolean canBeDisabled, boolean canBeRemoved, Class<?>[] permittedClasses, boolean restrictClasses){
        this.canHaveMultiple = canHaveMultiple;
        this.canBeDisabled = canBeDisabled;
        this.canBeRemoved = canBeRemoved;
        this.restrictClasses = restrictClasses;
        this.permittedClasses.addAll(Arrays.asList(permittedClasses));
    }
}
