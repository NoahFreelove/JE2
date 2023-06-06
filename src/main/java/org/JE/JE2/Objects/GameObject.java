package org.JE.JE2.Objects;

import org.JE.JE2.Annotations.ActPublic;
import org.JE.JE2.Annotations.Nullable;
import org.JE.JE2.Annotations.RequireNonNull;
import org.JE.JE2.IO.Logging.Errors.GameObjectError;
import org.JE.JE2.IO.Logging.Logger;
import org.JE.JE2.Objects.Scripts.LambdaScript.ILambdaScript;
import org.JE.JE2.Objects.Scripts.LambdaScript.LambdaScript;
import org.JE.JE2.Objects.Scripts.ScriptRestrictions;
import org.JE.JE2.Objects.Scripts.Script;
import org.JE.JE2.Objects.Scripts.Transform;
import org.JE.JE2.Objects.Scripts.Physics.PhysicsBody;
import org.JE.JE2.Rendering.Renderers.Renderer;
import org.JE.JE2.Rendering.Renderers.SpriteRenderer;
import org.JE.JE2.Rendering.Shaders.ShaderProgram;
import org.JE.JE2.Rendering.Texture;
import org.JE.JE2.Scene.Scene;
import org.joml.Vector2f;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Objects;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 JE2 - GameObject

 GameObjects are the base for everything you see in JE2. Every object possesses a Transform (pos, rot, scale).
 GameObjects can have scripts attached to them to add additional behaviour.
 GameObjects should have a default constructor if you want them to work with save/load features.
 @author Noah Freelove

 **/
public final class GameObject implements Serializable {
    public static final GameObject emptyGameObject = new GameObject();
    public transient Scene linkedScene = null;
    private transient GameObject parent = this;
    private transient ArrayList<GameObject> children = new ArrayList<>();
    private transient CopyOnWriteArrayList<Script> scripts = new CopyOnWriteArrayList<>();
    private transient Renderer rendererRef = null;
    private transient PhysicsBody physicsBodyRef = null;

    @ActPublic
    private Identity identity = new Identity();
    @ActPublic
    private boolean active = true;
    @ActPublic
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
        getTransform().setPosition(pos);
    }
    public void setPosition(float x,float y)
    {
        getTransform().setPosition(x,y);
    }

    public void setScale(Vector2f scale){
        getTransform().setScale(scale);
    }
    public void setScale(float x,float y)
    {
        getTransform().setScale(x,y);
    }
    public void setRotation(float z)
    {
        getTransform().setRotation(0,0,z);
    }

    public void setTransform(Transform transform){
        scripts.set(0, transform);
    }

    public GameObject addScript(ILambdaScript lambda){
        return addScript(new LambdaScript(lambda));
    }

    @RequireNonNull
    public GameObject addScript(Script script){
        Objects.requireNonNull(script);

        if(scripts == null)
            scripts = new CopyOnWriteArrayList<>();

        if(script.getAttachedObject() !=null)
        {
            Logger.log(new GameObjectError(this, "Can't Add Script. This Script already has a parent."));
            return this;
        }

        if(!script.getRestrictions().canHaveMultiple)
        {
            for(Script comp : scripts){
                if(comp.getClass() == script.getClass()){
                    Logger.log(new GameObjectError(this, "Can't Add Script. This Script cannot have multiple instances on a single object."));
                    return this;
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
                return this;
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
        scripts.add(script);
        return this;
    }

    @RequireNonNull
    public boolean removeScript(Script c){
        if(c == null)
        {
            Logger.log(new GameObjectError(this, "Can't Remove Null Script."));
            return false;
        }
        if(scripts.size()>0)
        {
            if(c == scripts.get(0)){
                Logger.log(new GameObjectError(this, "Can't Remove transform Script."));
                return false;
            }
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

    public void scriptPostRender(){
        if(!active)
            return;
        scripts.forEach((c)->{
            if(!c.getActive())
                return;
            c.postRender();
        });
    }

    public Identity identity() {
        return identity;
    }

    public void setIdentity(String name, String tag){
        identity.name = name;
        identity.tag = tag;
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

    public <T extends Script> CopyOnWriteArrayList<T> getScripts(Class<T> clazz){
        CopyOnWriteArrayList<T> list = new CopyOnWriteArrayList<>();
        for (Script s :
                scripts) {
            if(s.getClass() == clazz){
                list.add((T) s);
            }
        }
        return list;
    }

    @Nullable
    public Script getScript(int i){
        if(i >= scripts.size() || i<0)
            return null;
        return scripts.get(i);
    }

    public void setScript(int i, Script script){
        if(scripts.get(i).getClass() == script.getClass()){
            scripts.set(i, script);
            script.setAttachedObject(this);
            script.onAddedToGameObject(this);
        }
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
        return Sprite(shaderProgram, texture, texture);
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

    public void setScripts(ArrayList<Script> scripts) {
        this.scripts.clear();
        this.scripts.addAll(scripts);
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof GameObject){
            return ((GameObject) obj).identity.uniqueID == identity.uniqueID;
        }
        return false;
    }
}
