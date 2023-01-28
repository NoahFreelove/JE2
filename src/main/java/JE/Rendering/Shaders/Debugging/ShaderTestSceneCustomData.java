package JE.Rendering.Shaders.Debugging;

import JE.Objects.Base.GameObject;
import JE.Objects.Lights.Light;
import JE.Objects.Lights.PointLight;
import JE.UI.UIElements.Style.Color;
import org.joml.Vector2f;
import org.joml.Vector4f;

import java.util.ArrayList;

public class ShaderTestSceneCustomData {
    public Color baseColor = Color.WHITE;

    public ArrayList<Light> sceneLights = new ArrayList<>();
    public ArrayList<GameObject> otherObjects = new ArrayList<>();
}
