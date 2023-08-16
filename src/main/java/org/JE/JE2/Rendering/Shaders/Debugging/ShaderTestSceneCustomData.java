package org.JE.JE2.Rendering.Shaders.Debugging;

import org.JE.JE2.Objects.GameObject;
import org.JE.JE2.Objects.Lights.Light;
import org.JE.JE2.UI.UIElements.Style.Color;

import java.util.ArrayList;

public class ShaderTestSceneCustomData {
    public Color baseColor = Color.WHITE();

    public ArrayList<Light> sceneLights = new ArrayList<>();
    public ArrayList<GameObject> otherObjects = new ArrayList<>();
}
