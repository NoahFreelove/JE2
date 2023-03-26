package org.JE.JE2.Resources.Bundles;

import org.JE.JE2.Resources.ResourceLoader;
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
        byte[] loadedData = ResourceLoader.getBytes(dataPath);
        data = ByteBuffer.allocateDirect(loadedData.length);
        data.put(loadedData);
        data.flip();
    }

    public ByteBuffer getData() {
        return data;
    }

    @Override
    public void freeResource() {
        data = null;
    }
}
