package JE.Objects.Base;

import JE.Logging.Errors.GameObjectError;
import JE.Logging.Logger;
import JE.Objects.Components.Component;
import JE.Objects.Components.Transform;
import JE.Rendering.RenderTypes.Renderer;

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
    public GameObject(Transform t){
        this.setTransform(t);
    }
    public GameObject(Transform t, Identity id){
        this.setTransform(t); this.identity = id;
    }

    public Transform getTransform(){
        return (Transform) components.get(0);
    }
    public void setTransform(Transform transform){
        components.set(0, transform);
    }

    public boolean addComponent(Component c){
        if(c.parentObject !=null)
        {
            Logger.log(new GameObjectError(this, "Can't Add Component. This Component already has a parent."));
            return false;
        }
        if(!c.getRestrictions().canHaveMultiple)
        {
            for(Component comp : components){
                if(comp.getClass() == c.getClass()){
                    Logger.log(new GameObjectError(this, "Can't Add Component. This Component cannot have multiple instances on a single object."));
                    return false;
                }
            }
        }

        if(c.getRestrictions().restrictClasses){
            boolean allowed = false;
            for (Class clazz: c.getRestrictions().permittedClasses) {
                if (getClass() == clazz) {
                    allowed = true;
                    break;
                }
            }
            if(!allowed) {
                Logger.log(new GameObjectError(this, "Can't Add Component. GameObject class is not on permitted list."));
                return false;
            }
        }


        if(c instanceof Renderer)
        {
            renderer = (Renderer) c;
        }
        c.parentObject = this;
        c.onAddedToGameObject(this);
        return components.add(c);
    }
    public boolean removeComponent(Component c){
        if(c == null)
        {
            Logger.log(new GameObjectError(this, "Can't Remove Null Component."));
            return false;
        }
        if(c == components.get(0)){
            Logger.log(new GameObjectError(this, "Can't Remove transform component."));
            return false;
        }
        if(!c.getRestrictions().canBeRemoved)
        {
            Logger.log(new GameObjectError(this, "Can't Remove Component: '" + c.getClass().getName() + "'.The Component's Restrictions do not allow it to be removed."));
            return false;
        }

        if(c.parentObject != this){
            Logger.log(new GameObjectError(this, "Can't Remove Component. This GameObject does not own component: " + c.getClass().getName()));
            return false;
        }
        return components.remove(c);
    }
    public void componentUpdate(){
        if(!active)
            return;
        for(Component c : components){
            if(!c.getActive())
                continue;
            c.Update();
        }
    }
    public void componentStart(){
        if(!active)
            return;
        for(Component c : components){
            if(!c.getActive())
                continue;
            c.Start();
        }
    }

    public void update(){

    }
    public void start(){

    }
    public void awake(){
        if(!active)
            return;
        for(Component c : components){
            if(!c.getActive())
                continue;
            c.awake();
        }
    }

    public void preRender(){}

    public Identity getIdentity() {
        return new Identity(identity.name, identity.tag);
    }
    public void setIdentity(Identity identity) {
        this.identity = identity;
    }

    @Override
    public String toString() {
        return identity.toString();
    }
}
