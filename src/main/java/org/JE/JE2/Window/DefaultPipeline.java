package org.JE.JE2.Window;

import org.JE.JE2.Manager;
import org.JE.JE2.Rendering.VertexBuffers.VAO;
import org.JE.JE2.Utility.Watcher;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import static org.JE.JE2.Window.Window.*;
import static org.lwjgl.opengl.GL11.GL_DEPTH_TEST;
import static org.lwjgl.opengl.GL11.glDisable;
import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.glActiveTexture;
import static org.lwjgl.opengl.GL20.glGetUniformLocation;
import static org.lwjgl.opengl.GL20.glUniform1i;

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
    }

    @Override
    public void postProcess() {

        if(!defaultPostProcessShader.valid())
            return;


        // Bind the framebuffer
        GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, fboId);

        // Set the viewport to match the framebuffer size
        GL11.glViewport(0, 0, Window.getWidth(), Window.getHeight());

        // Clear the framebuffer
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);

        // Bind the texture
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, textureId);

        // Use the shader program
        defaultPostProcessShader.use();

        // Bind the quad VAO and draw the quad
        GL30.glBindVertexArray(quadVAO);
        GL11.glDrawArrays(GL11.GL_QUADS, 0, 4);

        GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, 0);



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
