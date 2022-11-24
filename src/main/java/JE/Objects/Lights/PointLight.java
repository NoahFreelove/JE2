package JE.Objects.Lights;

import JE.Objects.Base.GameObject;
import JE.Objects.Gizmos.GizmoParent;
import JE.Objects.Gizmos.Gizmo;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;

import static org.lwjgl.opengl.GL11.*;

public class PointLight extends GameObject {
    public Vector4f color = new Vector4f(1,1,1,1);
    public float intensity = 1;
    public float constant = 5f;
    public float linear = 0.09f;
    public float quadratic = 0.5f;
    public float radius = 5;
    public Vector3f ambient = new Vector3f(0.1f, 0.1f, 0.1f);
    public Vector3f diffuse = new Vector3f(0.8f, 0.8f, 0.8f);
    public Vector3f specular = new Vector3f(1.0f, 1.0f, 1.0f);

    public PointLight(Vector2f position, Vector4f color, float intensity, float radius){
        super();
        getTransform().position = new Vector2f(position.x(), position.y());
        this.intensity = intensity;
        this.color = color;
        this.radius = radius;
    }

    /*public PointLight(Vector2f position, float radius, Vector4f color, float intensity, float constant, float linear, float quadratic, Vector3f ambient, Vector3f diffuse, Vector3f specular){
        super();
        getTransform().position = new Vector2f(position.x(), position.y());
        this.radius = radius;
        this.intensity = intensity;
        this.color = color;
        this.constant = constant;
        this.linear = linear;
        this.quadratic = quadratic;
        this.ambient = ambient;
        this.diffuse = diffuse;
        this.specular = specular;
    }*/

    public GizmoParent getRangeGizmo(){

        Vector2f[] vertices = new Vector2f[360];
        for(int i = 0; i < 360; i++){
            vertices[i] = new Vector2f((float)Math.cos(Math.toRadians(i)) * radius, (float)Math.sin(Math.toRadians(i)) * radius);
        }
        Gizmo rangeGizmo = new Gizmo(vertices, color, GL_LINES);
        rangeGizmo.getTransform().position = getTransform().position;

        Gizmo pointGizmo = new Gizmo();
        pointGizmo.getTransform().scale = new Vector2f(0.5f,0.5f);
        pointGizmo.getTransform().position = getTransform().position;
        pointGizmo.renderer.baseColor = color;

        return new GizmoParent(pointGizmo, rangeGizmo);
    }
}
