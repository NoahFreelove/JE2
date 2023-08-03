package org.JE.JE2.Rendering.Renderers;

import org.JE.JE2.Manager;
import org.JE.JE2.Objects.GameObject;
import org.JE.JE2.Rendering.Framebuffer;
import org.JE.JE2.Rendering.Shaders.ShaderProgram;
import org.JE.JE2.Rendering.Renderers.VertexBuffers.VAO2f;
import org.joml.Vector2f;
import org.joml.Vector2i;

public class FramebufferRenderer {

    private Framebuffer fb;
    private ShaderProgram sp;
    private final SpriteRenderer postProcessor = new SpriteRenderer();
    public FramebufferRenderer(Framebuffer buffer, ShaderProgram shader){
        VAO2f screenQuadVAO = new VAO2f(new Vector2f[]{
                new Vector2f(0, 0),
                new Vector2f(1, 0),
                new Vector2f(1, 1),
                new Vector2f(0, 1)
        });
        postProcessor.setSpriteVAO(screenQuadVAO);
        setShaderProgram(shader);
        setFramebuffer(buffer);
    }
    public void setFramebuffer(Framebuffer fb){
        this.fb = fb;
        postProcessor.getTexture().resource.setID(fb.getTexture());
        postProcessor.getTexture().resource.getBundle().setImageSize(new Vector2i(fb.getWidth(), fb.getHeight()));
        postProcessor.getTexture().valid = true;
    }
    public void setShaderProgram(ShaderProgram sp){
        this.sp = sp;
        postProcessor.setShaderProgram(sp);
    }

    public Framebuffer getFb() {
        return fb;
    }

    public ShaderProgram getSp() {
        return sp;
    }

    public void Render(){
        postProcessor.requestRender(Manager.getMainCamera());
    }
}
