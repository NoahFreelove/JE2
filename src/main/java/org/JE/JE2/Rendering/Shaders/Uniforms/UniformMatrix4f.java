package org.JE.JE2.Rendering.Shaders.Uniforms;

import org.JE.JE2.Rendering.Shaders.ShaderProgram;
import org.joml.Matrix4f;
import org.lwjgl.BufferUtils;

public class UniformMatrix4f extends ShaderUniform{
    private Matrix4f value;
    public UniformMatrix4f(String name, Matrix4f value) {
        super(name);
        this.value = value;
    }

    @Override
    public void set(ShaderProgram program){
        program.setUniformMatrix4f(name,value.get(BufferUtils.createFloatBuffer(16)));
    }

    public void setValue(Matrix4f value) {
        this.value = value;
    }
}
