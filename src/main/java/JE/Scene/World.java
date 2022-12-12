package JE.Scene;

import JE.Audio.AudioSourcePlayer;
import JE.Objects.Base.GameObject;
import JE.Objects.Gizmos.Gizmo;
import JE.Objects.Lights.PointLight;
import org.jbox2d.common.Vec2;

import java.util.concurrent.CopyOnWriteArrayList;

public class World {
    public CopyOnWriteArrayList<PointLight> lights = new CopyOnWriteArrayList<>();
    public CopyOnWriteArrayList<GameObject> gameObjects = new CopyOnWriteArrayList<>();
    public CopyOnWriteArrayList<Gizmo> gizmos = new CopyOnWriteArrayList<>();
    public CopyOnWriteArrayList<AudioSourcePlayer> sounds = new CopyOnWriteArrayList<>();

    public org.jbox2d.dynamics.World physicsWorld = new org.jbox2d.dynamics.World(new Vec2(0,-9.8f));
}
