package org.JE.JE2.Window;

import org.JE.JE2.Manager;
import org.JE.JE2.Objects.GameObject;
import org.JE.JE2.Rendering.Framebuffer;
import org.JE.JE2.Rendering.Renderers.FramebufferRenderer;
import org.JE.JE2.Rendering.Renderers.SpriteRenderer;
import org.JE.JE2.Rendering.VertexBuffers.VAO2f;
import org.JE.JE2.Utility.Watcher;
import org.joml.Vector2f;
import org.joml.Vector2i;
import org.lwjgl.opengl.GL30;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL14.GL_FUNC_ADD;
import static org.lwjgl.opengl.GL14.glBlendEquation;

public class DefaultPipeline extends Pipeline{
    public boolean renderObjects = true;
    public boolean renderUI = true;

    @Override
    public void run() {
        Window.getDefaultFramebuffer().clearAndActivate();

        updateScene();

        if(renderObjects)
            renderObjects();

        Framebuffer.bindDefault();

        if(Window.doubleRenderPostProcess() && renderObjects)
            renderObjects();

        postProcess();

        if(renderUI)
            renderUI();

        checkWatchers();

        runQueuedEvents();
    }

    @Override
    public void renderObjects() {

        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        glBlendEquation(GL_FUNC_ADD);

        Manager.activeScene().world.gameObjects.forEach((gameObject) ->{
            if(gameObject == null)
                return;
            if(!gameObject.active())
                return;

            if(gameObject.getRenderer() != null)
            {
                if(!gameObject.getRenderer().getActive())
                    return;
                gameObject.getRenderer().Render(gameObject.getTransform(),0,gameObject.getLayer(),Manager.getMainCamera());
            }
        });
        //GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, 0);
    }

    FramebufferRenderer fbRenderer;
    @Override
    public void init() {
        fbRenderer = new FramebufferRenderer(Window.getDefaultFramebuffer(), Window.getDefaultPostProcessShader());
    }
    @Override
    public void postProcess() {
        if(!Window.doubleRenderPostProcess())
            fbRenderer.Render();

        Manager.activeScene().world.gameObjects.forEach((gameObject) -> {
            if(gameObject == null)
                return;
            if(!gameObject.active())
                return;
            gameObject.scriptPostRender();
        });
    }

    @Override
    public void renderUI() {
        UIHandler.renderNuklear();
    }

    @Override
    public void runQueuedEvents() {
        while (Window.actionQueue.size()>0){
            Runnable r = Window.actionQueue.get(0);
            Window.actionQueue.remove(0);
            r.run();
        }
        Window.actionQueue.clear();
    }

    @Override
    public void updateScene() {
        Manager.activeScene().update();
    }

    @Override
    public void checkWatchers() {
        Manager.activeScene().watchers.forEach(Watcher::invoke);
    }

    public void pushNoUpdateRender(){
        Window.queueGLFunction(new Runnable() {
            @Override
            public void run() {
                Manager.activeScene().updateUI = false;
                Manager.activeScene().updateObjects = false;
                Window.frameStart();
                Manager.activeScene().updateUI = true;
                Manager.activeScene().updateObjects = true;
            }
        },0);
    }


}
