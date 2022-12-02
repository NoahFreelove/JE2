package JE.UI;

import JE.Objects.Base.GameObject;
import org.joml.Vector2f;

import java.util.ArrayList;

public final class UIHandler extends GameObject {
    public ArrayList<UINode> nodes = new ArrayList<>();

    public UIHandler(){
        //nodes.add(new UINode(new Vector2f(0,0),new Vector2f(0.5f,0.5f)));
    }

    @Override
    public void update() {
        for (UINode node : nodes) {
            if(node instanceof UIUpdateable)
                ((UIUpdateable) node).update();
        }
    }

    @Override
    public void preRender(){
        for (UINode node :
                nodes) {
            if (node.renderer != null)
                node.renderer.Render(node.getTransform());
        }
    }
}
