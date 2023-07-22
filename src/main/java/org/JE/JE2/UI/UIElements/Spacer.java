package org.JE.JE2.UI.UIElements;

import org.JE.JE2.Window.UIHandler;

import static org.lwjgl.nuklear.Nuklear.nk_spacer;

public class Spacer extends UIElement {
    private int amount = 1;
    public Spacer() {
    }

    public Spacer(int amount) {
        if(amount<0)
            amount = 0;
        this.amount = amount;
    }

    @Override
    protected void render() {
        for (int i = 0; i < amount; i++) {
            nk_spacer(UIHandler.nuklearContext);
        }
    }
}
