package JE.Objects.Lights;

import JE.Rendering.Shaders.ShaderProgram;
import JE.UI.UIElements.Style.Color;
import org.joml.Vector2f;
import org.joml.Vector3f;

import static org.lwjgl.opengl.GL11.*;

public class PointLight extends Light {
    public Vector3f diffuse = new Vector3f(0.8f, 0.8f, 0.8f);
    public float radius = 5;

    public PointLight(){
        super(Color.WHITE, 1, 1);
        this.type = 1;
    }

    @Override
    protected void setLightSpecific(ShaderProgram shaderProgram, int index) {
        shaderProgram.setUniform3f("lights[" + index + "].diffuse", diffuse);
        shaderProgram.setUniform1f("lights[" + index + "].radius", radius);
    }

}
