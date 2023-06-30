package org.JE.JE2.Resources.Bundles;

import org.JE.JE2.IO.Filepath;
import org.joml.Vector2i;
import org.lwjgl.BufferUtils;

import java.nio.ByteBuffer;

public class TextureBundle extends ResourceBundle{
    private transient Vector2i imageSize;
    private transient ByteBuffer imageData;

    private boolean altered = false;

    public TextureBundle(){
        imageSize = new Vector2i();
        imageData = BufferUtils.createByteBuffer(1);
    }

    public TextureBundle(Vector2i imageSize, ByteBuffer imageData, Filepath filepath) {
        this.imageSize = imageSize;
        this.imageData = imageData;
        this.filepath = filepath;
        load();
    }

    public Vector2i getImageSize() {
        return imageSize;
    }

    public ByteBuffer getImageData() {
        return imageData;
    }

    @Override
    public boolean compareData(byte[] input) {
        if(input == null)
            return false;

        if(input.length != imageData.limit())
            return false;

        for(int i = 0; i < input.length; i++){
            if(input[i] != imageData.get(i))
                return false;
        }
        return true;
    }

    @Override
    public byte[] getData() {
        byte[] data = new byte[imageData.limit()];
        for(int i = 0; i < data.length; i++){
            data[i] = imageData.get(i);
        }
        return data;
    }

    @Override
    public void freeResource() {
        imageData = null;
        imageSize = null;
    }

    public boolean hasBeenAltered(){return altered;}

    public void setImageSize(Vector2i imageSize) {
        this.imageSize = imageSize;
        altered = true;
    }

    public void setImageData(ByteBuffer imageData) {
        this.imageData = imageData;
        altered = true;
    }

    @Override
    public void setFilepath(Filepath filepath) {
        this.filepath = filepath;
        altered = true;
    }
}
