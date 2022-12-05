package JE.Audio.Filters;

import JE.Audio.Sound;
import org.joml.Vector3f;
import org.lwjgl.openal.EXTEfx;

public class SoundFilter {

    public final int filterHandle;
    public Sound attachedSound;

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
