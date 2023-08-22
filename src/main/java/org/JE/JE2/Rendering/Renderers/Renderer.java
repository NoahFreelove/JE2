package org.JE.JE2.Rendering.Renderers;

import org.JE.JE2.Annotations.ActPublic;
import org.JE.JE2.Annotations.EditorEnum;
import org.JE.JE2.Annotations.GLThread;
import org.JE.JE2.Manager;
import org.JE.JE2.Objects.Scripts.Script;
import org.JE.JE2.Objects.Scripts.Serialize.Load;
import org.JE.JE2.Objects.Scripts.Serialize.Save;
import org.JE.JE2.Objects.Scripts.Transform;
import org.JE.JE2.Objects.Lights.Light;
import org.JE.JE2.Rendering.Camera;
import org.JE.JE2.Rendering.Material;
import org.JE.JE2.Rendering.Shaders.ShaderProgram;
import org.JE.JE2.Rendering.Renderers.VertexBuffers.VAO2f;
import org.JE.JE2.Utility.Time;
import org.joml.Vector2f;
import org.lwjgl.opengl.GL11;


import java.util.ArrayList;
import java.util.HashMap;

import static org.lwjgl.opengl.GL20.*;

public class Renderer extends Script implements Save, Load {
    protected RenderSegment[] renderSegments = new RenderSegment[0];
    public Material material = new Material();
    protected ShaderProgram shaderProgram = ShaderProgram.invalidShader();
    public boolean debug = false;

    public Renderer(){
        VAO2f defaultVAO = new VAO2f(new Vector2f[]{
                new Vector2f(0,0),
                new Vector2f(1,0),
                new Vector2f(1,1),
                new Vector2f(0,1)
        });
        renderSegments = new RenderSegment[1];
        renderSegments[0] = new RenderSegment(defaultVAO, new Transform(), GL_TRIANGLE_FAN);
        shaderProgram = ShaderProgram.defaultShader();
    }
    public Renderer(VAO2f vao, ShaderProgram sp){
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
        Transform animTransform = seg.getAnimationTransform();

        if(debug)
            System.out.println("passed attached object check");

        adjustedTransform.set(t);
        adjustedTransform.quickRelativeAdd(segTransform);
        adjustedTransform.quickRelativeAdd(animTransform);

        if(seg.usesRenderDistance()){
            if(!camera.withinRenderDistance(adjustedTransform.position(),adjustedTransform.scale()))
                return;
        }
        if(!shaderProgram.use())
            return;

        glViewport((int) camera.viewportSize.x, (int) camera.viewportSize.y, (int) camera.viewportSize.z, (int) camera.viewportSize.w);
        PreRender();

        setProjections(camera, shaderProgram, adjustedTransform, seg.scale());
        setPositions(shaderProgram, adjustedTransform);
        setMaterial(shaderProgram);

        shaderProgram.use_texture.setValue((shaderProgram.supportsTextures? 1 : 0));
        shaderProgram.use_lighting.setValue((shaderProgram.supportsLighting? 1 : 0));
        shaderProgram.delta_time.setValue(Time.deltaTime());
        shaderProgram.JE_Time.setValue(Time.systemTime());
        shaderProgram.render_size.setValue(t.scale());

        if(shaderProgram.supportsLighting)
        {
            setLighting(seg.getLightLayer());
        }


        shaderProgram.useUniforms();

        glPolygonMode(GL_FRONT_AND_BACK, (seg.isWireframe() ? GL_LINE : GL_FILL));
        VAO2f vao = seg.getVao();
        if(vao.Enable(0))
        {
            if(debug)
                System.out.println("passed vao call");
        }
        else{
            return;
        }
        int error = GL11.glGetError();

        glDrawArrays(drawMode, 0, vao.getVertices().length*vao.getDataSize() + seg.getAdditionalBufferSize());
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
        shader.material_ambient.setValue(material.getAmbient());
        shader.material_diffuse.setValue(material.getDiffuse());
        shader.material_specular.setValue(material.getSpecular());
        shader.material_shininess.setValue(material.getShininess());
        shader.material_base_color.setValue(material.getBaseColor().getVec4());
    }

    private void setPositions(ShaderProgram shader, Transform t) {
        shader.world_position.setValue(t.position3D());
        shader.world_scale.setValue(t.scale());
        shader.world_rotation.setValue(t.rotation());
    }

    private void setProjections(Camera camera, ShaderProgram shader, Transform t, boolean scale) {
        shader.MVP.setValue(camera.MVPOrtho(t,scale));
        shader.model.setValue(camera.getModel(t,scale));
        shader.view.setValue(camera.getViewMatrix());
        shader.projection.setValue(camera.getOrtho());
    }

    @GLThread
    private void setLighting(int selectedLayer) {
        shaderProgram.light_count.setValue(Manager.activeScene().world.activeLights());
        shaderProgram.layer.setValue(selectedLayer);

        int i = 0;
        for (Light light : Manager.activeScene().world.lights) {
            if(light.notActive()){
                continue;
            }
            light.setLighting(shaderProgram, i);
            i++;
        }
    }

    public void setDrawMode(int mode){
        drawMode = mode;
    }

    @Override
    public void destroy() {
        shaderProgram.destroy();
        for (RenderSegment renderSegment : renderSegments) {
            renderSegment.destroy();
        }
    }

    public RenderSegment[] getRenderSegments() {
        return renderSegments;
    }

    public Material getMaterial() {
        return material;
    }

    public static void getHighestBuffer(){
        Manager.queueGLFunction(() -> {
            System.out.println("Highest Buffer " + glGenBuffers());
        });
    }


    @Override
    public void load(HashMap<String, String> data) {
        // Need to load the Texture Segments, Its VAO points, its relative transform, texture, and normal texture, and tile factor and draw mode
        // In Future material and shader program

        ArrayList<RenderSegment> segs = new ArrayList<>();
        int i = 0;
        while (data.containsKey("RenderSegment" + i + ":VAO")) {
            VAO2f vao = new VAO2f();
            vao.setVertices(VAO2f.stringToVerts(data.get("RenderSegment" + i + ":VAO")));
            Transform relativeTransform = new Transform();
            relativeTransform.simpleDeserialize(data.get("RenderSegment" + i + ":RELATIVE_TRANSFORM"));
            Transform animationTransform = new Transform();
            animationTransform.simpleDeserialize(data.get("RenderSegment" + i + ":ANIMATION_TRANSFORM"));

            int drawMode = Integer.parseInt(data.get("TextureSegment" + i + ":DRAW_MODE"));
            RenderSegment ts = new RenderSegment(vao, relativeTransform, drawMode);
            ts.setAnimationTransform(animationTransform);
            segs.add(ts);
            i++;
        }
        renderSegments = segs.toArray(new RenderSegment[0]);

    }

    public void addRenderSegment(RenderSegment rs) {
        RenderSegment[] temp = new RenderSegment[renderSegments.length + 1];
        for (int i = 0; i < renderSegments.length; i++) {
            temp[i] = renderSegments[i];
        }
        temp[temp.length - 1] = rs;
        renderSegments = temp;
    }

    @Override
    public HashMap<String, String> save() {
        HashMap<String, String> data = new HashMap<>();
        // Need to save the Texture Segments, Its VAO points, its relative transform, texture, and normal texture, and tile factor and draw mode
        // In Future material and shader program
        int i = 0;
        for (RenderSegment ts : renderSegments) {
            data.put("RenderSegment" + i + ":VAO", ts.getVao().vertsToString());
            data.put("RenderSegment" + i + ":RELATIVE_TRANSFORM", ts.getRelativeTransform().simpleSerialize());
            data.put("RenderSegment" + i + ":ANIMATION_TRANSFORM", ts.getAnimationTransform().simpleSerialize());
            data.put("RenderSegment" + i + ":DRAW_MODE", ts.getDrawMode() + "");
            i++;
        }

        return data;
    }
}
