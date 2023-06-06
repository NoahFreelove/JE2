package org.JE.JE2.Window;

import org.JE.JE2.Manager;
import org.JE.JE2.Objects.GameObject;
import org.JE.JE2.Objects.Scripts.Transform;
import org.JE.JE2.Rendering.Renderers.SpriteRenderer;
import org.JE.JE2.Rendering.Texture;
import org.JE.JE2.Rendering.VertexBuffers.VAO;
import org.JE.JE2.Utility.Watcher;
import org.joml.Vector4f;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import static org.JE.JE2.Window.Window.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.glActiveTexture;
import static org.lwjgl.opengl.GL15.glBindBuffer;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30.glBindVertexArray;

public class DefaultPipeline extends Pipeline{
    @Override
    public void run() {
        // clear framebuffers
        GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, 0);
        GL30.glClear(GL30.GL_COLOR_BUFFER_BIT | GL30.GL_DEPTH_BUFFER_BIT);
        GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, framebuffer);
        GL30.glClear(GL30.GL_COLOR_BUFFER_BIT | GL30.GL_DEPTH_BUFFER_BIT);

        runQueuedEvents();
        updateScene();
        GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, framebuffer);
        renderObjects();
        GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, 0);
        //renderObjects();
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
                //gameObject.getRenderer().Render(Transform.zero,0, new Vector4f(0,0,Window.getWidth(),Window.getHeight()),Manager.getMainCamera());
            }
        });
        //GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, 0);
    }

    SpriteRenderer postProcessor = new SpriteRenderer();
    @Override
    public void init() {
        postProcessor.setSpriteVAO(screenVAO);
        postProcessor.getTexture().resource.setID(colorTexture);
        postProcessor.getTexture().valid = true;

    }
    @Override
    public void postProcess() {
        postProcessor.Render(GameObject.emptyGameObject);
        //postProcessor.Render(Manager.getMainCamera().getAttachedObject());

        /*// Clear the default framebuffer
        GL11.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);

        // Use the post process shader program
        GL20.glUseProgram(defaultPostProcessShader.programID);
        glBindVertexArray(quadVAO);
        glDisable(GL_DEPTH_TEST);
        glBindTexture(GL_TEXTURE_2D, colorTexture);

        glEnableVertexAttribArray(0);
        glBindBuffer(GL20.GL_ARRAY_BUFFER, quadVBO);
        glVertexAttribPointer(0, 2, GL_FLOAT, false, 0, 0);
        glDrawArrays(GL_TRIANGLES, 0, 6);*/


        /* //renderObjects();
        glDisable(GL_DEPTH_TEST);
        Manager.activeScene().world.gameObjects.forEach((gameObject) -> {
            if(gameObject == null)
                return;
            gameObject.scriptPostRender();
        });*/
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
