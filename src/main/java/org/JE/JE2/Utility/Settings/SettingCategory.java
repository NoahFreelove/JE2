package org.JE.JE2.Utility.Settings;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;

public class SettingCategory implements Serializable {
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

    public Setting<?>[] getSettings() {
        return settings.toArray(new Setting[0]);
    }

    public ArrayList<Setting<?>> getSettingsList() {
        return settings;
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

    public void addSettings(Setting<?>... settings) {
        this.settings.addAll(Arrays.asList(settings));
    }

    public void removeSetting(Setting<?> setting) {
        settings.remove(setting);
    }

    public void removeSettings(Setting<?>... settings) {
        this.settings.removeAll(Arrays.asList(settings));
    }

    public String getCategoryName() {
        return categoryName;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("Category: ").append(categoryName).append("\n");
        for (Setting<?> setting :
                settings) {
            builder.append(setting.toString()).append("\n");
        }
        builder.deleteCharAt(builder.length()-1);
        return builder.toString();
    }
}
