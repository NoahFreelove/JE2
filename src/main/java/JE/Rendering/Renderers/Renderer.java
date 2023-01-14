package JE.Rendering.Renderers;

import JE.Annotations.GLThread;
import JE.Manager;
import JE.Objects.Components.Base.Component;
import JE.Objects.Components.Base.ComponentRestrictions;
import JE.Objects.Components.Common.Transform;
import JE.Objects.Lights.PointLight;
import JE.Rendering.Camera;
import JE.Rendering.Shaders.ShaderLayout;
import JE.Rendering.Shaders.ShaderProgram;
import JE.Rendering.VertexBuffers.VAO;
import org.joml.Vector2f;
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


    public void SetShader(ShaderProgram shader){
        vao.setShaderProgram(shader);
    }

    protected void PreRender(){}

    @GLThread
    public void Render(Transform t)
    {
        Render(t,0, Manager.getCamera());
    }

    @GLThread
    public void Render(Transform t, int additionalBufferSize) {
        Render(t,additionalBufferSize, Manager.getCamera());
    }

    public void enableLayouts(){
        layouts.forEach(ShaderLayout::Enable);
    }
    public void disableLayouts(){
        layouts.forEach(ShaderLayout::Disable);
    }

    @GLThread
    public void Render(Transform t, int additionalBufferSize, Camera camera) {

        glViewport(camera.viewportSize.x, camera.viewportSize.y, camera.viewportSize.z, camera.viewportSize.w);
        PreRender();
        vao.shaderProgram.use();

        vao.shaderProgram.setUniformMatrix4f("MVP", camera.MVPOrtho(t).get(BufferUtils.createFloatBuffer(16)));
        vao.shaderProgram.setUniformMatrix4f("model", camera.getModel(t).get(BufferUtils.createFloatBuffer(16)));
        vao.shaderProgram.setUniformMatrix4f("view", camera.getViewMatrix().get(BufferUtils.createFloatBuffer(16)));
        vao.shaderProgram.setUniformMatrix4f("projection", camera.getOrtho().get(BufferUtils.createFloatBuffer(16)));
        vao.shaderProgram.setUniform3f("world_position", new Vector3f(t.position, t.zPos));
        vao.shaderProgram.setUniform2f("world_scale", new Vector2f(t.scale));
        vao.shaderProgram.setUniform3f("world_rotation", new Vector3f(t.rotation));

        vao.shaderProgram.setUniform1i("light_count", Manager.activeScene().world.lights.size());
        vao.shaderProgram.setUniform4f("base_color", baseColor);


        if(vao.shaderProgram.supportsLighting)
            setLighting();

        vao.Enable(0);
        enableLayouts();
        glDrawArrays(drawMode, 0, vao.getData().length + additionalBufferSize);
        disableLayouts();
        vao.Disable();
    }

    private void setLighting() {
        for (int i = 0; i <Manager.activeScene().world.lights.size(); i++) {
            PointLight light = Manager.activeScene().world.lights.get(i);
            Transform lightTransform = light.getTransform();
            vao.shaderProgram.setUniform3f("lights[" + i + "].position", new Vector3f(lightTransform.position, lightTransform.zPos));
            vao.shaderProgram.setUniform4f("lights[" + i + "].color", light.color);
            vao.shaderProgram.setUniform1f("lights[" + i + "].constant", light.constant);
            vao.shaderProgram.setUniform1f("lights[" + i + "].linear", light.linear);
            vao.shaderProgram.setUniform1f("lights[" + i + "].intensity", light.intensity);
            vao.shaderProgram.setUniform1f("lights[" + i + "].quadratic", light.quadratic);
            vao.shaderProgram.setUniform3f("lights[" + i + "].ambient", light.ambient);
            vao.shaderProgram.setUniform3f("lights[" + i + "].diffuse", light.diffuse);
            vao.shaderProgram.setUniform3f("lights[" + i + "].specular", light.specular);
            vao.shaderProgram.setUniform1f("lights[" + i + "].radius", light.radius);

            /*vao.shaderProgram.setUniform4f("lights[" + i + "].color", Manager.getActiveScene().world.lights.get(i).color);
            vao.shaderProgram.setUniform2f("lights[" + i + "].intensity", Manager.getActiveScene().world.lights.get(i).size);*/
        }
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

    public VAO getVAO(){return vao;}

}
