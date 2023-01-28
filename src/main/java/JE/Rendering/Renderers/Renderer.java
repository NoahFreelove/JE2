package JE.Rendering.Renderers;

import JE.Annotations.GLThread;
import JE.Manager;
import JE.Objects.Base.GameObject;
import JE.Objects.Components.Base.Component;
import JE.Objects.Components.Base.ComponentRestrictions;
import JE.Objects.Components.Common.Transform;
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

public class Renderer extends Component {
    protected VAO vao = new VAO();
    public ArrayList<ShaderLayout> layouts = new ArrayList<>();
    public Color baseColor = Color.WHITE;

    protected int drawMode = GL_POLYGON;

    public Renderer(){
        restrictions = new ComponentRestrictions(false, true, true);
    }

    public void SetShader(ShaderProgram shader){
        vao.setShaderProgram(shader);
    }

    protected void PreRender(){}

    @GLThread
    public void Render(GameObject t)
    {
        Render(t,0, Manager.getCamera());
    }

    @GLThread
    public void Render(GameObject t, int additionalBufferSize) {
        Render(t,additionalBufferSize, Manager.getCamera());
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
        if(!vao.shaderProgram.use())
            return;
        Transform t = gameObject.getTransform();

        vao.shaderProgram.setUniformMatrix4f("MVP", camera.MVPOrtho(t).get(BufferUtils.createFloatBuffer(16)));
        vao.shaderProgram.setUniformMatrix4f("model", camera.getModel(t).get(BufferUtils.createFloatBuffer(16)));
        vao.shaderProgram.setUniformMatrix4f("view", camera.getViewMatrix().get(BufferUtils.createFloatBuffer(16)));
        vao.shaderProgram.setUniformMatrix4f("projection", camera.getOrtho().get(BufferUtils.createFloatBuffer(16)));
        vao.shaderProgram.setUniform3f("world_position", new Vector3f(t.position, t.zPos));
        vao.shaderProgram.setUniform2f("world_scale", new Vector2f(t.scale));
        vao.shaderProgram.setUniform3f("world_rotation", new Vector3f(t.rotation));

        vao.shaderProgram.setUniform4f("base_color", baseColor.getVec4());

        if(vao.shaderProgram.supportsLighting)
            setLighting(gameObject.getLightLayer());

        vao.Enable(0);
        enableLayouts();
        glDrawArrays(drawMode, 0, vao.getData().length + additionalBufferSize);
        disableLayouts();
        vao.Disable();
    }

    @GLThread
    private void setLighting(int selectedLayer) {
        vao.shaderProgram.setUniform1i("light_count", Manager.activeScene().world.lights.size());
        for (int i = 0; i <Manager.activeScene().world.lights.size(); i++) {
            Light light = Manager.activeScene().world.lights.get(i);
            boolean found = false;
            for (int layer : light.affectedLayers) {
                if (layer == selectedLayer) {
                    light.setLighting(vao.shaderProgram, i);
                    found = true;
                    break;
                }
            }
            if(!found)
                light.hideLighting(vao.shaderProgram, i);
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
        getVAO().shaderProgram.destroy();
    }
}
