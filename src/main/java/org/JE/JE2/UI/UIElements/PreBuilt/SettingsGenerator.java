package org.JE.JE2.UI.UIElements.PreBuilt;

import org.JE.JE2.UI.UIElements.Checkbox;
import org.JE.JE2.UI.UIElements.Group;
import org.JE.JE2.UI.UIElements.Label;
import org.JE.JE2.UI.UIElements.Sliders.DoubleSlider;
import org.JE.JE2.UI.UIElements.Sliders.FloatSlider;
import org.JE.JE2.UI.UIElements.Sliders.IntSlider;
import org.JE.JE2.UI.UIElements.TextField;
import org.JE.JE2.Utility.Settings.Limits.DoubleLimit;
import org.JE.JE2.Utility.Settings.Limits.FloatLimit;
import org.JE.JE2.Utility.Settings.Limits.IntLimit;
import org.JE.JE2.Utility.Settings.Limits.StringLimit;
import org.JE.JE2.Utility.Settings.Setting;
import org.JE.JE2.Utility.Settings.SettingCategory;
import org.JE.JE2.Utility.Settings.SettingManager;

public class SettingsGenerator {

    public static Group generateSettingsUI(SettingManager manager){
        Group g = new Group();

        for (SettingCategory cat : manager.getCategories()) {
            g.addElement(new Label(cat.getCategoryName()));

            for (Setting<?> setting: cat.getSettings()) {
                if(setting.getValue() instanceof String v){
                    generateStringUI(g, setting, v);
                }
                else if(setting.getValue() instanceof Integer i){
                    generateIntUI(g, setting, i);
                }
                else if(setting.getValue() instanceof Float f){
                    generateFloatUI(g, setting, f);
                }
                else if(setting.getValue() instanceof Double d){
                    generateDoubleUI(g, setting, d);
                }
                else if(setting.getValue() instanceof Boolean b){
                    generateBooleanUI(g, setting, b);
                }
            }
        }
        return g;
    }

    private static void generateBooleanUI(Group g, Setting<?> setting, Boolean b) {
        g.addElement(new Checkbox(b, setting.getName(), (Setting<Boolean>) setting));
    }

    private static void generateDoubleUI(Group g, Setting<?> setting, Double d) {
        if(setting.getLimit() instanceof DoubleLimit lim){
            g.addElement(new DoubleSlider(lim.minValue, d, lim.maxValue,0.1,0.1, setting.getName(),(Setting<Double>) setting));
        }
        else{
            g.addElement(new DoubleSlider(-Double.MAX_VALUE+1, d, Double.MAX_VALUE,0.5, 0.1, setting.getName(),(Setting<Double>) setting));
        }
    }

    private static void generateFloatUI(Group g, Setting<?> setting, Float f) {
        if(setting.getLimit() instanceof FloatLimit lim){
            g.addElement(new FloatSlider(lim.minValue, f, lim.maxValue,0.1f,0.1f, setting.getName(), (Setting<Float>) setting));
        }
        else{
            g.addElement(new FloatSlider(-Float.MAX_VALUE+1, f, Float.MAX_VALUE,0.5f, 0.1f, setting.getName(),(Setting<Float>) setting));
        }
    }

    private static void generateIntUI(Group g, Setting<?> setting, Integer i) {
        if(setting.getLimit() instanceof IntLimit lim){
            g.addElement(new IntSlider(lim.minValue, i, lim.maxValue, setting.getName(), (Setting<Integer>) setting));
        }
        else{
            g.addElement(new IntSlider(Integer.MIN_VALUE, i, Integer.MAX_VALUE, setting.getName(), (Setting<Integer>) setting));
        }
    }

    private static void generateStringUI(Group g, Setting<?> setting, String v) {
        if(setting.getLimit() instanceof StringLimit lim){
            g.addElement(new TextField(lim.maxChars,32, v, setting.getName(), (Setting<String>) setting));
        }
        else{
            g.addElement(new TextField(128,32, v));

        }
    }
}
