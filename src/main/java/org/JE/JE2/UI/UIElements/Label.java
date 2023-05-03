package org.JE.JE2.UI.UIElements;

import org.JE.JE2.Resources.DataLoader;
import org.JE.JE2.UI.Font;
import org.JE.JE2.UI.UIElements.Style.Color;
import org.JE.JE2.Window.UIHandler;
import org.JE.JE2.Window.Window;

import static org.lwjgl.nuklear.Nuklear.*;

public class Label extends UIElement {
    public int alignment = NK_TEXT_ALIGN_LEFT;
    public String text = "";
    public Font font;

    public Label() {
        font = new Font(DataLoader.getBytes("arial.ttf"), false);
    }

    public Label(String text) {
        this.text = text;
        font = new Font(DataLoader.getBytes("arial.ttf"), false);
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
        if(font.created)
        {
            nk_style_set_font(UIHandler.nuklearContext, font.getFont());
        }
        nk_label_colored(UIHandler.nuklearContext,text,alignment, style.textColor.nkColor());
    }
}
