package JE.Objects.Lights;

import JE.Annotations.GLThread;
import JE.Objects.Base.GameObject;
import JE.Rendering.Shaders.ShaderProgram;
import org.joml.Vector3f;
import org.joml.Vector4f;

public abstract class Light extends GameObject {
    public Vector4f color;
    public float intensity;
    public int type;

    public Light(Vector4f color, float intensity, int type) {
        this.color = color;
        this.intensity = intensity;
        this.type = type;
    }

    @GLThread
    public final void setLighting(ShaderProgram shaderProgram, int index){
        shaderProgram.setUniform3f("lights[" + index + "].position", new Vector3f(getTransform().position, getTransform().zPos));
        shaderProgram.setUniform1i("lights[" + index + "].type", type);
        shaderProgram.setUniform4f("lights[" + index + "].color", color);
        shaderProgram.setUniform1f("lights[" + index + "].intensity", intensity);
        setLightSpecific(shaderProgram, index);
    }

    @GLThread
    protected abstract void setLightSpecific(ShaderProgram shaderProgram, int index);
}
