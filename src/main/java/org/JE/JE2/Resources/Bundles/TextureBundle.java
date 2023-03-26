package org.JE.JE2.Resources.Bundles;

import org.joml.Vector2i;
import org.lwjgl.BufferUtils;

import java.nio.ByteBuffer;

public class TextureBundle extends ResourceBundle{
    private transient Vector2i imageSize;
    private transient ByteBuffer imageData;
    public TextureBundle(){
        imageSize = new Vector2i();
        imageData = BufferUtils.createByteBuffer(1);
    }

    public TextureBundle(Vector2i imageSize, ByteBuffer imageData) {
        this.imageSize = imageSize;
        this.imageData = imageData;
        load();
    }

    public Vector2i getImageSize() {
        return imageSize;
    }

    public ByteBuffer getImageData() {
        return imageData;
    }

    @Override
    public void freeResource() {
        imageData = null;
        imageSize = null;
    }
}
