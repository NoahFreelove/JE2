package org.JE.JE2.Rendering.Debug;

import org.JE.JE2.Manager;
import org.JE.JE2.Scene.Scene;
import org.JE.JE2.UI.UIElements.Buttons.Button;
import org.JE.JE2.UI.UIElements.Group;
import org.JE.JE2.UI.UIElements.Spacer;
import org.JE.JE2.UI.UIObjects.UIWindow;

public class SceneSwitcherUI {
    public static UIWindow getWindow(){
        UIWindow window = new UIWindow("Swap Scenes");
        window.destroyOnLoad = false;
        window.addElement(getGroup());
        return window;
    }
    public static Group getGroup(){
        Group uiGroup = new Group();
        uiGroup.addElement(new Spacer());
        int i = 0;
        for (Scene scene : Manager.buildScenes.toArray(new Scene[0])) {
            int finalI = i;
            Button btn = new Button(scene.name, () -> Manager.setScene(finalI));
            uiGroup.addElement(btn);
            i++;
        }
        return uiGroup;
    }
}
