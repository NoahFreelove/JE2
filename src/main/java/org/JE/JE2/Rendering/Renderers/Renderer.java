package org.JE.JE2.Rendering.Renderers;

import org.JE.JE2.Annotations.ActPublic;
import org.JE.JE2.Annotations.EditorEnum;
import org.JE.JE2.Annotations.GLThread;
import org.JE.JE2.Annotations.HideFromInspector;
import org.JE.JE2.Manager;
import org.JE.JE2.Objects.GameObject;
import org.JE.JE2.Objects.Scripts.Script;
import org.JE.JE2.Objects.Scripts.Transform;
import org.JE.JE2.Objects.Lights.Light;
import org.JE.JE2.Rendering.Camera;
import org.JE.JE2.Rendering.Shaders.ShaderLayout;
import org.JE.JE2.Rendering.Shaders.ShaderProgram;
import org.JE.JE2.Rendering.VertexBuffers.VAO;
import org.JE.JE2.UI.UIElements.Style.Color;
import org.lwjgl.BufferUtils;

import java.util.ArrayList;

import static org.lwjgl.opengl.GL20.*;

public class Renderer extends Script {
    @HideFromInspector
    protected VAO vao = new VAO();
    public ArrayList<ShaderLayout> layouts = new ArrayList<>();
    public Color baseColor = Color.WHITE;

    @ActPublic
    protected boolean scale = true;

    @ActPublic
    @EditorEnum(values = {"Points", "Lines", "Line Loop", "Line Strip", "Triangles", "Triangle Strip", "Triangle Fan", "Quads", "Quad Strip", "Polygons"})
    protected int drawMode = GL_TRIANGLE_FAN;

    @ActPublic
    @EditorEnum(values = {"Base Color", "Textured", "Textured with Lighting"})
    public int defaultShaderIndex = 1;

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
        if(!camera.withinRenderDistance(gameObject.getTransform().position(), gameObject.getTransform().scale()))
            return;

        glViewport((int) camera.viewportSize.x, (int) camera.viewportSize.y, (int) camera.viewportSize.z, (int) camera.viewportSize.w);
        PreRender();
        ShaderProgram shader = vao.getShaderProgram();
        if(!shader.use())
            return;
        Transform t = gameObject.getTransform();

        shader.setUniformMatrix4f("MVP", camera.MVPOrtho(t,scale).get(BufferUtils.createFloatBuffer(16)));
        shader.setUniformMatrix4f("model", camera.getModel(t,scale).get(BufferUtils.createFloatBuffer(16)));
        shader.setUniformMatrix4f("view", camera.getViewMatrix().get(BufferUtils.createFloatBuffer(16)));
        shader.setUniformMatrix4f("projection", camera.getOrtho().get(BufferUtils.createFloatBuffer(16)));
        shader.setUniform3f("world_position", t.position3D());
        shader.setUniform2f("world_scale", t.scale());
        shader.setUniform3f("world_rotation", t.rotation());
        shader.setUniform4f("base_color", baseColor.getVec4());

        if (shader.supportsTextures)
        {
            vao.getShaderProgram().setUniform1i("use_texture", (shader.supportsTextures? 1 : 0));
        }

        if(shader.supportsLighting)
        {
            setLighting(gameObject.getLayer());
        }

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
        super.load();
        if(vao !=null)
            vao.load();
    }
}
