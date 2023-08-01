package org.JE.JE2.Rendering;

import org.JE.JE2.IO.Filepath;
import org.JE.JE2.Resources.Bundles.TextureBundle;
import org.JE.JE2.UI.UIElements.Style.Color;
import org.JE.JE2.Utility.JE2Math;
import org.joml.Vector2i;
import org.lwjgl.BufferUtils;

import java.nio.ByteBuffer;

public class TextureUtils {

    public static Texture saturateTexture(Texture input, String newName, float level){
        TextureBundle tb = new TextureBundle(input.resource.getBundle().getImageSize(), saturateBuffer(input.resource.getBundle(),level), input.resource.getBundle().getChannels(), input.resource.getBundle().filepath);
        return Texture.createTextureFromBytes(newName, tb);
    }

    public static ByteBuffer saturateBuffer(TextureBundle text, float level){
        int width = text.getImageSize().x();
        int height = text.getImageSize().y();
        int channels = text.getChannels();
        ByteBuffer imageBuffer = text.getImageData();
        imageBuffer.rewind();

        // set limit to capacity
        imageBuffer.limit(imageBuffer.capacity());

        ByteBuffer tintedImageBuffer = ByteBuffer.allocateDirect(width * height * channels);

        // Iterate through each pixel in the original buffer, modify the saturation, and store the modified pixel in the new buffer
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int pixelStart = (x + y * width) * channels;

                // Extract the RGB components of the pixel (assuming the image has RGB channels, and alpha channel if it exists)
                float r = (imageBuffer.get(pixelStart) & 0xFF) / 255.0f;
                float g = (imageBuffer.get(pixelStart + 1) & 0xFF) / 255.0f;
                float b = (imageBuffer.get(pixelStart + 2) & 0xFF) / 255.0f;

                // Calculate the grayscale value of the pixel
                float grayValue = (r + g + b) / 3.0f;

                // Apply saturation to each RGB component
                r = grayValue + level * (r - grayValue);
                g = grayValue + level * (g - grayValue);
                b = grayValue + level * (b - grayValue);

                // Clamp the color values to [0, 1]
                r = Math.min(1.0f, Math.max(0.0f, r));
                g = Math.min(1.0f, Math.max(0.0f, g));
                b = Math.min(1.0f, Math.max(0.0f, b));

                // Store the modified pixel in the new buffer
                tintedImageBuffer.put((byte) (r * 255.0f));
                tintedImageBuffer.put((byte) (g * 255.0f));
                tintedImageBuffer.put((byte) (b * 255.0f));

                if (channels == 4) {
                    // If the image has an alpha channel, copy it as well
                    tintedImageBuffer.put(imageBuffer.get(pixelStart + 3));
                }
            }
        }

// Reset the position of the new buffer to prepare for reading
        tintedImageBuffer.flip();

        imageBuffer.rewind();
        imageBuffer.flip();

        return tintedImageBuffer;
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
        int channels = 4;
        return new TextureBundle(imageSize,imageData,channels, Filepath.empty());
    }
}
