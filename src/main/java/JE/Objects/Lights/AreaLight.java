package JE.Objects.Lights;

import JE.Rendering.Shaders.ShaderProgram;
import org.joml.Vector2f;
import org.joml.Vector4f;

public class AreaLight extends Light {
    public boolean hasBounds = false;
    public Vector2f boundPos = new Vector2f(0,0);
    public Vector2f boundSize = new Vector2f(0,0);
    public AreaLight() {
        super(new Vector4f(1,1,1,1),1,2);
        this.type = 2;
    }

    @Override
    protected void setLightSpecific(ShaderProgram shaderProgram, int index) {
        shaderProgram.setUniform1i("lights[" + index + "].has_bounds", (hasBounds? 1:0));
        shaderProgram.setUniform2f("lights[" + index + "].bound_pos", boundPos);
        shaderProgram.setUniform2f("lights[" + index + "].bound_range", boundSize);
    }
}
