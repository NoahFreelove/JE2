package JE.Objects.Components;

import java.util.ArrayList;
import java.util.Arrays;

public class ComponentRestrictions {
    public boolean canHaveMultiple = true;
    public boolean canBeDisabled = true;
    public boolean canBeRemoved = true;
    public ArrayList<Class> permittedClasses = new ArrayList<>();
    public boolean restrictClasses = false;

    public ComponentRestrictions(){}


    public ComponentRestrictions(boolean canHaveMultiple, boolean canBeDisabled, boolean canBeRemoved){
        this.canHaveMultiple = canHaveMultiple;
        this.canBeDisabled = canBeDisabled;
        this.canBeRemoved = canBeRemoved;
    }
    public ComponentRestrictions(boolean canHaveMultiple, boolean canBeDisabled, boolean canBeRemoved, Class[] permittedClasses, boolean restrictClasses){
        this.canHaveMultiple = canHaveMultiple;
        this.canBeDisabled = canBeDisabled;
        this.canBeRemoved = canBeRemoved;
        this.restrictClasses = restrictClasses;
        this.permittedClasses.addAll(Arrays.asList(permittedClasses));
    }
}
