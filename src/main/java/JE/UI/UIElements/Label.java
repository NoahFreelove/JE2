package JE.UI.UIElements;

import JE.UI.UIElements.Style.Color;
import JE.Window.UIHandler;

import static org.lwjgl.nuklear.Nuklear.*;

public class Label extends UIElement {
    public int alignment = NK_TEXT_ALIGN_LEFT;
    public String text = "";
    public Color textColor = Color.WHITE;

    public Label() {
    }

    public Label(String text) {
        this.text = text;
    }

    public Label(int alignment) {
        this.alignment = alignment;
    }

    public Label(String text,int alignment) {
        this.alignment = alignment;
        this.text = text;
    }

    @Override
    protected void render() {
        nk_label_colored(UIHandler.nuklearContext,text,alignment, textColor.nkColor());
    }
}
