package JE.Rendering.Shaders.Debugging;

import JE.Objects.GameObject;
import JE.Objects.Lights.Light;
import JE.UI.UIElements.Style.Color;

import java.util.ArrayList;

public class ShaderTestSceneCustomData {
    public Color baseColor = Color.WHITE;

    public ArrayList<Light> sceneLights = new ArrayList<>();
    public ArrayList<GameObject> otherObjects = new ArrayList<>();
}
