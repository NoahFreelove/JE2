package JE.IO;

import org.lwjgl.BufferUtils;
import org.lwjgl.stb.STBImage;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

public class ImageProcessor {

    public static ByteBuffer ProcessImage(String filepath)
    {
        IntBuffer widthBuf = BufferUtils.createIntBuffer(1);
        IntBuffer heightBuf = BufferUtils.createIntBuffer(1);
        IntBuffer channelsBuf = BufferUtils.createIntBuffer(1);

        return STBImage.stbi_load(filepath,widthBuf,heightBuf,channelsBuf,4);
    }
}
