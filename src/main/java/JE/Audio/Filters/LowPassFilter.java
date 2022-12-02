package JE.Audio.Filters;

import org.lwjgl.openal.EXTEfx;

import static org.lwjgl.openal.EXTEfx.*;

public class LowPassFilter extends SoundFilter{
    public LowPassFilter(){
        super();
        setAttributei(AL_FILTER_TYPE, EXTEfx.AL_FILTER_LOWPASS);
        setAttributef(AL_LOWPASS_GAIN, 25f);
        setAttributef(AL_LOWPASS_GAINHF, 0f);
    }
}
