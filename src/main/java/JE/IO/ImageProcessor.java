package JE.IO;

import JE.Logging.Errors.ImageProcessError;
import JE.Logging.Logger;
import org.lwjgl.BufferUtils;
import org.lwjgl.stb.STBImage;

import java.io.File;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;

public class ImageProcessor {

    public static ByteBuffer ProcessImage(String filepath)
    {
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
        }
        return image;
    }
}
