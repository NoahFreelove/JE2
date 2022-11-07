package JE.Rendering;

import JE.Annotations.GLThread;
import JE.Manager;
import JE.Objects.Components.Component;
import JE.Objects.Components.ComponentRestrictions;
import JE.Objects.Components.Transform;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.lwjgl.BufferUtils;

import static org.lwjgl.opengl.GL20.*;

public class Renderer extends Component {
    protected VAO2f vao = new VAO2f();
    protected int drawMode = GL_TRIANGLES;

    public Renderer(){
        restrictions = new ComponentRestrictions(false, true, true);
    }

    public Renderer(VAO2f vao){
        this.vao = vao;
        restrictions = new ComponentRestrictions(false, true, true);
    }

    public void SetVertices(Vector2f[] vertices){
        vao.setVertices(vertices);
    }
    public void SetShader(ShaderProgram shader){
        vao.setShaderProgram(shader);
    }

    @GLThread
    public void Render(Transform t)
    {
        Render(t,0);
    }
    public void Render(Transform t, int additionalBufferSize) {
        Matrix4f mvp = Manager.getActiveScene().activeCamera.getMVP(t);
        vao.shaderProgram.setUniformMatrix4f("MVP", mvp.get(BufferUtils.createFloatBuffer(16)));

        vao.shaderProgram.setUniform1f("zPos", t.zPos);

        vao.Enable(0);
        glDrawArrays(drawMode, 0, vao.vertices.length + additionalBufferSize);
        vao.Disable();
    }

    @Override
    public void update() {

    }

    @Override
    public void start() {

    }

    @Override
    public void awake() {

    }

    public void setDrawMode(int mode){
        drawMode = mode;
    }
}
