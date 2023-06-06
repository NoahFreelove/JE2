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
import org.JE.JE2.Rendering.Material;
import org.JE.JE2.Rendering.Shaders.ShaderLayout;
import org.JE.JE2.Rendering.Shaders.ShaderProgram;
import org.JE.JE2.Rendering.VertexBuffers.VAO;
import org.JE.JE2.UI.UIElements.Style.Color;
import org.joml.Vector4f;
import org.lwjgl.BufferUtils;

import java.util.ArrayList;

import static org.lwjgl.opengl.GL20.*;

public class Renderer extends Script {
    @HideFromInspector
    protected VAO vao = new VAO();
    public ArrayList<ShaderLayout> layouts = new ArrayList<>();
    public Material material = new Material();

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
        Render(gameObject.getTransform(), additionalBufferSize, camera.viewportSize, gameObject.getLayer(), camera);
    }

    @GLThread
    public void Render(Transform t, int additionalBufferSize, Vector4f viewport, int layer, Camera camera){
        if(!camera.withinRenderDistance(t.position(),t.scale()))
            return;

        glViewport((int) camera.viewportSize.x, (int) camera.viewportSize.y, (int) camera.viewportSize.z, (int) camera.viewportSize.w);
        PreRender();
        ShaderProgram shader = vao.getShaderProgram();
        if(!shader.use())
            return;

        setProjections(camera, shader, t);
        setPositions(shader, t);
        setMaterial(shader);


        if (shader.supportsTextures)
        {
            vao.getShaderProgram().setUniform1i("use_texture", (shader.supportsTextures? 1 : 0));
        }

        if(shader.supportsLighting)
        {
            setLighting(layer);
        }

        vao.Enable(0);
        enableLayouts();
        glDrawArrays(drawMode, 0, vao.getData().length + additionalBufferSize);
        disableLayouts();
        vao.Disable();
    }

    private void setMaterial(ShaderProgram shader) {
        shader.setUniform3f("material.ambient", material.getAmbient());
        shader.setUniform3f("material.diffuse", material.getDiffuse());
        shader.setUniform3f("material.specular", material.getSpecular());
        shader.setUniform1f("material.shininess", material.getShininess());
        shader.setUniform4f("material.base_color", material.getBaseColor().getVec4());
    }

    private void setPositions(ShaderProgram shader, Transform t) {
        shader.setUniform3f("world_position", t.position3D());
        shader.setUniform2f("world_scale", t.scale());
        shader.setUniform3f("world_rotation", t.rotation());
    }

    private void setProjections(Camera camera, ShaderProgram shader, Transform t) {
        shader.setUniformMatrix4f("MVP", camera.MVPOrtho(t,scale).get(BufferUtils.createFloatBuffer(16)));
        shader.setUniformMatrix4f("model", camera.getModel(t,scale).get(BufferUtils.createFloatBuffer(16)));
        shader.setUniformMatrix4f("view", camera.getViewMatrix().get(BufferUtils.createFloatBuffer(16)));
        shader.setUniformMatrix4f("projection", camera.getOrtho().get(BufferUtils.createFloatBuffer(16)));
    }

    @GLThread
    private void setLighting(int selectedLayer) {
        vao.getShaderProgram().setUniform1i("light_count", Manager.activeScene().world.lights.size());
        vao.getShaderProgram().setUniform1i("layer", selectedLayer);

        int i = 0;
        for (Light light : Manager.activeScene().world.lights) {
            light.setLighting(vao.getShaderProgram(), i);
            i++;
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
