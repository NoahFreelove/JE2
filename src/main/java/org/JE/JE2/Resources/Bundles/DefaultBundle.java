package org.JE.JE2.Resources.Bundles;

import org.JE.JE2.Resources.DataLoader;
import org.lwjgl.BufferUtils;

import java.nio.ByteBuffer;

public class DefaultBundle extends ResourceBundle{
    private ByteBuffer data;

    public DefaultBundle() {
        data = BufferUtils.createByteBuffer(1);
    }
    public DefaultBundle(ByteBuffer data) {
        this.data = data;
        load();
    }

    public DefaultBundle(String dataPath) {
        byte[] loadedData = DataLoader.getClassLoaderBytes(dataPath);
        data = ByteBuffer.allocateDirect(loadedData.length);
        data.put(loadedData);
        data.flip();
    }

    @Override
    public boolean compareData(byte[] input) {
        byte[] data = getData();
        if(input == null)
            return false;

        if(input.length != data.length)
            return false;

        for(int i = 0; i < input.length; i++){
            if(input[i] != data[i])
                return false;
        }
        return true;
    }

    public byte[] getData() {
        byte[] data = new byte[this.data.limit()];
        for(int i = 0; i < data.length; i++){
            data[i] = this.data.get(i);
        }
        return data;
    }

    @Override
    public void freeResource() {
        data = null;
    }
}
