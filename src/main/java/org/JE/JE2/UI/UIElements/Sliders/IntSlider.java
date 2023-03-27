package org.JE.JE2.UI.UIElements.Sliders;

import org.JE.JE2.UI.UIElements.ElementEventChanged;
import org.JE.JE2.UI.UIElements.UIElement;
import org.JE.JE2.Utility.Settings.Setting;
import org.JE.JE2.Window.UIHandler;

import static org.lwjgl.nuklear.Nuklear.nk_layout_row_dynamic;
import static org.lwjgl.nuklear.Nuklear.nk_property_int;

public class IntSlider extends UIElement {
    private final int[] value = new int[1];
    private String title = "";
    private int prevValue = 0;
    private int min = 0;
    private int max = 0;
    private int incPerPixel = 1;
    public ElementEventChanged<Integer> onChanged = null;
    public Setting<Integer> watch = null;

    public IntSlider(int min, int initValue, int max, String title) {
        this.min = min;
        this.max = max;
        this.value[0] = initValue;
        prevValue = initValue;
        this.title = title;
    }

    public IntSlider(int min, int initValue, int max, String title, Setting<Integer> bindedSetting) {
        this.min = min;
        this.max = max;
        this.value[0] = initValue;
        prevValue = initValue;
        this.watch = bindedSetting;
        this.title = title;
    }

    public void setValue(int value) {
        this.value[0] = value;
    }

    public int getValue() {
        return value[0];
    }

    @Override
    protected void render() {
        if(watch != null)
        {
            value[0] = watch.getValue();
        }
        prevValue = value[0];
        nk_layout_row_dynamic(UIHandler.nuklearContext, 32, 1);
        nk_property_int(UIHandler.nuklearContext, title, min, value, max, 1, incPerPixel);

        if(prevValue != value[0]){
            if(onChanged !=null)
            {
                onChanged.run(value[0]);
            }
            if(watch !=null)
                watch.setValue(value[0]);
        }
    }
}
