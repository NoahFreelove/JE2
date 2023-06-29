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
import org.JE.JE2.Rendering.Shaders.ShaderProgram;
import org.JE.JE2.Rendering.VertexBuffers.VAO;
import org.JE.JE2.Rendering.VertexBuffers.VAO2f;
import org.joml.Vector2f;
import org.joml.Vector4f;
import org.lwjgl.BufferUtils;


import static org.lwjgl.opengl.GL20.*;

public class Renderer extends Script {
    @HideFromInspector
    protected VAO vao = new VAO();
    public Material material = new Material();
    public boolean wireframe = false;
    protected ShaderProgram shaderProgram = ShaderProgram.invalidShader();

    public Renderer(){
        vao = new VAO2f(new Vector2f[]{
                new Vector2f(0,0),
                new Vector2f(1,0),
                new Vector2f(1,1),
                new Vector2f(0,1)
        });
        shaderProgram = ShaderProgram.defaultShader();
    }
    public Renderer(VAO vao, ShaderProgram sp){
        this.shaderProgram = sp;
        this.vao = vao;
    }

    @ActPublic
    protected boolean scale = true;

    @ActPublic
    @EditorEnum(values = {"Points", "Lines", "Line Loop", "Line Strip", "Triangles", "Triangle Strip", "Triangle Fan", "Quads", "Quad Strip", "Polygons"})
    protected int drawMode = GL_TRIANGLE_FAN;

    @ActPublic
    @EditorEnum(values = {"Base Color", "Textured", "Textured with Lighting"})
    public int defaultShaderIndex = 1;

    public void setShaderProgram(ShaderProgram shader){

        this.shaderProgram = shader;
    }

    public ShaderProgram getShaderProgram() {
        return shaderProgram;
    }

    protected void PreRender(){}

    @GLThread
    public void Render(Transform t, int additionalBufferSize, int layer, Camera camera){
        if(!camera.withinRenderDistance(t.position(),t.scale()))
            return;

        glViewport((int) camera.viewportSize.x, (int) camera.viewportSize.y, (int) camera.viewportSize.z, (int) camera.viewportSize.w);
        PreRender();

        if(!shaderProgram.use())
            return;

        setProjections(camera, shaderProgram, t);
        setPositions(shaderProgram, t);
        setMaterial(shaderProgram);

        shaderProgram.setUniform1i("use_texture", (shaderProgram.supportsTextures? 1 : 0));

        if(shaderProgram.supportsLighting)
        {
            setLighting(layer);
        }

        glPolygonMode(GL_FRONT_AND_BACK, (wireframe ? GL_LINE : GL_FILL));

        vao.Enable(0);
        glDrawArrays(drawMode, 0, vao.getData().length + additionalBufferSize);
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
        shaderProgram.setUniform1i("light_count", Manager.activeScene().world.lights.size());
        shaderProgram.setUniform1i("layer", selectedLayer);

        int i = 0;
        for (Light light : Manager.activeScene().world.lights) {
            light.setLighting(shaderProgram, i);
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
        shaderProgram.destroy();
    }

    @Override
    public void load(){
        super.load();
        if(vao !=null)
            vao.load();
    }
}
