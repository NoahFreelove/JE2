package org.JE.JE2.UI.UIElements.Buttons;

import org.JE.JE2.UI.UIElements.UIElement;
import org.JE.JE2.Window.UIHandler;
import org.lwjgl.nuklear.NkStyleButton;

import static org.lwjgl.nuklear.Nuklear.nk_button_label;

public class Button extends UIElement {
    public String text = "text";
    public Runnable onClickEvent = () -> {};
    NkStyleButton button = UIHandler.nuklearContext.style().button();

    public Button() {
    }

    public Button(String text) {
        this.text = text;
    }

    public Button(String text, Runnable onClickEvent) {
        this.text = text;
        this.onClickEvent = onClickEvent;
    }

    @Override
    protected void render() {
        button.draw_begin();
        button.text_normal().set(style.textColor.nkColor());
        button.text_hover().set(style.textColor.nkColor());
        button.text_active().set(style.textColor.nkColor());

        if(isActive()){
            button.normal().data().color().set(style.normalColor.nkColor());
            button.hover().data().color().set(style.hoverColor.nkColor());
            button.active().data().color().set(style.pressedColor.nkColor());
        }
        else{
            button.normal().data().color().set(style.inactiveColor.nkColor());
            button.hover().data().color().set(style.inactiveColor.nkColor());
            button.active().data().color().set(style.inactiveColor.nkColor());
        }

        if(nk_button_label(UIHandler.nuklearContext, text)){
            if(!isActive())
                return;
            onClickEvent.run();
        }

        button.draw_end();

    }
}

