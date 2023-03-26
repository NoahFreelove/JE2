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
