package org.JE.JE2.Objects.Scripts.ScreenEffects.PostProcess;

import org.JE.JE2.Annotations.GLThread;
import org.JE.JE2.Manager;
import org.JE.JE2.Objects.GameObject;
import org.JE.JE2.Objects.Scripts.Script;
import org.JE.JE2.Rendering.Framebuffer;
import org.JE.JE2.Rendering.Renderers.SpriteRenderer;
import org.JE.JE2.Rendering.Shaders.ShaderProgram;
import org.JE.JE2.Rendering.Renderers.VertexBuffers.VAO2f;
import org.JE.JE2.Window.Window;
import org.joml.Vector2f;

import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30C.glGenVertexArrays;

/**
 * Will apply to entire screen when active camera is in bounds
 */
public class PostProcessingVolume extends Script {

    private ShaderProgram screenShader;
    private Vector2f bounds = new Vector2f(5,5);
    private SpriteRenderer postProcessor = new SpriteRenderer();
    public Framebuffer framebuffer = Window.getDefaultFramebuffer();

    public PostProcessingVolume(){
        super();
        screenShader = Window.getDefaultPostProcessShader();
        init();
    }

    public PostProcessingVolume(Vector2f bounds){
        super();
        this.bounds = bounds;
        screenShader = Window.getDefaultPostProcessShader();
        init();
    }

    public PostProcessingVolume(ShaderProgram program, Vector2f bounds){
        super();
        this.bounds = bounds;
        screenShader = program;
        init();
    }

    private void init(){
        postProcessor = new SpriteRenderer(screenShader);
        VAO2f screenQuadVAO = new VAO2f(new Vector2f[]{
                new Vector2f(0, 0),
                new Vector2f(1, 0),
                new Vector2f(1, 1),
                new Vector2f(0, 1)
        });
        postProcessor.getRenderSegments()[0].setVao(screenQuadVAO);
        postProcessor.getTexture().valid = true;
        postProcessor.getTextureSegments()[0].setRenderDistance(false);
    }

    @Override
    @GLThread
    public void postRender(){
        if(!inBounds()){
            return;
        }
        if(framebuffer == null)
        {
            framebuffer = Window.getDefaultFramebuffer();
            if(framebuffer == null)
                return;
        }

        postProcessor.getTexture().resource.getBundle().getImageSize().set(Window.getWidth(),Window.getHeight());

        postProcessor.getTexture().resource.setID(framebuffer.getTexture());
        postProcessor.requestRender(getAttachedObject().getTransform(), Manager.getMainCamera());
    }

    private boolean inBounds(){
        Vector2f cameraPos = Manager.getMainCamera().getAttachedObject().getTransform().position();
        Vector2f ourPos = getAttachedObject().getTransform().position();
        // if in x bounds
        if(cameraPos.x > ourPos.x - bounds.x && cameraPos.x < ourPos.x + bounds.x){
            // if in y bounds
            if(cameraPos.y > ourPos.y - bounds.y && cameraPos.y < ourPos.y + bounds.y){
                return true;
            }
        }
        return false;
    }

    public ShaderProgram getScreenShader() {
        return screenShader;
    }

    public void setScreenShader(ShaderProgram screenShader) {
        this.screenShader = screenShader;
        init();
    }

    public Vector2f getBounds() {
        return bounds;
    }

    public void setBounds(Vector2f bounds) {
        this.bounds = bounds;
    }
}
