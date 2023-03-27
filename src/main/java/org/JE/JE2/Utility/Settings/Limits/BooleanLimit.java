package org.JE.JE2.Utility.Settings.Limits;

public class BooleanLimit extends SettingLimit<Boolean>{
    public final int mode;
    public BooleanLimit(){
        mode = 0;
    }
    public BooleanLimit(int mode){
        this.mode = mode;
    }

    @Override
    public boolean canSet(Boolean value) {
        if(mode == 0)
            return true;

        if(mode == 1 && !value)
            return true;
        return mode == 2 && value;
    }
}
