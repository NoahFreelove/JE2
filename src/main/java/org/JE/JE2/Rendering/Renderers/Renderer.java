package org.JE.JE2.Rendering.Renderers;

import org.JE.JE2.Annotations.ActPublic;
import org.JE.JE2.Annotations.EditorEnum;
import org.JE.JE2.Annotations.GLThread;
import org.JE.JE2.Manager;
import org.JE.JE2.Objects.Scripts.Script;
import org.JE.JE2.Objects.Scripts.Transform;
import org.JE.JE2.Objects.Lights.Light;
import org.JE.JE2.Rendering.Camera;
import org.JE.JE2.Rendering.Material;
import org.JE.JE2.Rendering.Shaders.ShaderProgram;
import org.JE.JE2.Rendering.Renderers.VertexBuffers.VAO;
import org.JE.JE2.Rendering.Renderers.VertexBuffers.VAO2f;
import org.joml.Vector2f;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;


import static org.lwjgl.opengl.GL20.*;

public class Renderer extends Script {
    protected RenderSegment[] renderSegments = new RenderSegment[0];
    public Material material = new Material();
    protected ShaderProgram shaderProgram = ShaderProgram.invalidShader();
    public boolean debug = false;

    public Renderer(){
        VAO defaultVAO = new VAO2f(new Vector2f[]{
                new Vector2f(0,0),
                new Vector2f(1,0),
                new Vector2f(1,1),
                new Vector2f(0,1)
        });
        renderSegments = new RenderSegment[1];
        renderSegments[0] = new RenderSegment(defaultVAO, new Transform(), GL_TRIANGLE_FAN);
        shaderProgram = ShaderProgram.defaultShader();
    }
    public Renderer(VAO vao, ShaderProgram sp){
        this.shaderProgram = sp;
        renderSegments = new RenderSegment[1];
        renderSegments[0] = new RenderSegment(vao, new Transform(), GL_TRIANGLE_FAN);
    }
    public Renderer (RenderSegment seg, ShaderProgram sp){
        this.shaderProgram = sp;
        renderSegments = new RenderSegment[1];
        renderSegments[0] = seg;
    }

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
    public void requestRender(Transform t, Camera camera){
        for (RenderSegment rs : renderSegments) {
            if(!rs.isActive())
                continue;
            Render(rs,t,camera);
        }
    }

    Transform adjustedTransform = new Transform();
    @GLThread
    protected void Render(RenderSegment seg, Transform t, Camera camera){

        Transform segTransform = seg.getRelativeTransform();

        if(debug)
            System.out.println("passed attached object check");

        adjustedTransform.set(t);
        adjustedTransform.relativeAdd(segTransform);

        if(seg.usesRenderDistance()){
            if(!camera.withinRenderDistance(adjustedTransform.position(),adjustedTransform.scale()))
                return;
        }
        if(debug)
            System.out.println("passed render distance check");

        glViewport((int) camera.viewportSize.x, (int) camera.viewportSize.y, (int) camera.viewportSize.z, (int) camera.viewportSize.w);
        PreRender();

        if(!shaderProgram.use())
            return;

        if(debug)
            System.out.println("passed second shader check");

        setProjections(camera, shaderProgram, adjustedTransform, seg.scale());
        setPositions(shaderProgram, adjustedTransform);
        setMaterial(shaderProgram);

        shaderProgram.setUniform1i("use_texture", (shaderProgram.supportsTextures? 1 : 0));

        if(shaderProgram.supportsLighting)
        {
            setLighting(seg.getLightLayer());
        }

        glPolygonMode(GL_FRONT_AND_BACK, (seg.isWireframe() ? GL_LINE : GL_FILL));
        VAO vao = seg.getVao();
        if(vao.Enable(0))
        {
            if(debug)
                System.out.println("passed vao call");
        }
        else{
            return;
        }
        int error = GL11.glGetError();

        glDrawArrays(drawMode, 0, vao.getData().length + seg.getAdditionalBufferSize());
        if (error != GL11.GL_NO_ERROR) {
            System.out.println("OpenGL Error: " + error);
        }
        if(debug)
            System.out.println("passed vao disable ");
        vao.Disable();

        if(debug)
            System.out.println("passed draw call");
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

    private void setProjections(Camera camera, ShaderProgram shader, Transform t, boolean scale) {
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

    @Override
    public void destroy() {
        shaderProgram.destroy();
    }

    public RenderSegment[] getRenderSegments() {
        return renderSegments;
    }

    public Material getMaterial() {
        return material;
    }
}
