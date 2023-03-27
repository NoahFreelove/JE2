package org.JE.JE2.UI.UIElements;

import org.JE.JE2.UI.UIElements.ElementEventChanged;
import org.JE.JE2.UI.UIElements.UIElement;
import org.JE.JE2.Utility.Settings.Setting;
import org.JE.JE2.Window.UIHandler;

import org.lwjgl.BufferUtils;
import org.lwjgl.nuklear.NkStyleToggle;

import java.nio.ByteBuffer;

import static org.lwjgl.nuklear.Nuklear.nk_checkbox_label;

public class Checkbox extends UIElement {
    public boolean isChecked = false;
    public String label = "Checkbox";
    private final ByteBuffer bb = BufferUtils.createByteBuffer(1);
    public ElementEventChanged<Boolean> onChange = null;
    public Setting<Boolean> watch = null;
    NkStyleToggle toggle = UIHandler.nuklearContext.style().checkbox();


    public Checkbox() {
    }

    public Checkbox(boolean isChecked) {
        this.isChecked = isChecked;
    }
    public Checkbox(boolean isChecked, String label, Setting<Boolean> bindedSetting) {
        this.isChecked = isChecked;
        this.watch = bindedSetting;
        this.label = label;
    }

    public Checkbox(boolean isChecked, String label) {
        this.isChecked = isChecked;
        this.label = label;
    }

    public Checkbox(boolean isChecked, String label, ElementEventChanged<Boolean> onChange) {
        this.isChecked = isChecked;
        this.label = label;
        this.onChange = onChange;
    }

    @Override
    protected void render() {
        if(watch !=null)
            isChecked = watch.getValue();
        boolean prev = isChecked;
        bb.put(0,(byte) (isChecked? 1 : 0));

        draw();

        if(!isActive())
            return;
        isChecked = ((int)(bb.get(0)) == 1);
        if(prev!=isChecked)
        {
            if(onChange != null)
                onChange.run(isChecked);
            if(watch !=null)
                watch.setValue(isChecked);
        }
    }

    private void draw() {
        toggle.draw_begin();
        if(isChecked){
            toggle.active().data().color().set(style.pressedColor.nkColor());
            toggle.normal().data().color().set(style.pressedColor.nkColor());
            toggle.hover().data().color().set(style.hoverColor.nkColor());
        }
        else {
            toggle.active().data().color().set(style.inactiveColor.nkColor());
            toggle.normal().data().color().set(style.inactiveColor.nkColor());
            toggle.hover().data().color().set(style.inactiveColor.nkColor());
        }
        nk_checkbox_label(UIHandler.nuklearContext, label, bb);
        toggle.draw_end();
    }
}
