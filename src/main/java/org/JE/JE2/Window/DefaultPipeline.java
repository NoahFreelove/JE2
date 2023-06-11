package org.JE.JE2.Window;

import org.JE.JE2.Manager;
import org.JE.JE2.Objects.GameObject;
import org.JE.JE2.Rendering.Renderers.SpriteRenderer;
import org.JE.JE2.Rendering.VertexBuffers.VAO2f;
import org.JE.JE2.Utility.Watcher;
import org.joml.Vector2f;
import org.joml.Vector2i;
import org.lwjgl.opengl.GL30;

public class DefaultPipeline extends Pipeline{
    @Override
    public void run() {
        // clear framebuffers
        GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, 0);
        GL30.glClear(GL30.GL_COLOR_BUFFER_BIT | GL30.GL_DEPTH_BUFFER_BIT);
        GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, Window.getFramebuffer());
        GL30.glClear(GL30.GL_COLOR_BUFFER_BIT | GL30.GL_DEPTH_BUFFER_BIT);

        runQueuedEvents();
        updateScene();
        GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, Window.getFramebuffer());
        renderObjects();

        GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, 0);
        if(Window.doubleRenderPostProcess())
            renderObjects();
        postProcess();
        renderUI();
        checkWatchers();
    }

    @Override
    public void renderObjects() {

        Manager.activeScene().world.gameObjects.forEach((gameObject) ->{
            if(gameObject == null)
                return;
            if(!gameObject.active())
                return;

            if(gameObject.getRenderer() != null)
            {
                if(!gameObject.getRenderer().getActive())
                    return;
                gameObject.getRenderer().Render(gameObject);
            }
        });
        //GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, 0);
    }

    SpriteRenderer postProcessor = new SpriteRenderer();

    @Override
    public void init() {
        VAO2f screenQuadVAO = new VAO2f(new Vector2f[]{
                new Vector2f(0, 0),
                new Vector2f(1, 0),
                new Vector2f(1, 1),
                new Vector2f(0, 1)
        });
        postProcessor.setShaderProgram(Window.getDefaultPostProcessShader());
        postProcessor.setSpriteVAO(screenQuadVAO);
        postProcessor.getTexture().resource.setID(Window.getFramebufferTexture());
        postProcessor.getTexture().resource.getBundle().setImageSize(new Vector2i(Window.getWidth(), Window.getHeight()));
        postProcessor.getTexture().valid = true;
    }
    @Override
    public void postProcess() {
        if(!Window.doubleRenderPostProcess())
            postProcessor.Render(GameObject.emptyGameObject);

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
        Window.actionQueue.forEach(Runnable::run);
        Window.actionQueue.clear();
    }

    @Override
    public void pollEvents() {
    }

    @Override
    public void updateScene() {
        Manager.activeScene().update();
    }

    @Override
    public void checkWatchers() {
        Manager.activeScene().watchers.forEach(Watcher::invoke);
    }


}
