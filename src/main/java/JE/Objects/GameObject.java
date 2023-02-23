package JE.Objects;

import JE.Annotations.Nullable;
import JE.Annotations.RequireNonNull;
import JE.IO.FileInput.ImageProcessor;
import JE.IO.Logging.Errors.GameObjectError;
import JE.IO.Logging.Logger;
import JE.Objects.Scripts.Base.ScriptRestrictions;
import JE.Objects.Scripts.Base.Script;
import JE.Objects.Scripts.Common.Transform;
import JE.Objects.Scripts.Physics.PhysicsBody;
import JE.Rendering.Renderers.Renderer;
import JE.Rendering.Renderers.SpriteRenderer;
import JE.Rendering.Shaders.ShaderProgram;
import JE.Rendering.Texture;
import JE.Scene.Scene;
import org.joml.Vector2f;
import org.joml.Vector3f;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 JE2 - GameObject
 @author Noah Freelove

 GameObjects are the base for everything you see in JE2. Every object possesses a Transform (pos, rot, scale).
 GameObjects can have scripts attached to them to add additional behaviour.
 GameObjects should have a default constructor if you want them to work with save/load features.

 **/
public final class GameObject implements Serializable {
    public transient Scene linkedScene = null;
    private transient GameObject parent = this;
    private transient ArrayList<GameObject> children = new ArrayList<>();
    private transient CopyOnWriteArrayList<Script> scripts = new CopyOnWriteArrayList<>();
    private transient Renderer rendererRef = null;
    private transient PhysicsBody physicsBodyRef = null;
    private Identity identity = new Identity();

    private boolean active = true;
    private int layer = 0;

    public GameObject(){
        Transform t = new Transform();
        t.setRestrictions(new ScriptRestrictions(false,false,false));
        addScript(t);
    }

    public Transform getTransform(){
        return (Transform) scripts.get(0);
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
        scripts.set(0, transform);
    }

    @RequireNonNull
    public boolean addScript(Script script){
        Objects.requireNonNull(script);

        if(scripts == null)
            scripts = new CopyOnWriteArrayList<>();

        if(script.getAttachedObject() !=null)
        {
            Logger.log(new GameObjectError(this, "Can't Add Script. This Script already has a parent."));
            return false;
        }

        if(!script.getRestrictions().canHaveMultiple)
        {
            for(Script comp : scripts){
                if(comp.getClass() == script.getClass()){
                    Logger.log(new GameObjectError(this, "Can't Add Script. This Script cannot have multiple instances on a single object."));
                    return false;
                }
            }
        }

        if(script.getRestrictions().restrictClasses){
            boolean allowed = false;
            for (Class clazz: script.getRestrictions().permittedClasses) {
                if (getClass() == clazz) {
                    allowed = true;
                    break;
                }
            }
            if(!allowed) {
                Logger.log(new GameObjectError(this, "Can't Add Script. GameObject class is not on permitted list."));
                return false;
            }
        }

        if(script instanceof Renderer r)
        {
            rendererRef = r;
        }
        else if (script instanceof PhysicsBody p){
            physicsBodyRef = p;
        }
        script.setAttachedObject(this);
        script.onAddedToGameObject(this);

        notifyScriptsForeign(script);
        return scripts.add(script);
    }
    @RequireNonNull
    public boolean removeScript(Script c){
        if(c == null)
        {
            Logger.log(new GameObjectError(this, "Can't Remove Null Script."));
            return false;
        }
        if(c == scripts.get(0)){
            Logger.log(new GameObjectError(this, "Can't Remove transform Script."));
            return false;
        }
        if(!c.getRestrictions().canBeRemoved)
        {
            Logger.log(new GameObjectError(this, "Can't Remove Script: '" + c.getClass().getName() + "'.The Script's Restrictions do not allow it to be removed."));
            return false;
        }

        if(c.getAttachedObject() != this){
            Logger.log(new GameObjectError(this, "Can't Remove Script. This GameObject does not own Script: " + c.getClass().getName()));
            return false;
        }
        return scripts.remove(c);
    }
    public void scriptUpdate(){
        if(!active)
            return;
        for (int i = 1; i < scripts.size(); i++) {
            Script c = scripts.get(i);
            if(!c.getActive())
                continue;
            if(c.updateOnScriptUpdate){
                c.update();
            }
        }
        getTransform().update();
    }

    public void physicsUpdate(){
        if(!active)
            return;
        if(physicsBodyRef == null)
            return;

        if(physicsBodyRef.getActive())
        {
            physicsBodyRef.update();
        }
    }

    public void scriptStart(){
        if(!active)
            return;
        for(Script c : scripts){
            if(!c.getActive())
                continue;
            c.start();
        }
    }
    public void scriptAwake(){
        if(!active)
            return;
        scripts.forEach((c)->{
            if(!c.getActive())
                return;
            c.awake();
        });
    }
    private void notifyScriptsForeign(Script newScript){
        if(!active)
            return;
        scripts.forEach((c)->{
            if(!c.getActive())
                return;
            c.onForeignScriptAdded(newScript);
        });
    }
    public void scriptDestroy(){
        scripts.forEach(Script::destroy);
    }
    public void scriptUnload(Scene oldScene, Scene newScene){
        for(Script c : scripts){
            if(!c.getActive())
                continue;
            c.unload(oldScene, newScene);
        }
    }
    public void scriptParentAdded(Scene s){
        for(Script c : scripts){
            if(!c.getActive())
                continue;
            c.gameObjectAddedToScene(s);
        }
    }

    public void preRender(){}

    public Identity identity() {
        return new Identity(identity.name, identity.tag);
    }
    public void setIdentity(Identity identity) {
        this.identity = identity;
    }

    public CopyOnWriteArrayList<Script> getScripts() {
        return scripts;
    }

    @Nullable
    public <T extends Script> T getScript(Class<T> clazz){
        for (Script s :
                scripts) {
            if(s.getClass() == clazz){
                return (T) s;
            }
        }
        return null;
    }

    public <T extends Script> T[] getScripts(Class<T> clazz){
        ArrayList<T> list = new ArrayList<>();
        for (Script s :
                scripts) {
            if(s.getClass() == clazz){
                list.add((T) s);
            }
        }
        return (T[]) list.toArray();
    }

    @Nullable
    public Script getScript(int i){
        if(i >= scripts.size() || i<0)
            return null;
        return scripts.get(i);
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
            scriptAwake();
        }
    }

    public int getLayer() {
        return layer;
    }

    public void setLayer(int layer) {
        this.layer = layer;
    }

    public GameObject getParent(){
        return parent;
    }

    public GameObject[] getChildren(){
        return children.toArray(GameObject[]::new);
    }

    public void setParent(GameObject parent){
        if(parent == null){
            this.parent = this;
            return;
        }

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

    public static GameObject Sprite(ShaderProgram shaderProgram){
        GameObject sprite = new GameObject();
        sprite.addScript(new SpriteRenderer(shaderProgram));
        return sprite;
    }
    public static GameObject Sprite(ShaderProgram shaderProgram, Texture texture){
        return Sprite(shaderProgram, texture, new Texture(ImageProcessor.generateNormalMap(texture)));
    }

    public static GameObject Sprite(ShaderProgram shaderProgram, Texture texture, Texture normal){
        GameObject sprite = new GameObject();
        sprite.addScript(new SpriteRenderer(shaderProgram));
        if (sprite.getSpriteRenderer() != null) {
            sprite.getSpriteRenderer().setTexture(texture);
            sprite.getSpriteRenderer().setNormalTexture(normal);
        }
        return sprite;
    }

    @Nullable
    public SpriteRenderer getSpriteRenderer(){
        if(getRenderer() instanceof SpriteRenderer sr){
            return sr;
        }
        return null;
    }

    @Nullable
    public Renderer getRenderer(){
        return rendererRef;
    }

    @Nullable
    public PhysicsBody getPhysicsBody(){
        return physicsBodyRef;
    }

    public void setChildren(ArrayList<GameObject> children) {
        this.children = children;
    }

    public void setScripts(CopyOnWriteArrayList<Script> scripts) {
        this.scripts = scripts;
    }
}
