package org.JE.JE2.Objects.Scripts.CameraEffects;

import org.JE.JE2.Annotations.GLThread;
import org.JE.JE2.Manager;
import org.JE.JE2.Objects.Scripts.Script;
import org.JE.JE2.Rendering.Shaders.ShaderProgram;
import org.JE.JE2.Window.Window;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30C.glBindVertexArray;
import static org.lwjgl.opengl.GL30C.glGenVertexArrays;

public class PostProcessingVolume extends Script {

    ShaderProgram screenShader;
    private int quadVAO;
    private int quadVBO;

    public PostProcessingVolume(){
        super();
        screenShader = new ShaderProgram();
        screenShader.createShader("","");


        // create a quad VAO with full screen coordinates

        float[] quadVertices = {
                // positions  // texCoords
                -1.0f,  1.0f,  0.0f, 1.0f,
                -1.0f, -1.0f,  0.0f, 0.0f,
                 1.0f, -1.0f,  1.0f, 0.0f,

                -1.0f,  1.0f,  0.0f, 1.0f,
                    1.0f, -1.0f,  1.0f, 0.0f,
                    1.0f,  1.0f,  1.0f, 1.0f
        };

        Manager.queueGLFunction(() -> {
            quadVAO = glGenVertexArrays();
            quadVBO = glGenBuffers();
            glBindVertexArray(quadVAO);
            glBindBuffer(GL_ARRAY_BUFFER, quadVBO);
            glBufferData(GL_ARRAY_BUFFER, quadVertices, GL_STATIC_DRAW);
            glEnableVertexAttribArray(0);
            glVertexAttribPointer(0, 2, GL_FLOAT, false, 4 * 4, 0);
            glEnableVertexAttribArray(1);
            glVertexAttribPointer(1, 2, GL_FLOAT, false, 4 * 4, 2 * 4);
        });
    }

    @Override
    @GLThread
    public void postRender(){
        System.out.println("post render: " +screenShader.use());
        screenShader.setUniform1i("screenTexture",0);
        glBindVertexArray(quadVAO);
        glActiveTexture(GL_TEXTURE0);
        //glBindTexture(GL_TEXTURE_2D, Window.textureColorBuffer);
        glDrawArrays(GL_TRIANGLES, 0, 6);


    }
}
