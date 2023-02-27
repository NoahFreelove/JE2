package org.JE.JE2.Objects.Audio.Filters;

public abstract class IntensityFilter extends SoundFilter {
    protected float intensity = 1f;

    public IntensityFilter(){
        setIntensity(1);
    }
    public IntensityFilter(float intensity){
        setIntensity(intensity);
    }

    protected float simpleClamp(float f){
        if(f< 0)
            return 0;
        return Math.min(f, 1);
    }

    public float getIntensity(){return intensity;}

    public void setIntensity(float intensity){
        this.intensity = simpleClamp(intensity);
        intensityUpdate(intensity);
        if(attachedAudioSource !=null)
        {
            attachedAudioSource.updateFilter();
        }

    }

    protected abstract void intensityUpdate(float newIntensity);
}
