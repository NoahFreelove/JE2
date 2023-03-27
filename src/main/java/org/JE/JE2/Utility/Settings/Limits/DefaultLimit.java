package org.JE.JE2.Utility.Settings.Limits;

public class DefaultLimit<T> extends SettingLimit<T>{
    @Override
    public boolean canSet(T value) {
        return true;
    }
}
