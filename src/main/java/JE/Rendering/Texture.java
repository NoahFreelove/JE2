package JE.Rendering;

import JE.Annotations.GLThread;
import JE.IO.ImageProcessor;
import JE.Manager;
import JE.Rendering.Shaders.ShaderProgram;
import org.joml.Vector2i;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL13;

import java.io.Serializable;
import java.nio.ByteBuffer;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.glActiveTexture;
import static org.lwjgl.opengl.GL20.glGetUniformLocation;
import static org.lwjgl.opengl.GL20.glUniform1i;
import static org.lwjgl.opengl.GL30.glGenerateMipmap;

public class Texture implements Serializable {
    public ByteBuffer textureBuffer = BufferUtils.createByteBuffer(0);
    public Vector2i size = new Vector2i(64,64);
    public int generatedTextureID = -1;

    public Texture(){
        GenerateTexture();
    }

    public Texture(String filepath, Vector2i size){
        textureBuffer = ImageProcessor.ProcessImage(filepath);
        this.size = size;
        GenerateTexture();
    }

    public Texture(ByteBuffer bb, Vector2i size){
        textureBuffer = bb;
        this.size = size;
        GenerateTexture();
    }

    public void GenerateTexture(){
        Runnable r = () -> {
            this.generatedTextureID = glGenTextures();
            glBindTexture(GL_TEXTURE_2D, generatedTextureID);
            glTexImage2D(GL_TEXTURE_2D,0, GL_RGBA, size.x(), size.y(), 0, GL_RGBA, GL_UNSIGNED_BYTE, textureBuffer);
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR_MIPMAP_LINEAR);
            glGenerateMipmap(GL_TEXTURE_2D);
        };
        Manager.queueGLFunction(r);
    }

    @GLThread
    public void activateTexture(ShaderProgram shaderProgram){
        int textureUniform = glGetUniformLocation(shaderProgram.programID, "JE_Texture");
        glActiveTexture(GL13.GL_TEXTURE0);
        glBindTexture(GL_TEXTURE_2D, generatedTextureID);
        glUniform1i(textureUniform, 0);
    }

}
