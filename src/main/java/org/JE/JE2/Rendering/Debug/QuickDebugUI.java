package org.JE.JE2.Rendering.Debug;

import org.JE.JE2.UI.UIElements.Group;
import org.JE.JE2.UI.UIElements.UIElement;
import org.JE.JE2.UI.UIObjects.UIWindow;

public class QuickDebugUI {
    public static UIWindow quickDebugWindow(UIElement element){
        return quickDebugWindow(new Group(element), "Debug Window");
    }

    public static UIWindow quickDebugWindow(Group elements, String name){
        UIWindow window = new UIWindow(name);
        window.addElement(elements);
        return window;
    }
}
