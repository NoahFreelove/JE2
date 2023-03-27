package org.JE.JE2.Utility.Settings.Limits;

public class FloatLimit extends SettingLimit<Float>{
    public final float maxValue;
    public final float minValue;
    public FloatLimit(){
        minValue = -Float.MAX_VALUE + 1;
        maxValue = Float.MAX_VALUE;
    }
    public FloatLimit(float minValue, float maxValue){
        this.minValue = minValue;
        this.maxValue = maxValue;
    }
    @Override
    public boolean canSet(Float value) {
        return (minValue<=value && value<=maxValue);
    }
}
