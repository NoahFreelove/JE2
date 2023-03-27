package org.JE.JE2.UI.UIElements.Sliders;

import org.JE.JE2.UI.UIElements.ElementEventChanged;
import org.JE.JE2.UI.UIElements.UIElement;
import org.JE.JE2.Utility.Settings.Setting;
import org.JE.JE2.Window.UIHandler;
import org.lwjgl.nuklear.NkContext;

import static org.lwjgl.nuklear.Nuklear.nk_layout_row_dynamic;
import static org.lwjgl.nuklear.Nuklear.nk_property_double;

public class DoubleSlider extends UIElement {
    private NkContext context;
    private double prevValue = 0;
    private String title = "";
    private final double[] value = new double[1];
    private double min = 0;
    private double max = 0;
    private double step = 0.01f;
    private float incPerPixel = 0.001f;
    public Setting<Double> watch = null;
    public ElementEventChanged<Double> onChanged = null;

    public DoubleSlider(double min, double value, double max, double step, double incPerPixel, String title) {
        this.context = UIHandler.nuklearContext;
        this.min = min;
        this.value[0] = value;
        this.max = max;
        this.step = step;
        this.incPerPixel = (float) incPerPixel;
        this.title = title;
    }

    public DoubleSlider(double min, double value, double max, double step, double incPerPixel, String title, Setting<Double> bindedSetting) {
        this.context = UIHandler.nuklearContext;
        this.min = min;
        this.value[0] = value;
        this.max = max;
        this.step = step;
        this.incPerPixel = (float) incPerPixel;
        this.watch = bindedSetting;
        this.title = title;
    }

    public void setValue(double value) {
        this.value[0] = value;
    }

    public double getValue() {
        return value[0];
    }

    @Override
    public void render() {
        if(watch !=null)
            value[0] = watch.getValue();
        prevValue = value[0];
        nk_layout_row_dynamic(UIHandler.nuklearContext, 32, 1);
        nk_property_double(context, title, min, value, max, step, incPerPixel);
        if(prevValue != value[0]){
            if(onChanged !=null)
                onChanged.run( value[0]);
            if(watch != null)
                watch.setValue( value[0]);
        }
    }
}
