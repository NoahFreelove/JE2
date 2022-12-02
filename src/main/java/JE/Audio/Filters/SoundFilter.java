package JE.Audio.Filters;

import org.lwjgl.openal.EXTEfx;

public class SoundFilter {

    public final int filterHandle;

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
