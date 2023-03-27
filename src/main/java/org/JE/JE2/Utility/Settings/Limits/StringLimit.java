package org.JE.JE2.Utility.Settings.Limits;

public class StringLimit extends SettingLimit<String> {
    public final int maxChars;
    public StringLimit(){
        this.maxChars = 128;
    }
    public StringLimit(int maxChars)
    {
        this.maxChars = maxChars;
    }

    @Override
    public boolean canSet(String value) {
        return value.length() < maxChars;
    }
}
