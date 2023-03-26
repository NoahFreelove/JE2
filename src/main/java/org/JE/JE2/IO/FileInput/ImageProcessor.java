package org.JE.JE2.IO.FileInput;

import org.JE.JE2.IO.Logging.Errors.ImageProcessError;
import org.JE.JE2.IO.Logging.Logger;
import org.JE.JE2.Rendering.Texture;
import org.JE.JE2.Resources.Bundles.TextureBundle;
import org.JE.JE2.Resources.Resource;
import org.JE.JE2.Resources.Bundles.ResourceBundle;
import org.JE.JE2.Resources.ResourceType;
import org.joml.Vector2i;
import org.lwjgl.BufferUtils;
import org.lwjgl.stb.STBImage;

import java.io.File;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;

public class ImageProcessor {
    public static TextureBundle processImage(String filepath) {
        return processImage(filepath,true);
    }

    public static TextureBundle processImage(String filepath, boolean flip){
        Vector2i imageSize;
        ByteBuffer imageData;
        if(filepath == null)
        {
            Logger.log(new ImageProcessError(true));
            imageData = BufferUtils.createByteBuffer(1);
            imageSize = new Vector2i(1,1);
            return new TextureBundle(imageSize, imageData);
        }
        if(filepath.equals("") || new File(filepath).isDirectory() || !new File(filepath).exists())
        {
            Logger.log(new ImageProcessError(true));
            imageData = BufferUtils.createByteBuffer(1);
            imageSize = new Vector2i(1,1);
            return new TextureBundle(imageSize, imageData);
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
            return new TextureBundle(new Vector2i(),imageData);
        }
        image.flip();
        imageData = image;
        imageSize = new Vector2i(widthBuf.get(), heightBuf.get());
        return new TextureBundle(imageSize,imageData);
    }

    public static TextureBundle processImage(byte[] data, boolean flip){
        Vector2i imageSize;
        ByteBuffer imageData;
        if(data == null)
        {
            Logger.log(new ImageProcessError(true));
            imageData = BufferUtils.createByteBuffer(1);
            imageSize = new Vector2i(1,1);
            return new TextureBundle(imageSize,imageData);
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

            return new TextureBundle(new Vector2i(),imageData);
        }
        image.flip();
        imageData = image;
        imageSize = new Vector2i(widthBuf.get(), heightBuf.get());
        return new TextureBundle(imageSize,imageData);
    }

    public static TextureBundle generateSolidColorImage(Vector2i size, int color){
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
        return new TextureBundle(imageSize,imageData);
    }

    public static Texture generateSolidColorImage(Vector2i size, int color, String textureName){
        return new Texture(new Resource(textureName, generateSolidColorImage(size,color), ResourceType.TEXTURE));
    }

    public static TextureBundle generateNormalMap(Texture originalTexture){
        TextureBundle rb = originalTexture.resource.getTextureBundle();
        Vector2i imageSize;
        ByteBuffer imageData;
        imageSize = rb.getImageSize();
        imageData = BufferUtils.createByteBuffer(rb.getImageSize().x * rb.getImageSize().y * 4);
        for (int i = 0; i < rb.getImageSize().x * rb.getImageSize().y; i++) {
            imageData.put((byte) 127);
            imageData.put((byte) 127);
            imageData.put((byte) 255);
            imageData.put((byte) 255);
        }
        imageData.flip();
        return new TextureBundle(imageSize,imageData);
    }
}