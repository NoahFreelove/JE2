package org.JE.JE2.IO.FileInput;

import org.JE.JE2.IO.Logging.Logger;
import org.JE.JE2.Resources.Bundles.AudioBundle;
import org.JE.JE2.Resources.Bundles.ResourceBundle;
import org.JE.JE2.Window.Window;
import org.lwjgl.BufferUtils;
import org.lwjgl.openal.AL10;
import org.lwjgl.stb.STBVorbis;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;

import static org.lwjgl.system.MemoryStack.*;
import static org.lwjgl.system.MemoryStack.stackPop;

public class AudioProcessor {
    public static AudioBundle ProcessSound(String filePath)
    {
        ShortBuffer soundData;
        int format;
        int sampleRate;
        int channels;

        if(Window.audioContext() == -1)
            Window.CreateOpenAL();

        stackPush();
        IntBuffer channelsBuffer = stackMallocInt(1);
        stackPush();
        IntBuffer sampleRateBuffer = stackMallocInt(1);

        ShortBuffer rawAudioBuffer = STBVorbis.stb_vorbis_decode_filename(filePath, channelsBuffer, sampleRateBuffer);
        if(rawAudioBuffer == null){
            Logger.log("Error loading sound file: " + filePath);
            stackPop();
            stackPop();
            return new AudioBundle();
        }

        channels = channelsBuffer.get(0);
        sampleRate = sampleRateBuffer.get(0);

        stackPop();
        stackPop();

        //Find correct format
        format = -1;
        if(channels == 1){
            format = AL10.AL_FORMAT_MONO16;
        }else if(channels == 2){
            format = AL10.AL_FORMAT_STEREO16;
        }

        soundData = rawAudioBuffer;

        /*System.out.println("Sound loaded: " + filePath);
        System.out.println("Channels: " + channels);
        System.out.println("Sample Rate: " + sampleRate);
        System.out.println("Format: " + format);
        System.out.println("Buffer: " + rawAudioBuffer);
        System.out.println("Buffer Size: " + rawAudioBuffer.capacity());*/
        return new AudioBundle(soundData,format,sampleRate,channels);
    }

    public static AudioBundle ProcessSound(byte[] data)
    {
        ShortBuffer soundData;
        int format;
        int sampleRate;
        int channels;
        if(Window.audioContext() == -1)
            Window.CreateOpenAL();

        stackPush();
        IntBuffer channelsBuffer = stackMallocInt(1);
        stackPush();
        IntBuffer sampleRateBuffer = stackMallocInt(1);

        ByteBuffer putData = BufferUtils.createByteBuffer(data.length);
        putData.put(data);
        putData.flip();
        ShortBuffer rawAudioBuffer = STBVorbis.stb_vorbis_decode_memory(putData, channelsBuffer, sampleRateBuffer);
        if(rawAudioBuffer == null){
            Logger.log("Error loading sound bytes size: " + data.length);
            stackPop();
            stackPop();
            return new AudioBundle();
        }

        channels = channelsBuffer.get(0);
        sampleRate = sampleRateBuffer.get(0);

        stackPop();
        stackPop();

        //Find correct format
        format = -1;
        if(channels == 1){
            format = AL10.AL_FORMAT_MONO16;
        }else if(channels == 2){
            format = AL10.AL_FORMAT_STEREO16;
        }

        soundData = rawAudioBuffer;

        /*System.out.println("Sound loaded: " + filePath);
        System.out.println("Channels: " + channels);
        System.out.println("Sample Rate: " + sampleRate);
        System.out.println("Format: " + format);
        System.out.println("Buffer: " + rawAudioBuffer);
        System.out.println("Buffer Size: " + rawAudioBuffer.capacity());*/
        return new AudioBundle(soundData,format,sampleRate,channels);
    }
}
