package JE.Objects;

import JE.Objects.Components.Component;
import JE.Objects.Components.Transform;
import JE.Rendering.Renderer;

import java.io.Serializable;
import java.util.ArrayList;

public class GameObject implements Serializable {
    public boolean active = true;
    private Identity identity = new Identity();
    public Renderer renderer = null;

    private final ArrayList<Component> components = new ArrayList<>(){{
        add(new Transform());
    }};

    public GameObject(){}
    public GameObject(Identity id){
        this.identity = id;
    }

    public Transform getTransform(){
        return (Transform) components.get(0);
    }
    public void setTransform(Transform transform){
        components.set(0, transform);
    }

    public boolean addComponent(Component c){
        if(!c.getRestrictions().canHaveMultiple)
        {
            for(Component comp : components){
                if(comp.getClass() == c.getClass()){
                    return false;
                }
            }
        }
        if(c instanceof Renderer)
        {
            renderer = (Renderer) c;
        }
        return components.add(c);
    }
    public boolean removeComponent(Component c){
        if(c == components.get(0) || c == null || components.size() == 1){
            return false;
        }
        if(!c.getRestrictions().canBeRemoved)
            return false;

        return components.remove(c);
    }

    public void Update(){
        if(!active)
            return;
        for(Component c : components){
            if(!c.getActive())
                continue;
            c.update();
        }
    }
    public void Start(){
        if(!active)
            return;
        for(Component c : components){
            if(!c.getActive())
                continue;
            c.start();
        }
    }
    public void Awake(){
        if(!active)
            return;
        for(Component c : components){
            if(!c.getActive())
                continue;
            c.awake();
        }
    }

    public Identity getIdentity() {
        return new Identity(identity.name, identity.tag);
    }
    public void setIdentity(Identity identity) {
        this.identity = identity;
    }

}
