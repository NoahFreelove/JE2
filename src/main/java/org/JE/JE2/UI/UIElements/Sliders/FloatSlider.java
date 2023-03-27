package org.JE.JE2.UI.UIElements.Sliders;

import org.JE.JE2.UI.UIElements.ElementEventChanged;
import org.JE.JE2.UI.UIElements.UIElement;
import org.JE.JE2.Utility.Settings.Setting;
import org.JE.JE2.Window.UIHandler;
import org.lwjgl.nuklear.NkContext;

import static org.lwjgl.nuklear.Nuklear.nk_layout_row_dynamic;
import static org.lwjgl.nuklear.Nuklear.nk_property_float;

public class FloatSlider extends UIElement {
    private NkContext context;
    private float prevValue = 0;
    private String title = "";

    private final float[] value = new float[1];
    private float min = 0;
    private float max = 0;
    private float step = 0.01f;
    private float incPerPixel = 0.001f;
    public Setting<Float> watch = null;
    public ElementEventChanged<Float> onChanged = null;

    public FloatSlider(float min, float value, float max, float step, float incPerPixel, String title) {
        this.context = UIHandler.nuklearContext;
        this.min = min;
        this.value[0] = value;
        this.max = max;
        this.step = step;
        this.incPerPixel = incPerPixel;
        this.title = title;
    }

    public FloatSlider(float min, float value, float max, float step, float incPerPixel, String title, Setting<Float> bindedSetting) {
        this.context = UIHandler.nuklearContext;
        this.min = min;
        this.value[0] = value;
        this.max = max;
        this.step = step;
        this.incPerPixel = incPerPixel;
        this.watch = bindedSetting;
        this.title = title;
    }

    public void setValue(float value) {
        this.value[0] = value;
    }

    public float getValue() {
        return value[0];
    }

    @Override
    public void render() {
        if(watch !=null)
            value[0] = watch.getValue();
        prevValue = value[0];
        nk_layout_row_dynamic(UIHandler.nuklearContext, 32, 1);
        nk_property_float(context, title, min, value, max, step, incPerPixel);
        if(prevValue != value[0]){
            if(onChanged !=null)
                onChanged.run(value[0]);
            if(watch != null)
                watch.setValue(value[0]);
        }
    }
}
