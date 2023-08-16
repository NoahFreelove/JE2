package org.JE.JE2.Objects.Lights;

import org.JE.JE2.Annotations.GLThread;
import org.JE.JE2.Rendering.Shaders.ShaderProgram;
import org.JE.JE2.UI.UIElements.Style.Color;
import org.joml.Vector2f;

public class AreaLight extends Light {
    public Vector2f boundPos = new Vector2f(-5,-5);
    public Vector2f boundSize = new Vector2f(10,10);
    public AreaLight() {
        super(Color.WHITE(),1,2);
    }

    @Override
    @GLThread
    protected void setLightSpecific(ShaderProgram shaderProgram, int index) {
        shaderProgram.setUniform2f("lights[" + index + "].bound_pos", boundPos);
        shaderProgram.setUniform2f("lights[" + index + "].bound_range", boundSize);
    }
}
