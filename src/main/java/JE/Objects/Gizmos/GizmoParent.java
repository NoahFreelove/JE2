package JE.Objects.Gizmos;

import java.util.ArrayList;
import java.util.Arrays;

public class GizmoParent {
    public ArrayList<Gizmo> gizmos = new ArrayList<Gizmo>();

    public GizmoParent(){
    }

    public GizmoParent(ArrayList<Gizmo> gizmos){
        this.gizmos = gizmos;
    }
    public GizmoParent(Gizmo... gizmos)
    {
        this.gizmos.addAll(Arrays.asList(gizmos));
    }
}
