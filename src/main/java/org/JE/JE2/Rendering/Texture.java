package org.JE.JE2.Rendering;

import org.JE.JE2.Annotations.GLThread;
import org.JE.JE2.Annotations.Nullable;
import org.JE.JE2.IO.Logging.Errors.ImageProcessError;
import org.JE.JE2.IO.Logging.Logger;
import org.JE.JE2.Manager;
import org.JE.JE2.Resources.*;
import org.JE.JE2.Resources.Bundles.TextureBundle;
import org.JE.JE2.Resources.Bundles.ResourceBundle;
import org.joml.Vector2i;
import org.lwjgl.BufferUtils;
import org.lwjgl.stb.STBImage;

import java.io.File;
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
        resource = new Resource<>(new TextureBundle(), "null", 0);
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

    public static Texture checkExistElseCreate(String name, int ID, String bytePath){
        //System.out.println("warming up: " + bytePath);
        Resource<TextureBundle> finalRef = (Resource<TextureBundle>) ResourceManager.getIfExists(TextureBundle.class, name, ID);

        if(finalRef != null){
            return new Texture(finalRef, false);
        }
        else{
            return new Texture(new Resource<>(TextureProcessor.processImage(DataLoader.getBytes(bytePath), true,bytePath), name, ID), true);
        }
    }

    public static Texture checkExistElseCreate(String name, int ID, String bytePath, boolean flip){
        //System.out.println("warming up: " + bytePath);
        Resource<TextureBundle> finalRef = (Resource<TextureBundle>) ResourceManager.getIfExists(TextureBundle.class, name, ID);

        if(finalRef != null){
            return new Texture(finalRef, false);
        }
        else{
            return new Texture(new Resource<>(TextureProcessor.processImage(DataLoader.getBytes(bytePath), flip,bytePath), name, ID), true);
        }
    }

    /**
     * Assumes you've already warmed up or indexed texture.
     * @param name The name of the texture indexed in ResourceManager
     * @return a new Texture object
     */
    @Nullable
    public static Texture get(String name){
        return new Texture((Resource<TextureBundle>) ResourceManager.getIfExists(TextureBundle.class, name, -1), false);
    }

    public Texture(String filepath, boolean flip){
        TextureBundle tb = TextureProcessor.processImage(filepath, flip);
        this.resource = new Resource<>(tb, filepath, -1);
        GenerateTexture();
        ResourceManager.indexResource(resource);
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

    private static class TextureProcessor {
        static TextureBundle processImage(String filepath) {
            return processImage(filepath,true);
        }

        static TextureBundle processImage(String filepath, boolean flip){
            Vector2i imageSize;
            ByteBuffer imageData;
            if(filepath == null)
            {
                Logger.log(new ImageProcessError("Invalid Filepath: null"));
                imageData = BufferUtils.createByteBuffer(1);
                imageSize = new Vector2i(1,1);
                return new TextureBundle(imageSize, imageData,filepath);
            }
            if(filepath.equals("") || new File(filepath).isDirectory() || !new File(filepath).exists())
            {
                Logger.log(new ImageProcessError("Invalid Filepath: " + filepath));
                imageData = BufferUtils.createByteBuffer(1);
                imageSize = new Vector2i(1,1);
                return new TextureBundle(imageSize, imageData,filepath);
            }

            IntBuffer widthBuf = BufferUtils.createIntBuffer(1);
            IntBuffer heightBuf = BufferUtils.createIntBuffer(1);
            IntBuffer channelsBuf = BufferUtils.createIntBuffer(1);
            STBImage.stbi_set_flip_vertically_on_load(flip);


            STBImage.stbi_set_unpremultiply_on_load(true);
            ByteBuffer image = STBImage.stbi_load(filepath, widthBuf, heightBuf, channelsBuf, 4);
            if (image == null) {
                image = BufferUtils.createByteBuffer(1);
                imageData = image;
                Logger.log(new ImageProcessError("Failed to load image: " + STBImage.stbi_failure_reason() + "\nFilepath:" + filepath));
                return new TextureBundle(new Vector2i(),imageData,filepath);
            }
            image.flip();
            imageData = image;
            imageSize = new Vector2i(widthBuf.get(), heightBuf.get());
            return new TextureBundle(imageSize,imageData,filepath);
        }

        static TextureBundle processImage(byte[] data, boolean flip, String filepath){
            Vector2i imageSize;
            ByteBuffer imageData;
            if(data == null)
            {
                Logger.log(new ImageProcessError("Invalid Filepath: null"));
                imageData = BufferUtils.createByteBuffer(1);
                imageSize = new Vector2i(1,1);
                return new TextureBundle(imageSize,imageData,filepath);
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

                return new TextureBundle(new Vector2i(),imageData,filepath);
            }
            image.flip();
            imageData = image;
            imageSize = new Vector2i(widthBuf.get(), heightBuf.get());
            return new TextureBundle(imageSize,imageData,filepath);
        }

        static TextureBundle generateSolidColorImage(Vector2i size, int color){
            Vector2i imageSize;
            ByteBuffer imageData;
            imageSize = size;
            imageData = BufferUtils.createByteBuffer(size.x * size.y * 4);
            for (int i = 0; i < size.x * size.y; i++) {
                imageData.put((byte) ((color >> 16) & 0xFF));
                imageData.put((byte) ((color >> 8) & 0xFF));
                imageData.put((byte) (color & 0xFF));
                imageData.put((byte) ((color >> 24) & 0xFF));
            }
            imageData.flip();
            return new TextureBundle(imageSize,imageData,"");
        }
    }

}

