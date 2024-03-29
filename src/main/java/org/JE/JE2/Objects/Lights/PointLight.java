package org.JE.JE2.Objects.Lights;

import org.JE.JE2.Objects.GameObject;
import org.JE.JE2.Rendering.Shaders.ShaderProgram;
import org.JE.JE2.UI.UIElements.Style.Color;
import org.joml.Vector2f;
import org.joml.Vector3f;

public class PointLight extends Light {
    public float quadratic = 1;
    public float linear = 1;
    public float constant = 1;
    public float radius = 5;
    public PointLight(){
        super(Color.WHITE(), 1, 1);
    }

    @Override
    protected void setLightSpecific(ShaderProgram shaderProgram, int index) {
        shaderProgram.setUniform1f("lights[" + index + "].quadratic", quadratic);
        shaderProgram.setUniform1f("lights[" + index + "].linear", linear);
        shaderProgram.setUniform1f("lights[" + index + "].constant", constant);
        shaderProgram.setUniform1f("lights[" + index + "].radius", radius);
    }

    public static GameObject pointLightObject(Vector2f position, float constant, float linear, float quadratic, float radius, float intensity){
        GameObject light = new GameObject();
        PointLight p = new PointLight();
        light.addScript(p);
        p.quadratic = quadratic;
        p.linear = linear;
        p.constant = constant;
        p.radius = radius;
        p.intensity = intensity;
        light.getTransform().setPosition(position);
        return light;
    }

    public boolean isObjectInsideRadius(GameObject gameObject){
        // get from center of object, so we take scale into account
        Vector2f scale = gameObject.getTransform().scale().div(2);
        return new Vector2f().add(gameObject.getTransform().position()).add(scale).distance(getAttachedObject().getTransform().position()) < radius;
    }

    public boolean isInsideRadius(Vector2f position){
        return position.distance(getAttachedObject().getTransform().position()) < radius;
    }
}
