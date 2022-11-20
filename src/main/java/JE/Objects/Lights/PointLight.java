package JE.Objects.Lights;

import JE.Objects.Base.GameObject;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;

public class PointLight extends GameObject {
    public Vector4f color = new Vector4f(1,1,1,1);
    public float intensity = 1;
    public float constant = 0.5f;
    public float linear = 0.09f;
    public float quadratic = 1;
    public Vector3f ambient = new Vector3f(0.1f, 0.1f, 0.1f);
    public Vector3f diffuse = new Vector3f(0.8f, 0.8f, 0.8f);
    public Vector3f specular = new Vector3f(1.0f, 1.0f, 1.0f);

    public PointLight(Vector2f position, Vector4f color, float intensity){
        super();
        getTransform().position = new Vector2f(position.x(), position.y());
        this.intensity = intensity;
        this.color = color;
    }

    public PointLight(Vector2f position, Vector4f color, float intensity, float constant, float linear, float quadratic, Vector3f ambient, Vector3f diffuse, Vector3f specular){
        super();
        getTransform().position = new Vector2f(position.x(), position.y());
        this.intensity = intensity;
        this.color = color;
        this.constant = constant;
        this.linear = linear;
        this.quadratic = quadratic;
        this.ambient = ambient;
        this.diffuse = diffuse;
        this.specular = specular;
    }
}
