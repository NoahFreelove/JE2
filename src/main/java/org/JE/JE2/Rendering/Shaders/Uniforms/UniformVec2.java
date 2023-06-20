package org.JE.JE2.Rendering.Shaders.Uniforms;

import org.JE.JE2.Rendering.Shaders.ShaderProgram;
import org.joml.Vector2f;

public class UniformVec2 extends ShaderUniform{
    private Vector2f value;
    public UniformVec2(String name, Vector2f value) {
        super(name);
        this.value = value;
    }

    @Override
    public void set(ShaderProgram program){
        program.setUniform2f(name,value);
    }

    public void setValue(Vector2f value) {
        this.value = value;
    }
}
