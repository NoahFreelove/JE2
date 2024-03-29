package org.JE.JE2.Rendering;

import org.JE.JE2.Annotations.GLThread;
import org.JE.JE2.IO.Filepath;
import org.JE.JE2.IO.Logging.Errors.ImageProcessError;
import org.JE.JE2.IO.Logging.Logger;
import org.JE.JE2.Manager;
import org.JE.JE2.Resources.*;
import org.JE.JE2.Resources.Bundles.TextureBundle;
import org.joml.Vector2i;
import org.lwjgl.BufferUtils;
import org.lwjgl.stb.STBImage;

import java.io.Serializable;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.glActiveTexture;
import static org.lwjgl.opengl.GL30.glGenerateMipmap;

public class Texture implements Serializable {
    public Resource<TextureBundle> resource;
    public boolean valid = false;
    public int forceValidateMode = 0;
    public Texture(){
        resource = new Resource<>(new TextureBundle(), "empty", 0);
    }

    private Texture(Resource<TextureBundle> resource, boolean newTexture){
        if(resource == null)
        {
            valid = false;
            return;
        }
        this.resource = resource;

        if(newTexture) {
            GenerateTexture();
            ResourceManager.indexResource(resource);
        }
        else
            valid = true;
    }

    public static Texture checkExistElseCreate(String name, int ID, Filepath filepath){
        return checkExistElseCreate(name, ID, filepath, filepath.flag_flipTexture);
    }

    public static Texture checkExistElseCreate(String name, Filepath filepath){
        return checkExistElseCreate(name, -1, filepath, filepath.flag_flipTexture);
    }
    public static Texture checkExistElseCreate(String name, Filepath filepath, boolean flip){
        return checkExistElseCreate(name, -1, filepath, flip);
    }

    public static Texture checkExistElseCreate(String name, int ID, Filepath filepath, boolean flip){
        //System.out.println("warming up: " + bytePath);
        Resource<TextureBundle> finalRef = (Resource<TextureBundle>) ResourceManager.getIfExists(TextureBundle.class, name, ID);

        if(finalRef != null){
            return new Texture(finalRef, false);
        }
        else{
            if(filepath.isClassLoaderPath)
                return new Texture(new Resource<>(TextureProcessor.processImage(DataLoader.getClassLoaderBytes(filepath.getPath(true)), flip,filepath), name, ID), true);
            else {
                return new Texture(new Resource<>(TextureProcessor.processImage(DataLoader.getFileData(filepath.getPath(false)), flip,filepath), name, ID), true);

            }
        }
    }

    public static Texture createTexture(String name, Filepath fp, boolean flip){
        Texture t = checkExistElseCreate("",-1, fp,flip);
        t.resource.setName(name);
        return t;
    }

    public static Texture createTexture(Resource<TextureBundle> res, boolean newTexture){
        return new Texture(res,newTexture);
    }

    /**
     * Assumes you've already warmed up or indexed texture.
     * @param name The name of the texture indexed in ResourceManager
     * @return a new Texture object
     */
    public static Texture get(String name){
        return new Texture((Resource<TextureBundle>) ResourceManager.getIfExists(TextureBundle.class, name, -1), false);
    }

    public static Texture createTextureFromBytes(String name, TextureBundle tb){
        Resource<TextureBundle> newResource = new Resource<>(tb, name,-1);
        ResourceManager.indexResource(newResource);
        return new Texture(newResource, true);
    }



    public void GenerateTexture(){
        if(resource.getBundle().getImageData().limit() == 1)
            return;
        Runnable r = () -> {
            if(resource == null)
                return;
            glEnable(GL_TEXTURE_2D);
            resource.setID(glGenTextures());
            glBindTexture(GL_TEXTURE_2D, resource.getID());
            glPixelStorei(GL_UNPACK_ALIGNMENT, 1);

            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);
            glTexImage2D(GL_TEXTURE_2D,0, GL_RGBA, resource.getBundle().getImageSize().x(), resource.getBundle().getImageSize().y(), 0, GL_RGBA, GL_UNSIGNED_BYTE, resource.getBundle().getImageData());
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST_MIPMAP_NEAREST);
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);

            glGenerateMipmap(GL_TEXTURE_2D);
            if(forceValidateMode>0){
                valid = forceValidateMode == 1;
            }
            else
                valid = true;
        };
        Manager.queueGLFunction(r);
    }


    @GLThread
    public boolean activateTexture(int textureSlot){
        if(!valid)
            return false;
        glActiveTexture(textureSlot);
        glBindTexture(GL_TEXTURE_2D, resource.getID());
        return true;
    }

    public void unbindTexture(int glTexture0) {
        glActiveTexture(glTexture0);
        glBindTexture(GL_TEXTURE_2D, 0);
    }

    private static class TextureProcessor {

        static TextureBundle processImage(byte[] data, boolean flip, Filepath filepath){
            Vector2i imageSize;
            ByteBuffer imageData;
            if(data == null)
            {
                Logger.log(new ImageProcessError("Invalid Filepath: null"));
                imageData = BufferUtils.createByteBuffer(1);
                imageSize = new Vector2i(1,1);
                return new TextureBundle(imageSize,imageData,0,filepath);
            }

            IntBuffer widthBuf = BufferUtils.createIntBuffer(1);
            IntBuffer heightBuf = BufferUtils.createIntBuffer(1);
            IntBuffer channelsBuf = BufferUtils.createIntBuffer(1);
            STBImage.stbi_set_flip_vertically_on_load(flip);
            STBImage.stbi_set_unpremultiply_on_load(true);
            ByteBuffer putData = BufferUtils.createByteBuffer(data.length);
            putData.put(data);
            putData.flip();
            ByteBuffer image = STBImage.stbi_load_from_memory(putData, widthBuf, heightBuf, channelsBuf, 4);

            if (image == null) {
                image = BufferUtils.createByteBuffer(1);
                imageData = image;
                Logger.log(new ImageProcessError("Failed to load image: " + STBImage.stbi_failure_reason() + "\nbytes:" + data.length));

                return new TextureBundle(new Vector2i(),imageData,0,filepath);
            }
            image.flip();
            imageData = image;
            imageSize = new Vector2i(widthBuf.get(), heightBuf.get());
            int channels = channelsBuf.get();
            return new TextureBundle(imageSize,imageData,channels,filepath);
        }
    }

}

