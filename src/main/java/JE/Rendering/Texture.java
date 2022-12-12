package JE.Rendering;

import JE.Annotations.GLThread;
import JE.Manager;
import JE.Rendering.Shaders.ShaderProgram;
import JE.Resources.Resource;
import JE.Resources.ResourceType;
import org.joml.Vector2i;
import org.lwjgl.opengl.GL13;

import java.io.Serializable;
import java.nio.ByteBuffer;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.glActiveTexture;
import static org.lwjgl.opengl.GL20.glGetUniformLocation;
import static org.lwjgl.opengl.GL20.glUniform1i;
import static org.lwjgl.opengl.GL30.glGenerateMipmap;

public class Texture implements Serializable {
    public Resource resource;
    public int generatedTextureID = -1;

    public Texture(){
        GenerateTexture();
    }

    public Texture(String filepath){
        resource = new Resource("texture",filepath, ResourceType.TEXTURE);
        GenerateTexture();
    }

    public Texture(String filepath, Vector2i size){
        resource = new Resource("texture",filepath, ResourceType.TEXTURE);
        resource.bundle.imageSize = size;
        GenerateTexture();
    }

    public Texture(ByteBuffer bb, Vector2i size){
        resource = new Resource("texture",bb,size, ResourceType.TEXTURE);
        GenerateTexture();
    }

    public void GenerateTexture(){
        Runnable r = () -> {
            if(resource == null)
                return;
            this.generatedTextureID = glGenTextures();
            glBindTexture(GL_TEXTURE_2D, generatedTextureID);
            glTexImage2D(GL_TEXTURE_2D,0, GL_RGBA, resource.bundle.imageSize.x(), resource.bundle.imageSize.y(), 0, GL_RGBA, GL_UNSIGNED_BYTE, resource.bundle.imageData);
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR_MIPMAP_LINEAR);
            glGenerateMipmap(GL_TEXTURE_2D);
        };
        Manager.queueGLFunction(r);
    }


    @GLThread
    public void activateTexture(int textureSlot){
        glActiveTexture(textureSlot);
        glBindTexture(GL_TEXTURE_2D, generatedTextureID);
    }
}
