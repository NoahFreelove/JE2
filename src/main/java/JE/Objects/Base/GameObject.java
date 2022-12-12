package JE.Objects.Base;

import JE.Logging.Errors.GameObjectError;
import JE.Logging.Logger;
import JE.Objects.Components.Base.Component;
import JE.Objects.Components.Common.Transform;
import JE.Rendering.RenderTypes.Renderer;
import JE.Scene.Scene;
import org.joml.Vector2f;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Objects;

/**
 JE2 - GameObject
 @author Noah Freelove

 GameObjects are the base for everything you see in JE2. Every object possesses a Transform (pos, rot, scale).
 GameObjects can have components attached to them to add additional behaviour.
 GameObjects should have a default constructor if you want them to work with save/load features.

 **/
public class GameObject implements Serializable {
    private boolean active = true;
    private Identity identity = new Identity();
    public Renderer renderer = null;

    private final ArrayList<Component> components = new ArrayList<>(){{
        add(new Transform());
    }};

    public GameObject(){}

    public Transform getTransform(){
        return (Transform) components.get(0);
    }

    public void setPosition(Vector2f pos){
        getTransform().position = new Vector2f(pos);
    }
    public void setPosition(float x,float y)
    {
        getTransform().position = new Vector2f(x,y);
    }

    public void setScale(Vector2f scale){
        getTransform().scale = new Vector2f(scale);
    }
    public void setScale(float x,float y)
    {
        getTransform().scale = new Vector2f(x,y);
    }

    public void setTransform(Transform transform){
        components.set(0, transform);
    }

    public boolean addComponent(Component c){
        Objects.requireNonNull(c);

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
            c.update();
        }
    }
    public void componentStart(){
        if(!active)
            return;
        for(Component c : components){
            if(!c.getActive())
                continue;
            c.start();
        }
    }
    public void componentAwake(){
        if(!active)
            return;
        for(Component c : components){
            if(!c.getActive())
                continue;
            c.awake();
        }
    }
    public void componentDestroy(){
        for(Component c : components){
            if(!c.getActive())
                continue;
            c.destroy();
        }
    }
    public void componentUnload(Scene oldScene, Scene newScene){
        for(Component c : components){
            if(!c.getActive())
                continue;
            c.unload(oldScene, newScene);
        }
    }
    public void componentGameObjectAddedToScene(Scene s){
        for(Component c : components){
            if(!c.getActive())
                continue;
            c.gameObjectAddedToScene(s);
        }
    }

    public void update(){

    }
    public void start(){
    }
    public void awake(){
    }

    public void destroy(){

    }
    public void unload(){}

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
    public boolean active(){
        return active;
    }
    public void setActive(boolean newState){
        this.active = newState;
        if(active)
        {
            awake();
            componentAwake();
        }
    }
}
