package JE.Rendering.Materials;

import org.joml.Vector3f;

public class Material {
    public float shininess = 1;
    public float roughness = 0;
    public float metallic = 0;
    public float reflectivity = 0;
    public float ambient = 0.1f;
    public float diffuse = 1;
    public float specular = 1;
    public float opacity = 1;

    public Vector3f baseColor = new Vector3f(1,1,1);

    public Material(){

    }

    public Material(Vector3f baseColor){
        this.baseColor = baseColor;
    }

    public Material(Vector3f baseColor, float shininess, float roughness, float metallic, float reflectivity, float ambient, float diffuse, float specular, float opacity){
        this.baseColor = baseColor;
        this.shininess = shininess;
        this.roughness = roughness;
        this.metallic = metallic;
        this.reflectivity = reflectivity;
        this.ambient = ambient;
        this.diffuse = diffuse;
        this.specular = specular;
        this.opacity = opacity;
    }
}
