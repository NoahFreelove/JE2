package JE.UI.UIElements.Buttons;

import JE.UI.UIElements.Style.StyleInfo;
import JE.Window.UIHandler;
import org.lwjgl.nuklear.NkStyleButton;

public class StyledButton extends Button{
    NkStyleButton button = UIHandler.ctx.style().button();
    public StyleInfo style = new StyleInfo();

    public StyledButton() {
    }

    public StyledButton(String text) {
        super(text);
    }
    public StyledButton(String text, StyleInfo style) {
        super(text);
        this.style = style;
    }

    public StyledButton(String text, Runnable onClickEvent) {
        super(text, onClickEvent);
    }

    public StyledButton(String text, StyleInfo style, Runnable onClickEvent) {
        super(text, onClickEvent);
        this.style = style;
    }

    @Override
    protected void render() {
        button.draw_begin();

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
        super.render();
        button.draw_end();
    }
}
