package JE.Objects.Components;

public class ComponentRestrictions {
    public boolean canHaveMultiple = true;
    public boolean canBeDisabled = true;
    public boolean canBeRemoved = true;

    public ComponentRestrictions(){}

    public ComponentRestrictions(ComponentRestrictions restrictions){
        this.canHaveMultiple = restrictions.canHaveMultiple;
        this.canBeDisabled = restrictions.canBeDisabled;
        this.canBeRemoved = restrictions.canBeRemoved;
    }

    public ComponentRestrictions(boolean canHaveMultiple, boolean canBeDisabled, boolean canBeRemoved){
        this.canHaveMultiple = canHaveMultiple;
        this.canBeDisabled = canBeDisabled;
        this.canBeRemoved = canBeRemoved;
    }
}
