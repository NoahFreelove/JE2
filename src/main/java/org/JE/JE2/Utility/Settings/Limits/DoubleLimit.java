package org.JE.JE2.Utility.Settings.Limits;

public class DoubleLimit extends SettingLimit<Double> {
    public final double maxValue;
    public final double minValue;
    public DoubleLimit(){
        minValue = -Double.MAX_VALUE + 1;
        maxValue = Double.MAX_VALUE;
    }
    public DoubleLimit(double minValue, double maxValue){
        this.minValue = minValue;
        this.maxValue = maxValue;
    }
    @Override
    public boolean canSet(Double value) {
        return (minValue<=value && value<=maxValue);
    }
}
