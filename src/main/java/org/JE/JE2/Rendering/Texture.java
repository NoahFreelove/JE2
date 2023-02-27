package org.JE.JE2.Rendering;

import org.JE.JE2.Annotations.GLThread;
import org.JE.JE2.Manager;
import org.JE.JE2.Resources.Resource;
import org.JE.JE2.Resources.ResourceBundle;
import org.JE.JE2.Resources.ResourceType;
import org.joml.Vector2i;

import java.io.Serializable;
import java.nio.ByteBuffer;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.glActiveTexture;
import static org.lwjgl.opengl.GL20.glGetUniformLocation;
import static org.lwjgl.opengl.GL30.glGenerateMipmap;

public class Texture implements Serializable {
    public Resource resource;
    public int generatedTextureID = -1;

    public Texture(){
    }
    public Texture(Resource resource){
        this.resource = resource;
        GenerateTexture();
    }

    public Texture(ResourceBundle bundle){
        this.resource = new Resource("resource", bundle, ResourceType.TEXTURE);
        GenerateTexture();
    }

    public Texture(String filepath){
        resource = new Resource("texture",filepath, ResourceType.TEXTURE);
        resource.bundle.filepath = filepath;
        GenerateTexture();
    }

    public Texture(byte[] data){
        resource = new Resource("texture",data, ResourceType.TEXTURE);
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
            glEnable(GL_TEXTURE_2D);
            this.generatedTextureID = glGenTextures();
            glBindTexture(GL_TEXTURE_2D, generatedTextureID);
            glPixelStorei(GL_UNPACK_ALIGNMENT, 1);

            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);
            glTexImage2D(GL_TEXTURE_2D,0, GL_RGBA, resource.bundle.imageSize.x(), resource.bundle.imageSize.y(), 0, GL_RGBA, GL_UNSIGNED_BYTE, resource.bundle.imageData);
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST_MIPMAP_NEAREST);
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);

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
