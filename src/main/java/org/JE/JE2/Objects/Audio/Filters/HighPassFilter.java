package org.JE.JE2.Objects.Audio.Filters;

import static org.lwjgl.openal.EXTEfx.*;

public class HighPassFilter extends IntensityFilter{

    public HighPassFilter(){
        super();
        setAttributei(AL_FILTER_TYPE, AL_FILTER_HIGHPASS);
        setAttributef(AL_HIGHPASS_GAIN, 25f);
    }
    public HighPassFilter(float intensity){
        super(intensity);
        setAttributei(AL_FILTER_TYPE, AL_FILTER_HIGHPASS);
        setAttributef(AL_HIGHPASS_GAIN, 25f);
    }

    @Override
    protected void intensityUpdate(float newIntensity) {
        setAttributef(AL_HIGHPASS_GAINLF, 1-intensity);
    }
}
