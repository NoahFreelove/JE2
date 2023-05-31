package org.JE.JE2.Window;

import org.JE.JE2.Manager;
import org.JE.JE2.Rendering.VertexBuffers.VAO;
import org.JE.JE2.Utility.Watcher;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import static org.JE.JE2.Window.Window.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.glActiveTexture;
import static org.lwjgl.opengl.GL20.glGetUniformLocation;
import static org.lwjgl.opengl.GL20.glUniform1i;
import static org.lwjgl.opengl.GL30.glBindVertexArray;

public class DefaultPipeline extends Pipeline{
    @Override
    public void run() {
        runQueuedEvents();
        updateScene();
        renderObjects();
        postProcess();
        renderUI();
        checkWatchers();
    }

    @Override
    public void renderObjects() {
        GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, framebuffer);

        Manager.activeScene().world.gameObjects.forEach((gameObject) ->{
            if(gameObject == null)
                return;
            if(!gameObject.active())
                return;

            if(gameObject.getRenderer() != null)
            {
                if(!gameObject.getRenderer().getActive())
                    return;
                gameObject.getRenderer().Render(gameObject,0, Manager.getMainCamera());
            }
        });
        GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, 0);
    }

    @Override
    public void postProcess() {
        // Clear the default framebuffer
        GL11.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);

        // Use the post process shader program
        GL20.glUseProgram(defaultPostProcessShader.programID);
        glBindVertexArray(quadVAO);
        glDisable(GL_DEPTH_TEST);
        glBindTexture(GL_TEXTURE_2D, colorTexture);
        glDrawArrays(GL_TRIANGLES, 0, 6);


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
