package org.JE.JE2.Objects.Audio.Filters;

import org.lwjgl.openal.EXTEfx;

import static org.lwjgl.openal.EXTEfx.*;

public class LowPassFilter extends IntensityFilter{

    public LowPassFilter(){
        super();
        setAttributei(AL_FILTER_TYPE, EXTEfx.AL_FILTER_LOWPASS);
        setAttributef(AL_LOWPASS_GAIN, 25f);
    }
    public LowPassFilter(float intensity){
        super(intensity);
        setAttributei(AL_FILTER_TYPE, EXTEfx.AL_FILTER_LOWPASS);
        setAttributef(AL_LOWPASS_GAIN, 25f);
    }

    @Override
    protected void intensityUpdate(float newIntensity) {
        setAttributef(AL_LOWPASS_GAINHF, 1-intensity);
    }
}
