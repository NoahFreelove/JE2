package org.JE.JE2.Objects.Lights;

import org.JE.JE2.Objects.GameObject;
import org.JE.JE2.Rendering.Shaders.ShaderProgram;
import org.JE.JE2.UI.UIElements.Style.Color;
import org.joml.Vector2f;
import org.joml.Vector3f;

public class PointLight extends Light {
    public Vector3f diffuse = new Vector3f(0.8f, 0.8f, 0.8f);
    public float radius = 5;

    public PointLight(){
        super(Color.WHITE, 1, 1);
    }

    @Override
    protected void setLightSpecific(ShaderProgram shaderProgram, int index) {
        shaderProgram.setUniform3f("lights[" + index + "].diffuse", diffuse);
        shaderProgram.setUniform1f("lights[" + index + "].radius", radius);
    }

    public static GameObject pointLightObject(Vector2f position, Vector3f diffuse, float radius, float intensity){
        GameObject light = new GameObject();
        PointLight p = new PointLight();
        light.addScript(p);
        p.diffuse = diffuse;
        p.radius = radius;
        p.intensity = intensity;
        light.getTransform().setPosition(position);
        return light;
    }

}
