package org.JE.JE2.Utility.Settings;

import java.util.ArrayList;
import java.util.Arrays;

public class SettingCategory {
    public ArrayList<Setting<?>> settings = new ArrayList<>();

    private String categoryName;

    public SettingCategory(String categoryName) {
        this.categoryName = categoryName;
    }

    public SettingCategory(String categoryName, Setting<?>... settings) {
        this.settings = new ArrayList<>();
        this.settings.addAll(Arrays.asList(settings));
        this.categoryName = categoryName;
    }

    public Setting<?> getSetting(String name) {
        for(Setting<?> setting : settings) {
            if(setting.getName().equals(name)) {
                return setting;
            }
        }
        return null;
    }

    public void addSetting(Setting<?> setting) {
        settings.add(setting);
    }
    public void removeSetting(Setting<?> setting) {
        settings.remove(setting);
    }

    public String getCategoryName() {
        return categoryName;
    }
}
