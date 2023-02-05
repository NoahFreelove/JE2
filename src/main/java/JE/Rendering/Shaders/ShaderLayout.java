package JE.Rendering.Shaders;

import JE.Annotations.GLThread;
import JE.Logging.Errors.ShaderLayoutError;
import JE.Logging.Logger;
import JE.Rendering.VertexBuffers.VAO;

import java.io.Serializable;

public class ShaderLayout implements Serializable {
    private VAO vao;
    private int location;

    public ShaderLayout(VAO vao, int location) {
        this.vao = vao;
        if(location > 100)
            this.location = location;
        else {
            this.location = location + 100;
            Logger.log(new ShaderLayoutError("ShaderLayout location is less than 100. These are reserved for JE2. The location has been set to " + this.location));
        }

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
