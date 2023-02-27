package org.JE.JE2.UI.UIElements.Buttons;

import org.JE.JE2.UI.UIElements.UIElement;
import org.JE.JE2.Window.UIHandler;

import static org.lwjgl.nuklear.Nuklear.nk_button_label;

public class Button extends UIElement {
    public String text = "text";
    public Runnable onClickEvent = () -> {};

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
        if(nk_button_label(UIHandler.nuklearContext, text)){
            if(!isActive())
                return;
            onClickEvent.run();
        }

    }
}

