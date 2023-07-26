package org.JE.JE2.Rendering;

import org.JE.JE2.UI.UIElements.Style.Color;
import org.joml.Vector3f;

public class Material {
    private Vector3f ambient;
    private Vector3f diffuse;
    private Vector3f specular;
    private float shininess;
    private Color baseColor = Color.WHITE;

    public Material(){
        this(new Vector3f(1), new Vector3f(1), new Vector3f(.5f), 0f);
    }

    public Material(Vector3f ambient, Vector3f diffuse, Vector3f specular, float shininess) {
        this.ambient = ambient;
        this.diffuse = diffuse;
        this.specular = specular;
        this.shininess = shininess;
    }

    public Vector3f getAmbient() {
        return ambient;
    }

    public void setAmbient(Vector3f ambient) {
        this.ambient = ambient;
    }

    public Vector3f getDiffuse() {
        return diffuse;
    }

    public void setDiffuse(Vector3f diffuse) {
        this.diffuse = diffuse;
    }

    public Vector3f getSpecular() {
        return specular;
    }

    public void setSpecular(Vector3f specular) {
        this.specular = specular;
    }

    public float getShininess() {
        return shininess;
    }

    public void setShininess(float shininess) {
        this.shininess = shininess;
    }

    public Color getBaseColor() {
        return baseColor;
    }

    public void setBaseColor(Color baseColor) {
        this.baseColor = baseColor;
    }
}
