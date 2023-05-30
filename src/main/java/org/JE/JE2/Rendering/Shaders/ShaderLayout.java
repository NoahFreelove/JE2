package org.JE.JE2.Rendering.Shaders;

import org.JE.JE2.Annotations.GLThread;
import org.JE.JE2.IO.Logging.Errors.ShaderLayoutError;
import org.JE.JE2.IO.Logging.Logger;
import org.JE.JE2.Rendering.VertexBuffers.VAO;

import java.io.Serializable;

public class ShaderLayout implements Serializable {
    private VAO vao;
    private int location;

    public ShaderLayout(VAO vao, int location) {
        this.vao = vao;

    }

    public void Enable() {
        vao.Enable(location);
    }

    public void Disable() {
        vao.Disable();
    }

    @GLThread
    public void SetData(float[] data){
        vao.setDataNow(data);
    }
}
