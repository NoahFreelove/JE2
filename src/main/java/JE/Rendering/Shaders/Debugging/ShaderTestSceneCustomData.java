package JE.Rendering.Shaders.Debugging;

import JE.Objects.Base.GameObject;
import JE.Objects.Lights.PointLight;
import org.joml.Vector2f;
import org.joml.Vector4f;

import java.util.ArrayList;

public class ShaderTestSceneCustomData {
    public Vector2f[] vertices = new Vector2f[]{
            new Vector2f(0,0),
            new Vector2f(1,0),
            new Vector2f(1,1),
            new Vector2f(0,1)
    };
    public Vector4f baseColor = new Vector4f(1,1,1,1);

    public ArrayList<PointLight> sceneLights = new ArrayList<>();
    public ArrayList<GameObject> otherObjects = new ArrayList<>();
}
