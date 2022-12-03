package JE.Audio.Filters;

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
        if(attachedSound !=null)
        {
            attachedSound.updateFilter();
        }

    }

    protected abstract void intensityUpdate(float newIntensity);
}
