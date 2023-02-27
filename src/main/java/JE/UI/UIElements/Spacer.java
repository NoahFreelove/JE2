package JE.UI.UIElements;

import JE.Window.UIHandler;

import static org.lwjgl.nuklear.Nuklear.nk_spacer;

public class Spacer extends UIElement {
    @Override
    protected void render() {
        nk_spacer(UIHandler.nuklearContext);
    }
}
