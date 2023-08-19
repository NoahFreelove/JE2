package org.JE.JE2.Objects.Audio;

import org.JE.JE2.Annotations.StandaloneScript;
import org.JE.JE2.IO.Filepath;
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

@StandaloneScript
public sealed class AudioSource extends Script permits AudioSourcePlayer {
    private Resource<AudioBundle> audioResource;

    private transient boolean isPlaying = false;

    private boolean loops;

    private SoundFilter filter;

    private transient int audioBuffer;
    private int source;
    private transient float duration = 0;

    protected float maxDistance = 10;
    protected float fadeOutDistance = 5;
    protected float panStrength;
    protected AudioSource(){
        super();
    }

    protected AudioSource(Resource<AudioBundle> bundleResource){
        this.audioResource = bundleResource;
        generateAudioBuffer();

    }

    public AudioSource setAudio(Resource<AudioBundle> resource){
        this.audioResource = resource;
        generateAudioBuffer();
        return this;
    }

    public void generateAudioBuffer(){

        alcMakeContextCurrent(Window.audioContext());

        this.audioBuffer = alGenBuffers();
        alBufferData(audioBuffer,
                audioResource.getBundle().getFormat(),
                audioResource.getBundle().getSoundData(),
                audioResource.getBundle().getSampleRate());

        // Generate source
        source = alGenSources();
        alSourcei(source, AL10.AL_BUFFER, audioBuffer);
        alSourcei(source, AL10.AL_LOOPING, loops?1:0);

        alSourcei(source, AL_POSITION, 0);

        setMaxGain(1);
        setGain(1);
        setFilter(new SoundFilter());
        duration = getDuration();

        /*int err = alGetError();
        if(err != AL_NO_ERROR){
            Logger.log("Error: " + err);
        }*/
    }

    protected void delete(){
        alDeleteSources(audioResource.getID());
    }

    protected void playSound(){
        playAt(0);
    }

    protected void playAt(int pos){
        int state = alGetSourcei(source, AL_SOURCE_STATE);
        //System.out.println(audioResource.getID() + " " + state);

        if(state == AL_STOPPED)
        {
            isPlaying = false;
            alSourcei(source, AL_POSITION, pos);
            alGetSourcei(source, AL_POSITION);
        }

        if(!isPlaying){
            alSourcePlay(source);

            isPlaying = true;
        }
    }

    protected void stopSound(){
        if(isPlaying){
            alSourceStop(source);
            isPlaying = false;
        }
    }

    public boolean isPlaying() {
        int state = alGetSourcei(source, AL_SOURCE_STATE);
        return state == AL_PLAYING;
    }

    public boolean loops(){
        return loops;
    }
    public void setLoops(boolean loops){
        this.loops = loops;
        alSourcei(source, AL10.AL_LOOPING, loops?1:0);
    }

    public void setFilter(SoundFilter filter){
        this.filter = filter;
        filter.attachedAudioSource = this;
        updateFilter();
    }
    public void updateFilter(){
        if(filter == null) return;
        AL10.alSourcei(source, EXTEfx.AL_DIRECT_FILTER, filter.filterHandle);
    }
    public SoundFilter getFilter(){return filter;}
    public void removeFilter(){
        setFilter(new SoundFilter());
    }
    public void setGain(float gain){
        alSourcef(source, AL_GAIN, gain);
    }
    public void setMaxGain(float maxGain){
        alSourcef(source,AL_MAX_GAIN, maxGain);
    }

    public void setReferenceDistance(float distance){
        fadeOutDistance = distance;
    }

    public void setPanStrength(float factor){
        panStrength = factor;
        alSourcef(source, AL_ROLLOFF_FACTOR, factor);
    }

    public void setMaxDistance(float distance){
        maxDistance = distance;
    }

    public void setPositionWorld(float x, float y){
        if(x > 1 || y > 1 || x < -1 || y < -1){
            Logger.log("Audio Warning For Clip <" + getAudioResource().getName() + ">: " +
                    "setPositionWorld position is out of bounds. Must be between -1 and 1", Logger.WARN);
        }
        alSource3f(source, AL_POSITION, x, y, 0);
    }

    public int getBufferPosition(){
        /*// get alError
        int error = alGetError();
        if(error != AL_NO_ERROR){
            Logger.log("Error: " + error);
        }*/
        return alGetSourcei(source, AL_SAMPLE_OFFSET);
    }
    public void setAttribute3f(int attribute, Vector3f value)
    {
        alSource3f(source, attribute, value.x, value.y, value.z);
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

    public Resource<AudioBundle> getAudioResource(){
        return audioResource;
    }

    protected static class AudioProcessor {
        public static AudioBundle processAudio(Filepath filePath)
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

            ShortBuffer rawAudioBuffer = STBVorbis.stb_vorbis_decode_filename(filePath.getPath(false), channelsBuffer, sampleRateBuffer);
            if(rawAudioBuffer == null){
                Logger.log("Error loading sound file: " + filePath.getPath(false));
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

            String data = "Sound loaded: " + filePath.getPath(false) + "\n" +
                    "Channels: " + channels + "\n" +
                    "Sample Rate: " + sampleRate + "\n" +
                    "Format: " + format + "\n" +
                    "Buffer: " + rawAudioBuffer + "\n" +
                    "Buffer Size: " + rawAudioBuffer.capacity() + "\n";

            Logger.log(data, Logger.DEBUG);

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
}

