package JE.UI.UIElements.Sliders;

import JE.UI.UIElements.Style.StyleInfo;
import JE.Window.UIHandler;
import org.lwjgl.nuklear.NkStyleSlider;

public class StyledSlider extends Slider{
    NkStyleSlider slider = UIHandler.ctx.style().slider();
    public StyleInfo style = new StyleInfo();

    public StyledSlider() {
    }
    public StyledSlider(StyleInfo style) {
        this.style = style;
    }

    public StyledSlider(float min, float max, float step) {
        super(min, max, step);
    }
    public StyledSlider(float min, float max, float step, StyleInfo style) {
        super(min, max, step);
        this.style = style;
    }

    public StyledSlider(float initialValue, float min, float max, float step, StyleInfo style) {
        super(initialValue, min, max, step);
        this.style = style;
    }

    public StyledSlider(float initialValue, float min, float max, float step) {
        super(initialValue, min, max, step);
    }

    @Override
    protected void render() {
        slider.draw_begin();
        if(isActive()){
            slider.bar_filled(style.pressedColor.nkColor());
            slider.bar_hover(style.hoverColor.nkColor());
            slider.bar_normal(style.normalColor.nkColor());
        }
        else{
            slider.bar_filled(style.inactiveColor.nkColor());
            slider.bar_hover(style.inactiveColor.nkColor());
            slider.bar_normal(style.inactiveColor.nkColor());
        }
        super.render();
        slider.draw_end();
    }
}
