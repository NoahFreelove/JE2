package org.JE.JE2.Rendering.Shaders.Uniforms;

import org.JE.JE2.Rendering.Shaders.ShaderProgram;

public class UniformInt64 extends ShaderUniform{
    private long value;
    public UniformInt64(String name, long value) {
        super(name);
        this.value = value;
    }

    @Override
    public void set(ShaderProgram program){
        program.setUniformI64(name,value);
    }

    public void setValue(long value) {
        this.value = value;
    }

    public long getValue() {
        return value;
    }
}