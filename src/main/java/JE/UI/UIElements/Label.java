package JE.UI.UIElements;

import JE.Window.UIHandler;

import static org.lwjgl.nuklear.Nuklear.NK_TEXT_ALIGN_LEFT;
import static org.lwjgl.nuklear.Nuklear.nk_label;

public class Label extends Element_UI{
    public int alignment = NK_TEXT_ALIGN_LEFT;
    public String text = "";

    public Label() {
    }

    public Label(String text) {
        this.text = text;
    }

    public Label(int alignment) {
        this.alignment = alignment;
    }

    public Label(int alignment, String text) {
        this.alignment = alignment;
        this.text = text;
    }

    @Override
    protected void render() {
        nk_label(UIHandler.ctx,text,alignment);
    }
}
