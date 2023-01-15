package JE.Audio;

import JE.Audio.Filters.SoundFilter;
import JE.Logging.Logger;
import JE.Objects.Components.Base.Component;
import JE.Resources.Resource;
import JE.Resources.ResourceType;
import JE.Window.Window;
import org.joml.Vector3f;
import org.lwjgl.BufferUtils;
import org.lwjgl.openal.AL10;
import org.lwjgl.openal.EXTEfx;

import java.nio.IntBuffer;

import static org.lwjgl.openal.AL10.*;
import static org.lwjgl.openal.AL11.AL_SAMPLE_OFFSET;

public sealed class AudioSource extends Component permits AudioSourcePlayer {
    private int sourceID;

    private Resource audioResource;

    private boolean isPlaying = false;

    private boolean loops;

    private SoundFilter filter;

    public AudioSource(){
        super();
    }
    public AudioSource setAudio(String filePath){
        this.audioResource = new Resource("sound", filePath, ResourceType.SOUND);

        generateAudioBuffer();
        return this;
    }
    public AudioSource setAudio(Resource resource){
        this.audioResource = resource;
        generateAudioBuffer();
        return this;
    }
    private void generateAudioBuffer(){


        alBufferData(Window.audioBuffer, audioResource.bundle.format, audioResource.bundle.soundData, audioResource.bundle.sampleRate);

        // Generate source
        sourceID = alGenSources();
        Logger.log("Source:" + sourceID);
        alSourcei(sourceID, AL10.AL_BUFFER, Window.audioBuffer);
        alSourcei(sourceID, AL10.AL_LOOPING, loops?1:0);

        alSourcei(sourceID, AL_POSITION, 0);

        setMaxGain(1);
        setGain(1);
        setFilter(new SoundFilter());

    }

    protected void delete(){
        alDeleteSources(sourceID);
    }

    protected void playSound(){
        playAt(0);
    }
    protected void playAt(int pos){
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
        audioResource.bundle.isPlaying = isPlaying;

    }

    protected void stopSound(){
        if(isPlaying){
            alSourceStop(sourceID);
            isPlaying = false;
        }
        audioResource.bundle.isPlaying = isPlaying;
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
        filter.attachedAudioSource = this;
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
    public int getBufferPosition(){
        return alGetSourcei(sourceID, AL_SAMPLE_OFFSET);
    }
    public void setAttribute3f(int attribute, Vector3f value)
    {
        alSource3f(sourceID, attribute, value.x, value.y, value.z);
    }
    public int getSourceSize(){
        IntBuffer size = BufferUtils.createIntBuffer(1);
        alGetBufferi(Window.audioBuffer,AL_SIZE,size);
        return size.get();
    }

    public float getDuration(){
        IntBuffer ib = BufferUtils.createIntBuffer(1);
        alGetBufferi(Window.audioBuffer,AL_SIZE,ib);
        return (float)ib.get()/ (float)audioResource.bundle.sampleRate;
    }
    public float getPositionTime(){

        return (float)getBufferPosition() / (float)audioResource.bundle.sampleRate;
    }
    public float getDecimal(){
        return getPositionTime() / getDuration();
    }
}
