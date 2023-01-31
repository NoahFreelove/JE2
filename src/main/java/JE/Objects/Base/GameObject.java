package JE.Objects.Base;

import JE.Annotations.Nullable;
import JE.Annotations.RequireNonNull;
import JE.Logging.Errors.GameObjectError;
import JE.Logging.Logger;
import JE.Objects.Components.Base.Component;
import JE.Objects.Components.Common.Transform;
import JE.Objects.Components.Physics.PhysicsBody;
import JE.Objects.Identity;
import JE.Rendering.Renderers.Renderer;
import JE.Scene.Scene;
import JE.Utility.JOMLtoJBOX;
import org.joml.Vector2f;
import org.joml.Vector3f;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Objects;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 JE2 - GameObject
 @author Noah Freelove

 GameObjects are the base for everything you see in JE2. Every object possesses a Transform (pos, rot, scale).
 GameObjects can have components attached to them to add additional behaviour.
 GameObjects should have a default constructor if you want them to work with save/load features.

 **/
public class GameObject implements Serializable {
    private GameObject parent = this;
    private ArrayList<GameObject> children = new ArrayList<>();

    private boolean active = true;
    private Identity identity = new Identity();
    public Renderer renderer = null;
    public PhysicsBody physicsBody = null;
    public Scene linkedScene = null;
    private int lightLayer = 0;

    private final CopyOnWriteArrayList<Component> components = new CopyOnWriteArrayList<>(){{
        add(new Transform());
    }};

    public GameObject(){}

    public Transform getTransform(){
        return (Transform) components.get(0);
    }

    public void setPosition(Vector2f pos){
        getTransform().setPosition(new Vector2f(pos));
    }
    public void setPosition(float x,float y)
    {
        getTransform().setPosition(new Vector2f(x,y));
    }

    public void setScale(Vector2f scale){
        getTransform().setScale(new Vector2f(scale));
    }
    public void setScale(float x,float y)
    {
        getTransform().setScale(new Vector2f(x,y));
    }
    public void setRotation(float z)
    {
        getTransform().setRotation(new Vector3f(0,0,z));
    }

    public void setTransform(Transform transform){
        components.set(0, transform);
    }

    public boolean addComponent(Component c){
        Objects.requireNonNull(c);

        if(c.parentObject() !=null)
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


        if(c instanceof Renderer r)
        {
            renderer = r;
        }
        else if(c instanceof PhysicsBody p)
        {
            physicsBody = p;
        }
        c.setParentObject(this);
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

        if(c.parentObject() != this){
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
        components.forEach((c)->{
            if(!c.getActive())
                return;
            c.awake();
        });


    }
    public void componentDestroy(){
        components.forEach(Component::destroy);
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
    public final void physicsUpdate(){
        if(physicsBody !=null)
        {
            Vector2f adjustedPos = new Vector2f(getTransform().position());
            adjustedPos.x += physicsBody.getSize().x/2;
            adjustedPos.y += physicsBody.getSize().y/2;
            physicsBody.body.setTransform(JOMLtoJBOX.vec2(adjustedPos), getTransform().rotation().z());
        }
    }

    public Identity getIdentity() {
        return new Identity(identity.name, identity.tag);
    }
    public void setIdentity(Identity identity) {
        this.identity = identity;
    }

    public CopyOnWriteArrayList<Component> getComponents() {
        return components;
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

    public int getLightLayer() {
        return lightLayer;
    }

    public void setLightLayer(int lightLayer) {
        this.lightLayer = lightLayer;
    }

    public GameObject getParent(){
        return parent;
    }

    public GameObject[] getChildren(){
        return children.toArray(GameObject[]::new);
    }

    @RequireNonNull
    public void setParent(GameObject parent){
        Objects.requireNonNull(parent);
        if(this.parent !=null)
            this.parent.removeChild(this);

        this.parent = parent;
        if(parent == this){
            return;
        }
        parent.addChild(this);
    }

    private void addChild(GameObject gameObject) {
        if(!children.contains(gameObject))
            children.add(gameObject);
    }

    private void removeChild(GameObject gameObject) {
        children.remove(gameObject);
    }

    @Nullable
    public GameObject getChild(int index){
        if(children.size()-1>=index){
            return children.get(index);
        }
        return null;
    }

}
