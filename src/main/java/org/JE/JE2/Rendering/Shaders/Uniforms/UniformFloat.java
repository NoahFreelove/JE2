package org.JE.JE2.Rendering.Shaders.Uniforms;

import org.JE.JE2.Rendering.Shaders.ShaderProgram;
import org.joml.Vector3f;

public class UniformFloat extends ShaderUniform{
    private float value;
    public UniformFloat(String name, float value) {
        super(name);
        this.value = value;
    }

    @Override
    public void set(ShaderProgram program){
        program.setUniform1f(name,value);
    }

    public void setValue(float value) {
        this.value = value;
    }
}
