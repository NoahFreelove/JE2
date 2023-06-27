package org.JE.JE2.Rendering.Shaders.Uniforms;

import org.JE.JE2.Rendering.Shaders.ShaderProgram;

public class UniformInt extends ShaderUniform{
    private int value;
    public UniformInt(String name, int value) {
        super(name);
        this.value = value;
    }

    @Override
    public void set(ShaderProgram program){
        program.setUniform1i(name,value);
    }

    public void setValue(int value) {
        this.value = value;
    }
}
