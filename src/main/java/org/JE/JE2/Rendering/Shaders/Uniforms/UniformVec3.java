package org.JE.JE2.Rendering.Shaders.Uniforms;

import org.JE.JE2.Rendering.Shaders.ShaderProgram;
import org.joml.Vector2f;
import org.joml.Vector3f;

public class UniformVec3 extends ShaderUniform{
    private Vector3f value;
    public UniformVec3(String name, Vector3f value) {
        super(name);
        this.value = value;
    }

    @Override
    public void set(ShaderProgram program){
        program.setUniform3f(name,value);
    }

    public void setValue(Vector3f value) {
        this.value = value;
    }
}
