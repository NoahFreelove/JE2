package JE.Rendering.Renderers;

import JE.Annotations.GLThread;
import JE.Manager;
import JE.Objects.GameObject;
import JE.Objects.Scripts.Base.Script;
import JE.Objects.Scripts.Common.Transform;
import JE.Objects.Lights.Light;
import JE.Rendering.Camera;
import JE.Rendering.Shaders.ShaderLayout;
import JE.Rendering.Shaders.ShaderProgram;
import JE.Rendering.VertexBuffers.VAO;
import JE.UI.UIElements.Style.Color;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.lwjgl.BufferUtils;

import java.util.ArrayList;

import static org.lwjgl.opengl.GL20.*;

public class Renderer extends Script {
    protected VAO vao = new VAO();
    public ArrayList<ShaderLayout> layouts = new ArrayList<>();
    public Color baseColor = Color.WHITE;
    protected boolean scale = true;
    protected int drawMode = GL_TRIANGLE_FAN;

    public void SetShader(ShaderProgram shader){
        vao.setShaderProgram(shader);
    }

    protected void PreRender(){}

    @GLThread
    public void Render(GameObject t)
    {
        Render(t,0, Manager.getMainCamera());
    }

    @GLThread
    public void Render(GameObject t, int additionalBufferSize) {
        Render(t,additionalBufferSize, Manager.getMainCamera());
    }

    public void enableLayouts(){
        layouts.forEach(ShaderLayout::Enable);
    }
    public void disableLayouts(){
        layouts.forEach(ShaderLayout::Disable);
    }

    @GLThread
    public void Render(GameObject gameObject, int additionalBufferSize, Camera camera) {

        glViewport(camera.viewportSize.x, camera.viewportSize.y, camera.viewportSize.z, camera.viewportSize.w);
        PreRender();
        ShaderProgram shader = vao.getShaderProgram();
        if(!shader.use())
            return;
        Transform t = gameObject.getTransform();

        shader.setUniformMatrix4f("MVP", camera.MVPOrtho(t,scale).get(BufferUtils.createFloatBuffer(16)));
        shader.setUniformMatrix4f("model", camera.getModel(t,scale).get(BufferUtils.createFloatBuffer(16)));
        shader.setUniformMatrix4f("view", camera.getViewMatrix().get(BufferUtils.createFloatBuffer(16)));
        shader.setUniformMatrix4f("projection", camera.getOrtho().get(BufferUtils.createFloatBuffer(16)));
        shader.setUniform3f("world_position", new Vector3f(t.position(), t.zPos()));
        shader.setUniform2f("world_scale", new Vector2f(t.scale()));
        shader.setUniform3f("world_rotation", new Vector3f(t.rotation()));
        shader.setUniform4f("base_color", baseColor.getVec4());

        if (shader.supportsTextures)
        {
            vao.getShaderProgram().setUniform1i("use_texture", (shader.supportsTextures? 1 : 0));
        }

        if(shader.supportsLighting)
            setLighting(gameObject.getLayer());

        vao.Enable(0);
        enableLayouts();
        glDrawArrays(drawMode, 0, vao.getData().length + additionalBufferSize);
        disableLayouts();
        vao.Disable();
    }

    @GLThread
    private void setLighting(int selectedLayer) {
        vao.getShaderProgram().setUniform1i("light_count", Manager.activeScene().world.lights.size());

        for (int i = 0; i <Manager.activeScene().world.lights.size(); i++) {
            Light light = Manager.activeScene().world.lights.get(i);
            boolean found = false;
            for (int layer : light.affectedLayers) {
                if (layer == selectedLayer) {
                    light.setLighting(vao.getShaderProgram(), i);
                    found = true;
                    break;
                }
            }
            if(!found)
                light.hideLighting(vao.getShaderProgram(), i);
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

    @Override
    public void destroy() {
        getVAO().getShaderProgram().destroy();
    }

    @Override
    public void load(){
        if(vao !=null)
            vao.load();
    }
}
