package JE.UI.UIElements;

import JE.Window.UIHandler;

import static org.lwjgl.nuklear.Nuklear.nk_button_label;

public class Button extends Element_UI {
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
        if(nk_button_label(UIHandler.ctx, text)){
            onClickEvent.run();
        }
    }
}

