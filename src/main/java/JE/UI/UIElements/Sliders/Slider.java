package JE.UI.UIElements.Sliders;
import JE.UI.UIElements.FloatEventChanged;
import JE.UI.UIElements.UIElement;
import JE.Window.UIHandler;
import org.lwjgl.BufferUtils;

import java.nio.FloatBuffer;

import static org.lwjgl.nuklear.Nuklear.nk_slider_float;
import static org.lwjgl.nuklear.Nuklear.nnk_progress;

public class Slider extends UIElement {
    public float v = .5f;
    public float min = 0;
    public float max = 1;
    public float step = 0.1f;

    public boolean inactiveStrict = true;
    public FloatEventChanged onChange = (newValue) -> {};

    private final FloatBuffer fb = BufferUtils.createFloatBuffer(1);

    public Slider(float min, float max, float step) {
        this.min = min;
        this.max = max;
        this.step = step;
        v = (min + max)/2;
    }

    public Slider(float initialValue, float min, float max, float step) {
        this.v = initialValue;
        this.min = min;
        this.max = max;
        this.step = step;
    }

    public Slider() {
    }

    @Override
    protected void render() {
        float prev = v;
        fb.put(0,v);
        if(!isActive())
        {
            nk_slider_float(UIHandler.ctx, min, fb, max, (inactiveStrict? 0 : step));
            return;
        }
        nk_slider_float(UIHandler.ctx, min, fb, max, step);

        v = fb.get(0);
        if(v != prev)
            onChange.run(v);
    }
}
