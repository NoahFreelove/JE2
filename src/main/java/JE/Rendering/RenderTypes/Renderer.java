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
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.BufferUtils;

import java.util.ArrayList;

import static org.lwjgl.opengl.GL20.*;

public class Renderer extends Component {
    protected VAO vao = new VAO();
    public ArrayList<ShaderLayout> layouts = new ArrayList<>();
    public Vector4f baseColor = new Vector4f(1, 1, 1, 1);

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
        vao.shaderProgram.setUniform3f("world_position", new Vector3f(t.position, t.zPos));
        vao.shaderProgram.setUniform1f("lightCount", Manager.getActiveScene().world.lights.size());
        vao.shaderProgram.setUniform4f("baseColor", baseColor);

        for (int i = 0; i <Manager.getActiveScene().world.lights.size(); i++) {
            Transform lightTransform = Manager.getActiveScene().world.lights.get(i).getTransform();
            vao.shaderProgram.setUniform3f("lights[" + i + "].position", new Vector3f(lightTransform.position, lightTransform.zPos));
            // Make light very bright

            vao.shaderProgram.setUniform1f("lights[" + i + "].constant", 1f);
            vao.shaderProgram.setUniform1f("lights[" + i + "].linear", 1f);
            vao.shaderProgram.setUniform1f("lights[" + i + "].quadratic", 1f);
            vao.shaderProgram.setUniform3f("lights[" + i + "].ambient", new Vector3f(1,1,1));
            vao.shaderProgram.setUniform3f("lights[" + i + "].diffuse", new Vector3f(0.5f, 0.5f, 0.5f));
            vao.shaderProgram.setUniform3f("lights[" + i + "].specular", new Vector3f(1f, 1f, 1f));
            vao.shaderProgram.setUniform1f("lights[" + i + "].intensity", 5);


            /*vao.shaderProgram.setUniform4f("lights[" + i + "].color", Manager.getActiveScene().world.lights.get(i).color);
            vao.shaderProgram.setUniform2f("lights[" + i + "].intensity", Manager.getActiveScene().world.lights.get(i).size);*/
        }

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
