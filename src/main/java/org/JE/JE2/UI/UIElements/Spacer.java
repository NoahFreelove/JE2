package org.JE.JE2.UI.UIElements;

import org.JE.JE2.Window.UIHandler;

import static org.lwjgl.nuklear.Nuklear.nk_spacer;

public class Spacer extends UIElement {
    @Override
    protected void render() {
        nk_spacer(UIHandler.nuklearContext);
    }
}
