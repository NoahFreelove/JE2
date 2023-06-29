package org.JE.JE2.UI.UIElements;

import org.JE.JE2.Window.UIHandler;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import static org.JE.JE2.Window.UIHandler.nuklearContext;
import static org.lwjgl.nuklear.Nuklear.nk_style_set_font;

public class Group extends UIElement{
    private final CopyOnWriteArrayList<UIElement> elements = new CopyOnWriteArrayList<>();

    public Group(){}

    public Group(UIElement... elements) {
        this.elements.addAll(List.of(elements));
    }

    public void addElement(UIElement element) {
        elements.add(element);
    }

    public void addElements(UIElement... elements) {
        this.elements.addAll(List.of(elements));
    }

    public void removeElement(UIElement element) {
        elements.remove(element);
    }

    public void removeElements(UIElement... elements) {
        this.elements.removeAll(List.of(elements));
    }

    @Override
    protected void render() {
        nk_style_set_font(nuklearContext, UIHandler.default_font.getFont());
        elements.forEach(UIElement::requestRender);
    }
}
