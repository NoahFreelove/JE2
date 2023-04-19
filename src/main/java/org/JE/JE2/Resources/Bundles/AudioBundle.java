package org.JE.JE2.Resources.Bundles;

import org.lwjgl.BufferUtils;

import java.nio.ShortBuffer;

public class AudioBundle extends ResourceBundle{
    private transient ShortBuffer soundData;
    private transient int format;
    private transient int sampleRate;
    private transient int channels;
    
    public AudioBundle() {
        soundData = BufferUtils.createShortBuffer(1);
        format = 0;
        sampleRate = 0;
        channels = 0;
    }

    public AudioBundle(ShortBuffer soundData, int format, int sampleRate, int channels) {
        this.soundData = soundData;
        this.format = format;
        this.sampleRate = sampleRate;
        this.channels = channels;
        load();
    }

    @Override
    public boolean compareData(byte[] input) {
        // convert byte array to short array
        short[] shorts = new short[input.length / 2];
        for (int i = 0; i < shorts.length; i++) {
            shorts[i] = (short) ((input[i * 2] & 0xff) | (input[i * 2 + 1] << 8));
        }

        // CHeck if the data is the same
        if(soundData.capacity() != shorts.length)
            return false;
        for (int i = 0; i < soundData.capacity(); i++) {
            if(soundData.get(i) != shorts[i])
                return false;
        }
        return true;
    }

    @Override
    public byte[] getData() {
        byte[] data = new byte[soundData.capacity() * 2];
        for (int i = 0; i < soundData.capacity(); i++) {
            data[i * 2] = (byte) (soundData.get(i) & 0xff);
            data[i * 2 + 1] = (byte) ((soundData.get(i) >> 8) & 0xff);
        }
        return data;
    }

    @Override
    public void freeResource() {
        soundData = null;
        format = 0;
        sampleRate = 0;
        channels = 0;
    }

    public ShortBuffer getSoundData() {
        return soundData;
    }

    public int getFormat() {
        return format;
    }

    public int getSampleRate() {
        return sampleRate;
    }

    public int getChannels() {
        return channels;
    }

}
