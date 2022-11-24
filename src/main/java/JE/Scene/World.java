package JE.Scene;

import JE.Objects.Base.GameObject;
import JE.Objects.Gizmos.Gizmo;
import JE.Objects.Lights.PointLight;

import java.util.ArrayList;

public class World {
    public ArrayList<PointLight> lights = new ArrayList<>(){{
    }};
    public ArrayList<GameObject> gameObjects = new ArrayList<>(){{
    }};
    public ArrayList<Gizmo> gizmos = new ArrayList<>(){{
    }};
}
