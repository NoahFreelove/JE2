package JE.Scene;

import JE.Audio.SoundPlayer;
import JE.Objects.Base.GameObject;
import JE.Objects.Gizmos.Gizmo;
import JE.Objects.Lights.PointLight;
import org.jbox2d.common.Vec2;

import java.util.ArrayList;

public class World {
    public ArrayList<PointLight> lights = new ArrayList<>();
    public ArrayList<GameObject> gameObjects = new ArrayList<>();
    public ArrayList<Gizmo> gizmos = new ArrayList<>();
    public ArrayList<SoundPlayer> sounds = new ArrayList<>();

    public org.jbox2d.dynamics.World physicsWorld = new org.jbox2d.dynamics.World(new Vec2(0,-9.8f));
}
