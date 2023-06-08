package org.JE.JE2.Objects.Scripts.ScreenEffects.PostProcess;

import org.JE.JE2.Annotations.GLThread;
import org.JE.JE2.Manager;
import org.JE.JE2.Objects.Scripts.Script;
import org.JE.JE2.Rendering.Renderers.SpriteRenderer;
import org.JE.JE2.Rendering.Shaders.ShaderProgram;
import org.JE.JE2.Rendering.Shaders.ShaderRegistry;
import org.JE.JE2.Rendering.VertexBuffers.VAO2f;
import org.JE.JE2.Window.Window;
import org.joml.Vector2f;
import org.joml.Vector2i;

import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30C.glGenVertexArrays;

/**
 * Will apply to entire screen when active camera is in bounds
 */
public class PostProcessingVolume extends Script {

    ShaderProgram screenShader;
    Vector2f bounds = new Vector2f(5,5);
    SpriteRenderer postProcessor = new SpriteRenderer();

    public PostProcessingVolume(){
        super();
        screenShader = Window.getDefaultPostProcessShader();
        init();
    }

    public PostProcessingVolume(ShaderProgram program){
        super();
        screenShader = program;
        init();
    }

    private void init(){
        VAO2f screenQuadVAO = new VAO2f(new Vector2f[]{
                new Vector2f(0, 0),
                new Vector2f(1, 0),
                new Vector2f(1, 1),
                new Vector2f(0, 1)
        }, screenShader);
        postProcessor.setSpriteVAO(screenQuadVAO);
        postProcessor.getTexture().resource.getBundle().setImageSize(new Vector2i(Window.getWidth(), Window.getHeight()));
        postProcessor.getTexture().valid = true;
    }

    float i = 0;
    @Override
    @GLThread
    public void postRender(){
        if(!inBounds()){
            return;
        }
        postProcessor.getTexture().resource.setID(Window.getFramebufferTexture());
        postProcessor.Render(getAttachedObject());
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
}
