package JE.UI.UIElements.Checkboxes;

import JE.UI.UIElements.BooleanEventChanged;
import JE.UI.UIElements.Style.StyleInfo;
import JE.Window.UIHandler;
import org.lwjgl.nuklear.NkStyleToggle;

public class StyledCheckbox extends Checkbox{
    NkStyleToggle toggle = UIHandler.ctx.style().checkbox();
    public StyleInfo style = new StyleInfo();

    public StyledCheckbox() {}

    public StyledCheckbox(boolean isChecked) {
        super(isChecked);
    }

    public StyledCheckbox(boolean isChecked, StyleInfo style){
        super(isChecked);
        this.style = style;
    }

    public StyledCheckbox(boolean isChecked, String label) {
        super(isChecked, label);
    }

    public StyledCheckbox(boolean isChecked, String label, StyleInfo style) {
        super(isChecked, label);
        this.style = style;
    }

    public StyledCheckbox(boolean isChecked, String label, BooleanEventChanged onChange) {
        super(isChecked, label, onChange);
    }

    public StyledCheckbox(boolean isChecked, String label, BooleanEventChanged onChange, StyleInfo style) {
        super(isChecked, label, onChange);
        this.style = style;
    }

    @Override
    protected void render() {
        toggle.draw_begin();
        if(isActive()){
            toggle.active().data().color().set(style.pressedColor.nkColor());
            toggle.normal().data().color().set(style.normalColor.nkColor());
            toggle.hover().data().color().set(style.hoverColor.nkColor());
        }
        else {
            toggle.active().data().color().set(style.inactiveColor.nkColor());
            toggle.normal().data().color().set(style.inactiveColor.nkColor());
            toggle.hover().data().color().set(style.inactiveColor.nkColor());
        }
        // set check icon image


        super.render();
        toggle.draw_end();
    }
}
