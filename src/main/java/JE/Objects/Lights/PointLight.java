package JE.Objects.Lights;

import JE.Objects.Gizmos.GizmoParent;
import JE.Objects.Gizmos.Gizmo;
import JE.Rendering.Shaders.ShaderProgram;
import JE.UI.UIElements.Style.Color;
import org.joml.Vector2f;
import org.joml.Vector3f;

import static org.lwjgl.opengl.GL11.*;

public class PointLight extends Light {
    public Vector3f diffuse = new Vector3f(0.8f, 0.8f, 0.8f);
    public float radius = 5;

    public PointLight(){
        super(Color.WHITE, 1,1);
        this.type = 1;
    }

    @Override
    protected void setLightSpecific(ShaderProgram shaderProgram, int index) {
        shaderProgram.setUniform3f("lights[" + index + "].diffuse", diffuse);
        shaderProgram.setUniform1f("lights[" + index + "].radius", radius);
    }

    public GizmoParent getRangeGizmo(){

        Vector2f[] vertices = new Vector2f[360];
        for(int i = 0; i < 360; i++){
            vertices[i] = new Vector2f((float)Math.cos(Math.toRadians(i)) * radius, (float)Math.sin(Math.toRadians(i)) * radius);
        }
        Gizmo rangeGizmo = new Gizmo();
        rangeGizmo.setVertices(vertices);
        rangeGizmo.setBaseColor(color);
        rangeGizmo.setDrawMode(GL_LINES);
        rangeGizmo.getTransform().setPosition(new Vector2f(getTransform().position().x+0.5f,getTransform().position().y+0.5f));

        Gizmo pointGizmo = new Gizmo();
        pointGizmo.getTransform().setScale(new Vector2f(0.5f,0.5f));
        pointGizmo.getTransform().setPosition(new Vector2f(getTransform().position().x+0.25f,getTransform().position().y+0.25f));
        pointGizmo.renderer.baseColor = color;

        return new GizmoParent(pointGizmo, rangeGizmo);
    }
}
