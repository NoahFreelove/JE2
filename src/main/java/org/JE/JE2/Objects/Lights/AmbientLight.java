package org.JE.JE2.Objects.Lights;

import org.JE.JE2.Annotations.GLThread;
import org.JE.JE2.Objects.GameObject;
import org.JE.JE2.Rendering.Shaders.ShaderProgram;
import org.JE.JE2.UI.UIElements.Style.Color;

public class AmbientLight extends Light{
    public AmbientLight() {
        super(Color.WHITE,1,0);
    }

    @Override
    @GLThread
    protected void setLightSpecific(ShaderProgram shaderProgram, int index) {}

    public static GameObject ambientLightObject(float intensity, Color color){
        GameObject ambient = new GameObject();
        AmbientLight p = new AmbientLight();
        ambient.addScript(p);
        p.intensity = intensity;
        p.color = color;
        return ambient;
    }
}
