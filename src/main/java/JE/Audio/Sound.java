package JE.Audio;

import JE.Audio.Filters.SoundFilter;
import JE.Objects.Components.Base.Component;
import JE.Resources.Resource;
import JE.Resources.ResourceType;
import org.joml.Vector3f;
import org.lwjgl.openal.AL10;
import org.lwjgl.openal.EXTEfx;

import static org.lwjgl.openal.AL10.*;

public sealed class Sound extends Component permits SoundPlayer  {
    private int bufferID;
    private int sourceID;

    private Resource audioResource;

    private boolean isPlaying = false;

    private boolean loops;

    private SoundFilter filter;

    public Sound(){
        super();
    }
    public void setAudio(String filePath){
        this.audioResource = new Resource("sound", filePath, ResourceType.SOUND);
        generateAudioBuffer();

    }
    public void setAudio(Resource resource){
        this.audioResource = resource;
        generateAudioBuffer();
    }
    private void generateAudioBuffer(){

        bufferID = AL10.alGenBuffers();

        alBufferData(bufferID, audioResource.bundle.format, audioResource.bundle.soundData, audioResource.bundle.sampleRate);

        // Generate source
        sourceID = alGenSources();
        alSourcei(sourceID, AL10.AL_BUFFER, bufferID);
        alSourcei(sourceID, AL10.AL_LOOPING, loops?1:0);

        alSourcei(sourceID, AL_POSITION, 0);

        setMaxGain(1);
        setGain(1);
        setFilter(new SoundFilter());
    }

    protected void delete(){
        alDeleteSources(sourceID);
        alDeleteBuffers(bufferID);
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
    public void setAttribute3f(int attribute, Vector3f value)
    {
        alSource3f(sourceID, attribute, value.x, value.y, value.z);
    }
}
