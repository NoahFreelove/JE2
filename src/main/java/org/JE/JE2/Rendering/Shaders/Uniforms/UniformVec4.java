package org.JE.JE2.Rendering.Shaders.Uniforms;

import org.JE.JE2.Rendering.Shaders.ShaderProgram;
import org.joml.Vector3f;
import org.joml.Vector4f;

public class UniformVec4 extends ShaderUniform{
    private Vector4f value;
    public UniformVec4(String name, Vector4f value) {
        super(name);
        this.value = value;
    }

    @Override
    public void set(ShaderProgram program){
        program.setUniform4f(name,value);
    }

    public void setValue(Vector4f value) {
        this.value = value;
    }
}
