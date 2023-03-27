package org.JE.JE2.UI.UIElements;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class Group extends UIElement{
    private CopyOnWriteArrayList<UIElement> elements = new CopyOnWriteArrayList<>();

    public Group(){}

    public Group(UIElement[] elements) {
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
        elements.forEach(UIElement::requestRender);
    }
}
