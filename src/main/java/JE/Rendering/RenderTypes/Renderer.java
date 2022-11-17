package JE.Rendering.RenderTypes;

import JE.Annotations.GLThread;
import JE.Manager;
import JE.Objects.Components.Component;
import JE.Objects.Components.ComponentRestrictions;
import JE.Objects.Components.Transform;
import JE.Rendering.Camera;
import JE.Rendering.Shaders.ShaderLayout;
import JE.Rendering.Shaders.ShaderProgram;
import JE.Rendering.VertexBuffers.VAO;
import JE.Rendering.VertexBuffers.VAO2f;
import org.joml.Vector2f;
import org.lwjgl.BufferUtils;

import java.util.ArrayList;

import static org.lwjgl.opengl.GL20.*;

public class Renderer extends Component {
    protected VAO vao = new VAO();
    public ArrayList<ShaderLayout> layouts = new ArrayList<>();

    protected int drawMode = GL_POLYGON;

    public Renderer(){
        restrictions = new ComponentRestrictions(false, true, true);
    }

    public Renderer(VAO2f vao){
        this.vao = vao;
        restrictions = new ComponentRestrictions(false, true, true);
    }
    public Renderer(VAO vao){
        this.vao = vao;
        restrictions = new ComponentRestrictions(false, true, true);
    }

    public void SetShader(ShaderProgram shader){
        vao.setShaderProgram(shader);
    }

    protected void PreRender(){}

    @GLThread
    public void Render(Transform t)
    {
        Render(t,0, Manager.getActiveScene().activeCamera);
    }

    @GLThread
    public void Render(Transform t, int additionalBufferSize) {
        Render(t,additionalBufferSize, Manager.getActiveScene().activeCamera);
    }

    public void enableLayouts(){
        layouts.forEach(ShaderLayout::Enable);
    }
    public void disableLayouts(){
        layouts.forEach(ShaderLayout::Disable);
    }

    @GLThread
    public void Render(Transform t, int additionalBufferSize, Camera camera) {
        PreRender();

        vao.shaderProgram.setUniformMatrix4f("MVP", camera.getMVP(t).get(BufferUtils.createFloatBuffer(16)));
        vao.shaderProgram.setUniform1f("zPos", t.zPos);

        vao.Enable(0);
        enableLayouts();
        glDrawArrays(drawMode, 0, vao.getData().length + additionalBufferSize);
        disableLayouts();
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
