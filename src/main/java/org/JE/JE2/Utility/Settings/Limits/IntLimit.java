package org.JE.JE2.Utility.Settings.Limits;

public class IntLimit extends SettingLimit<Integer>{
    public final int maxValue;
    public final int minValue;

    public IntLimit(){
        this.minValue = Integer.MIN_VALUE;
        this.maxValue = Integer.MAX_VALUE;
    }
    public IntLimit(int minValue, int maxValue){
        this.minValue = minValue;
        this.maxValue = maxValue;
    }
    @Override
    public boolean canSet(Integer value) {
        return (minValue<=value && value<=maxValue);
    }
}
