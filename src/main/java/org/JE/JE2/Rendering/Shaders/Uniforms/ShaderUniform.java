package org.JE.JE2.Rendering.Shaders.Uniforms;

import org.JE.JE2.Annotations.GLThread;
import org.JE.JE2.Rendering.Shaders.ShaderProgram;

public class ShaderUniform {
    public static final ShaderUniform EMPTY = new ShaderUniform("");

    protected final String name;

    public ShaderUniform(String name){
        this.name = name;
    }
    @GLThread
    public void set(ShaderProgram program){}

}
