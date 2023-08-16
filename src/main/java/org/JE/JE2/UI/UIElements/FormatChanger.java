package org.JE.JE2.UI.UIElements;

import org.JE.JE2.Window.UIHandler;

import static org.lwjgl.nuklear.Nuklear.nk_layout_row_static;

public class FormatChanger extends UIElement{

    float row_height;
    int width_per_item;
    int items_per_row;

    public FormatChanger(float row_height, int width_per_item, int items_per_row) {
        this.row_height = row_height;
        this.width_per_item = width_per_item;
        this.items_per_row = items_per_row;
    }

    @Override
    protected void render() {
        nk_layout_row_static(UIHandler.nuklearContext, row_height, width_per_item, items_per_row);
    }
}
