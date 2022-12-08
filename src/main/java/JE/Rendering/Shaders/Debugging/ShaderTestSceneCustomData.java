package JE.Rendering.Shaders.Debugging;

import JE.Objects.Base.GameObject;
import JE.Objects.Lights.PointLight;
import org.joml.Vector2f;
import org.joml.Vector4f;

import java.util.ArrayList;

public class ShaderTestSceneCustomData {
    public Vector4f baseColor = new Vector4f(1,1,1,1);

    public ArrayList<PointLight> sceneLights = new ArrayList<>();
    public ArrayList<GameObject> otherObjects = new ArrayList<>();
}
