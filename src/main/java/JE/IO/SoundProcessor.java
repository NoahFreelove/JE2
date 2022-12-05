package JE.IO;

import JE.Resources.ResourceBundle;
import JE.Window.Window;
import org.lwjgl.openal.AL10;
import org.lwjgl.stb.STBVorbis;

import java.nio.IntBuffer;
import java.nio.ShortBuffer;

import static org.lwjgl.system.MemoryStack.*;
import static org.lwjgl.system.MemoryStack.stackPop;

public class SoundProcessor {
    public static ResourceBundle ProcessSound(String filePath)
    {
        ResourceBundle bundle = new ResourceBundle();
        if(Window.audioContext() == -1)
            Window.CreateOpenAL();

        stackPush();
        IntBuffer channelsBuffer = stackMallocInt(1);
        stackPush();
        IntBuffer sampleRateBuffer = stackMallocInt(1);

        ShortBuffer rawAudioBuffer = STBVorbis.stb_vorbis_decode_filename(filePath, channelsBuffer, sampleRateBuffer);

        if(rawAudioBuffer == null){
            System.out.println("Error loading sound file: " + filePath);
            stackPop();
            stackPop();
            return bundle;
        }

        int channels = channelsBuffer.get(0);
        int sampleRate = sampleRateBuffer.get(0);

        stackPop();
        stackPop();

        //Find correct format
        int format = -1;
        if(channels == 1){
            format = AL10.AL_FORMAT_MONO16;
        }else if(channels == 2){
            format = AL10.AL_FORMAT_STEREO16;
        }
        bundle.format = format;
        bundle.sampleRate = sampleRate;
        bundle.channels = channels;
        bundle.soundData = rawAudioBuffer;
        return bundle;
    }
}
