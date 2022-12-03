package JE.Audio;

import JE.Audio.Filters.SoundFilter;
import JE.Window.Window;
import org.lwjgl.openal.AL10;
import org.lwjgl.openal.EXTEfx;
import org.lwjgl.stb.STBVorbis;

import java.nio.IntBuffer;
import java.nio.ShortBuffer;

import static org.lwjgl.openal.AL10.*;
import static org.lwjgl.system.MemoryStack.*;
import static org.lwjgl.system.libc.LibCStdlib.free;

public class Sound {
    private int bufferID;
    private int sourceID;

    private final String filepath;

    private boolean isPlaying = false;

    private boolean loops;

    private SoundFilter filter;

    public Sound(String filePath, boolean loops){
        this.filepath = filePath;
        this.loops = loops;

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
            return;
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

        bufferID = AL10.alGenBuffers();

        alBufferData(bufferID, format, rawAudioBuffer, sampleRate);

        // Generate source
        sourceID = alGenSources();
        alSourcei(sourceID, AL10.AL_BUFFER, bufferID);
        alSourcei(sourceID, AL10.AL_LOOPING, loops?1:0);

        alSourcei(sourceID, AL_POSITION, 0);

        setMaxGain(1);
        setGain(1);
        setFilter(new SoundFilter());

        free(rawAudioBuffer);
    }

    public void delete(){
        alDeleteSources(sourceID);
        alDeleteBuffers(bufferID);
    }

    public void play(){
        playAt(0);
    }
    public void playAt(int pos){
        int state = alGetSourcei(sourceID, AL_SOURCE_STATE);

        if(state == AL_STOPPED)
        {
            isPlaying = false;
            alSourcei(sourceID, AL_POSITION, pos);
            alGetSourcei(sourceID, AL_POSITION);

        }

        if(!isPlaying){
            alSourcePlay(sourceID);
            isPlaying = true;
        }
    }

    public void stop(){
        if(isPlaying){
            alSourceStop(sourceID);
            isPlaying = false;
        }
    }

    public String getFilepath() {
        return filepath;
    }

    public boolean isPlaying() {
        int state = alGetSourcei(sourceID, AL_SOURCE_STATE);
        return state == AL_PLAYING;
    }

    public boolean loops(){
        return loops;
    }
    public void setLoops(boolean loops){
        this.loops = loops;
        alSourcei(sourceID, AL10.AL_LOOPING, loops?1:0);
    }

    public void setFilter(SoundFilter filter){
        this.filter = filter;
        filter.attachedSound = this;
        updateFilter();
    }
    public void updateFilter(){
        if(filter == null) return;
        AL10.alSourcei(sourceID, EXTEfx.AL_DIRECT_FILTER, filter.filterHandle);
    }
    public SoundFilter getFilter(){return filter;}
    public void setGain(float gain){
        alSourcef(sourceID, AL_GAIN, gain);
    }
    public void setMaxGain(float maxGain){
        alSourcef(sourceID,AL_MAX_GAIN, maxGain);
    }
    public int getPosition(){
        return alGetSourcei(sourceID, AL_POSITION);
    }
}
