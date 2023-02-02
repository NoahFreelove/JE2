package JE.IO.FileInput;

import JE.Annotations.GLThread;
import JE.Logging.Errors.ImageProcessError;
import JE.Logging.Logger;
import JE.Rendering.Texture;
import JE.Resources.Resource;
import JE.Resources.ResourceBundle;
import JE.Resources.ResourceType;
import org.joml.Vector2i;
import org.lwjgl.BufferUtils;
import org.lwjgl.stb.STBImage;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.awt.image.renderable.RenderedImageFactory;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;

public class ImageProcessor {
    public static ResourceBundle ProcessImage(String filepath) {
        return ProcessImage(filepath,true);
    }

        public static ResourceBundle ProcessImage(String filepath,boolean flip){
        ResourceBundle rb = new ResourceBundle();
        if(filepath == null)
        {
            Logger.log(new ImageProcessError(true));
            rb.imageData = BufferUtils.createByteBuffer(1);
            rb.imageSize = new Vector2i(1,1);
            return rb;
        }
        if(filepath.equals("") || new File(filepath).isDirectory() || !new File(filepath).exists())
        {
            Logger.log(new ImageProcessError(true));
            return rb;
        }

        IntBuffer widthBuf = BufferUtils.createIntBuffer(1);
        IntBuffer heightBuf = BufferUtils.createIntBuffer(1);
        IntBuffer channelsBuf = BufferUtils.createIntBuffer(1);
        STBImage.stbi_set_flip_vertically_on_load(flip);


        STBImage.stbi_set_unpremultiply_on_load(true);
        ByteBuffer image = STBImage.stbi_load(filepath, widthBuf, heightBuf, channelsBuf, 4);
        if (image == null) {
            image = BufferUtils.createByteBuffer(1);
            rb.imageData = image;
            Logger.log(new ImageProcessError("Failed to load image: " + STBImage.stbi_failure_reason()));
            return rb;
        }
        rb.imageData = image;
        rb.imageSize = new Vector2i(widthBuf.get(), heightBuf.get());
        return rb;
    }
    public static ResourceBundle generateSolidColorImage(Vector2i size, int color){
        ResourceBundle rb = new ResourceBundle();
        rb.imageSize = size;
        rb.imageData = BufferUtils.createByteBuffer(size.x * size.y * 4);
        for (int i = 0; i < size.x * size.y; i++) {
            rb.imageData.put((byte) ((color >> 16) & 0xFF));
            rb.imageData.put((byte) ((color >> 8) & 0xFF));
            rb.imageData.put((byte) (color & 0xFF));
            rb.imageData.put((byte) ((color >> 24) & 0xFF));
        }
        rb.imageData.flip();
        return rb;
    }

    public static Texture generateSolidColorImage(Vector2i size, int color, String textureName){
        return new Texture(new Resource(textureName, generateSolidColorImage(size,color), ResourceType.TEXTURE));
    }


    public static ResourceBundle generateNormalMap(Texture originalTexture){
        ResourceBundle rb = originalTexture.resource.bundle;
        ResourceBundle rb2 = new ResourceBundle();
        rb2.imageSize = rb.imageSize;
        rb2.imageData = BufferUtils.createByteBuffer(rb.imageSize.x * rb.imageSize.y * 4);
        for (int i = 0; i < rb.imageSize.x * rb.imageSize.y; i++) {
            rb2.imageData.put((byte) 127);
            rb2.imageData.put((byte) 127);
            rb2.imageData.put((byte) 255);
            rb2.imageData.put((byte) 255);
        }
        rb2.imageData.flip();
        return rb2;
    }
}