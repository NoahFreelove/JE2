package org.JE.JE2.Objects.Audio;

import org.JE.JE2.Objects.Audio.Filters.SoundFilter;
import org.JE.JE2.IO.Logging.Logger;
import org.JE.JE2.Objects.Scripts.Script;
import org.JE.JE2.Resources.Bundles.AudioBundle;
import org.JE.JE2.Resources.Resource;
import org.JE.JE2.Window.Window;
import org.joml.Vector3f;
import org.lwjgl.BufferUtils;
import org.lwjgl.openal.AL10;
import org.lwjgl.openal.EXTEfx;
import org.lwjgl.stb.STBVorbis;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;

import static org.lwjgl.openal.AL10.*;
import static org.lwjgl.openal.AL11.AL_SAMPLE_OFFSET;
import static org.lwjgl.openal.ALC10.alcMakeContextCurrent;
import static org.lwjgl.system.MemoryStack.*;
import static org.lwjgl.system.MemoryStack.stackPop;

public sealed class AudioSource extends Script permits AudioSourcePlayer {
    private Resource<AudioBundle> audioResource;

    private transient boolean isPlaying = false;

    private boolean loops;

    private SoundFilter filter;

    private transient int audioBuffer;
    private transient float duration = 0;

    protected AudioSource(){
        super();
    }

    protected AudioSource(Resource<AudioBundle> bundleResource){

    }

    public AudioSource setAudio(Resource<AudioBundle> resource){
        this.audioResource = resource;
        generateAudioBuffer();
        return this;
    }

    private void generateAudioBuffer(){

        alcMakeContextCurrent(Window.audioContext());

        this.audioBuffer = alGenBuffers();
        alBufferData(audioBuffer,
                audioResource.getBundle().getFormat(),
                audioResource.getBundle().getSoundData(),
                audioResource.getBundle().getSampleRate());

        // Generate source
        audioResource.setID(alGenBuffers());
        alSourcei(audioResource.getID(), AL10.AL_BUFFER, audioBuffer);
        alSourcei(audioResource.getID(), AL10.AL_LOOPING, loops?1:0);

        alSourcei(audioResource.getID(), AL_POSITION, 0);

        setMaxGain(1);
        setGain(1);
        setFilter(new SoundFilter());
        duration = getDuration();
    }

    protected void delete(){
        alDeleteSources(audioResource.getID());
    }

    protected void playSound(){
        playAt(0);
    }

    protected void playAt(int pos){
        int state = alGetSourcei(audioResource.getID(), AL_SOURCE_STATE);

        if(state == AL_STOPPED)
        {
            isPlaying = false;
            alSourcei(audioResource.getID(), AL_POSITION, pos);
            alGetSourcei(audioResource.getID(), AL_POSITION);
        }

        if(!isPlaying){
            alSourcePlay(audioResource.getID());
            isPlaying = true;
        }
    }

    protected void stopSound(){
        if(isPlaying){
            alSourceStop(audioResource.getID());
            isPlaying = false;
        }
    }

    public boolean isPlaying() {
        int state = alGetSourcei(audioResource.getID(), AL_SOURCE_STATE);
        return state == AL_PLAYING;
    }

    public boolean loops(){
        return loops;
    }
    public void setLoops(boolean loops){
        this.loops = loops;
        alSourcei(audioResource.getID(), AL10.AL_LOOPING, loops?1:0);
    }

    public void setFilter(SoundFilter filter){
        this.filter = filter;
        filter.attachedAudioSource = this;
        updateFilter();
    }
    public void updateFilter(){
        if(filter == null) return;
        AL10.alSourcei(audioResource.getID(), EXTEfx.AL_DIRECT_FILTER, filter.filterHandle);
    }
    public SoundFilter getFilter(){return filter;}
    public void setGain(float gain){
        alSourcef(audioResource.getID(), AL_GAIN, gain);
    }
    public void setMaxGain(float maxGain){
        alSourcef(audioResource.getID(),AL_MAX_GAIN, maxGain);
    }
    public int getBufferPosition(){
        // get alError
        int error = alGetError();
        if(error != AL_NO_ERROR){
            Logger.log("Error: " + error);
        }
        return alGetSourcei(audioResource.getID(), AL_SAMPLE_OFFSET);
    }
    public void setAttribute3f(int attribute, Vector3f value)
    {
        alSource3f(audioResource.getID(), attribute, value.x, value.y, value.z);
    }
    public int getSourceSize(){
        IntBuffer size = BufferUtils.createIntBuffer(1);
        alGetBufferi(audioBuffer,AL_SIZE,size);
        return size.get();
    }

    public float getDuration(){
        return audioResource.getBundle().getSoundData().capacity() / (float)audioResource.getBundle().getSampleRate();
    }

    public float getPositionTime(){
        return (float)getBufferPosition() / (float)audioResource.getBundle().getSampleRate();
    }

    public float duration(){
        return duration;
    }

    public float getDecimal(){
        return getPositionTime() / getDuration() *2;
    }

}

class AudioProcessor {
    public static AudioBundle processAudio(String filePath)
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

    public static AudioBundle processAudio(byte[] data)
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