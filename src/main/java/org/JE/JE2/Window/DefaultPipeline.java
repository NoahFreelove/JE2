package org.JE.JE2.Window;

import org.JE.JE2.Manager;
import org.JE.JE2.Objects.GameObject;
import org.JE.JE2.Rendering.Framebuffer;
import org.JE.JE2.Rendering.Renderers.FramebufferRenderer;
import org.JE.JE2.Rendering.Renderers.Renderer;
import org.JE.JE2.Scene.Scene;
import org.JE.JE2.Utility.Watcher;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL14.GL_FUNC_ADD;
import static org.lwjgl.opengl.GL14.glBlendEquation;

public class DefaultPipeline extends Pipeline{
    public boolean renderObjects = true;
    public boolean renderUI = true;

    private static void renderObject(GameObject gameObject) {
        if (gameObject == null)
            return;
        if (!gameObject.active())
            return;

        Renderer r = gameObject.getRenderer();

        if (r != null) {
            if (!r.getActive())
                return;
            r.requestRender(gameObject.getTransform(), Manager.getMainCamera());
        }
    }

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

        Scene.persistentScene.getWorld().gameObjects.forEach(DefaultPipeline::renderObject);

        Manager.activeScene().getWorld().gameObjects.forEach(DefaultPipeline::renderObject);
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

        Scene.persistentScene.getWorld().gameObjects.forEach((gameObject) -> {
            if(gameObject == null)
                return;
            if(!gameObject.active())
                return;
            gameObject.scriptPostRender();
        });

        Manager.activeScene().getWorld().gameObjects.forEach((gameObject) -> {
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
        Window.runQueuedEvents();
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
