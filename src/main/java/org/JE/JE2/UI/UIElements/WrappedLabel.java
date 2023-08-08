package org.JE.JE2.UI.UIElements;

import org.JE.JE2.Window.UIHandler;
import org.lwjgl.nuklear.NkContext;
import org.lwjgl.nuklear.NkRect;

import static org.lwjgl.nuklear.Nuklear.*;

public class WrappedLabel extends UIElement{
    public int alignment = NK_TEXT_ALIGN_LEFT;
    protected String text = "Label";

    public WrappedLabel() {

    }

    public WrappedLabel(String text) {
        this.text = text;
    }

    public WrappedLabel(int alignment) {
        this.alignment = alignment;
    }

    public WrappedLabel(String text,int alignment) {
        this.alignment = alignment;
        this.text = text;
    }

    public void setText(String text) {
        this.text = text;
    }
    public String getText(){
        return text;
    }

    @Override
    protected void render() {
        if(style.font.created)
        {
            nk_style_set_font(UIHandler.nuklearContext, style.font.getFont());
        }
        nk_label_colored_wrap(UIHandler.nuklearContext,text,style.textColor.nkColor());
    }
}
