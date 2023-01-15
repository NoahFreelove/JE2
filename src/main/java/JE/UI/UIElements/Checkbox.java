package JE.UI.UIElements;

import JE.Window.UIHandler;

import org.lwjgl.BufferUtils;

import java.nio.ByteBuffer;

import static org.lwjgl.nuklear.Nuklear.nk_checkbox_label;

public class Checkbox extends UIElement {
    public boolean isChecked = false;
    public String label = "Checkbox";
    private final ByteBuffer bb = BufferUtils.createByteBuffer(1);
    public BooleanEventChanged onChange = value -> {};

    public Checkbox() {
    }

    public Checkbox(boolean isChecked) {
        this.isChecked = isChecked;
    }

    public Checkbox(boolean isChecked, String label) {
        this.isChecked = isChecked;
        this.label = label;
    }

    public Checkbox(boolean isChecked, String label, BooleanEventChanged onChange) {
        this.isChecked = isChecked;
        this.label = label;
        this.onChange = onChange;
    }

    @Override
    protected void render() {
        boolean prev = isChecked;
        bb.put(0,(byte) (isChecked? 1 : 0));
        nk_checkbox_label(UIHandler.ctx, label, bb);
        isChecked = ((int)(bb.get(0)) == 1);
        if(prev!=isChecked)
            onChange.run(isChecked);

    }
}
