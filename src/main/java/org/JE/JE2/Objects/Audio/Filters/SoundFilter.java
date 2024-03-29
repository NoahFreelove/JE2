package org.JE.JE2.Objects.Audio.Filters;

import org.JE.JE2.Objects.Audio.AudioSource;
import org.lwjgl.openal.EXTEfx;

public class SoundFilter {

    public final int filterHandle;
    public AudioSource attachedAudioSource;

    public SoundFilter(){
        filterHandle = EXTEfx.alGenFilters();
    }

    public void setAttributei(int param, int value){
        EXTEfx.alFilteri(filterHandle,param,value);
    }
    public void setAttributef(int param, float value){
        EXTEfx.alFilterf(filterHandle,param,value);
    }

}
