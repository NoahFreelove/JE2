package org.JE.JE2.UI.UIElements.Style;

import org.JE.JE2.Resources.DataLoader;
import org.JE.JE2.UI.Font;

public class StyleInfo {
    public Color hoverColor = Color.createColorHex("#0061CE");
    public Color normalColor = Color.createColorHex("#006AF6");
    public Color inactiveColor = Color.createColorHex("#002550");
    public Color pressedColor = Color.createColorHex("#000B50");
    public Color textColor = Color.createColorHex("#FFFFFF");
    public Font font;

    public StyleInfo(){
        font = new Font(DataLoader.getBytes("arial.ttf"), false);
    }
    public StyleInfo setHoverColor(Color c){
        this.hoverColor = c;
        return this;
    }
    public StyleInfo setInactiveColor(Color c){
        this.inactiveColor = c;
        return this;
    }
    public StyleInfo setNormalColor(Color c){
        this.normalColor = c;
        return this;
    }
    public StyleInfo setPressedColor(Color c){
        this.pressedColor = c;
        return this;
    }

    public StyleInfo setTextColor(Color c){
        this.textColor = c;
        return this;
    }
}
