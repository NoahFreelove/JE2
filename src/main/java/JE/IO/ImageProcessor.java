package JE.IO;

import JE.Annotations.GLThread;
import JE.Logging.Errors.ImageProcessError;
import JE.Logging.Logger;
import JE.Resources.Resource;
import JE.Resources.ResourceBundle;
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

    public static ResourceBundle ProcessImage(String filepath){
        ResourceBundle rb = new ResourceBundle();

        if(filepath == null)
        {
            Logger.log(new ImageProcessError(true));
            return null;
        }
        if(filepath.equals("") || new File(filepath).isDirectory() || !new File(filepath).exists())
        {
            Logger.log(new ImageProcessError(true));
            return null;
        }

        IntBuffer widthBuf = BufferUtils.createIntBuffer(1);
        IntBuffer heightBuf = BufferUtils.createIntBuffer(1);
        IntBuffer channelsBuf = BufferUtils.createIntBuffer(1);
        STBImage.stbi_set_flip_vertically_on_load(true);
        STBImage.stbi_set_unpremultiply_on_load(true);
        ByteBuffer image = STBImage.stbi_load(filepath, widthBuf, heightBuf, channelsBuf, 4);
        if (image == null) {
            Logger.log(new ImageProcessError("Failed to load image: " + STBImage.stbi_failure_reason()));
            return rb;
        }
        rb.imageData = image;
        rb.imageSize = new Vector2i(widthBuf.get(), heightBuf.get());
        return rb;
    }


    public static ResourceBundle generateNormalImage(ResourceBundle textureBundle){
        ResourceBundle normalBundle = new ResourceBundle();
        normalBundle.filepath = textureBundle.filepath;
        normalBundle.imageSize = new Vector2i(textureBundle.imageSize);

        try {
            BufferedImage bi = ImageIO.read(new File(textureBundle.filepath));
            BufferedImage normal = new BufferedImage(bi.getWidth(), bi.getHeight(), bi.getType());


            for (int x = 0; x < bi.getWidth(); x++) {
                for (int y = 0; y < bi.getHeight(); y++) {
                    int rgb = bi.getRGB(x, y);
                    int r = (rgb >> 16) & 0xFF;
                    int g = (rgb >> 8) & 0xFF;
                    int b = (rgb & 0xFF);

                    float xNormal = (r - 128) / 128f;
                    float yNormal = (g - 128) / 128f;
                    float zNormal = (b - 128) / 128f;

                    int rNormal = (int) (128 + (xNormal * 128f));
                    int gNormal = (int) (128 + (yNormal * 128f));
                    int bNormal = (int) (128 + (zNormal * 128f));

                    int normalRGB = (rNormal << 16) | (gNormal << 8) | bNormal;
                    normal.setRGB(x, y, normalRGB);
                }
            }
            normalBundle.imageData = BufferUtils.createByteBuffer(normal.getWidth() * normal.getHeight() * 4);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(bi, "png", baos);
            normalBundle.imageData = BufferUtils.createByteBuffer(baos.size());
            normalBundle.imageData.put(baos.toByteArray());}
        catch (IOException e){
            System.out.println(e.getMessage());
        }

        return normalBundle;
    }
}