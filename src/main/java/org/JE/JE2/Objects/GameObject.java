package org.JE.JE2.Objects;

import org.JE.JE2.Annotations.ActPublic;
import org.JE.JE2.Annotations.ReadOnly;
import org.JE.JE2.Annotations.RequireNonNull;
import org.JE.JE2.IO.Filepath;
import org.JE.JE2.IO.Logging.Errors.GameObjectError;
import org.JE.JE2.IO.Logging.Errors.JE2Error;
import org.JE.JE2.IO.Logging.Logger;
import org.JE.JE2.Objects.Scripts.Attributes.DontDestroyOnLoad;
import org.JE.JE2.Objects.Scripts.LambdaScript.ILambdaScript;
import org.JE.JE2.Objects.Scripts.LambdaScript.LambdaScript;
import org.JE.JE2.Objects.Scripts.ScriptRestrictions;
import org.JE.JE2.Objects.Scripts.Script;
import org.JE.JE2.Objects.Scripts.Serialize.IgnoreSave;
import org.JE.JE2.Objects.Scripts.Serialize.Load;
import org.JE.JE2.Objects.Scripts.Serialize.Save;
import org.JE.JE2.Objects.Scripts.Transform;
import org.JE.JE2.Objects.Scripts.Physics.PhysicsBody;
import org.JE.JE2.Rendering.Renderers.Renderer;
import org.JE.JE2.Rendering.Renderers.SpriteRenderer;
import org.JE.JE2.Rendering.Shaders.ShaderProgram;
import org.JE.JE2.Rendering.Texture;
import org.JE.JE2.Resources.DataLoader;
import org.JE.JE2.Scene.Scene;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector2f;
import org.joml.Vector3f;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Serializable;
import java.util.*;
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
    private transient PhysicsBody activePhysicsBody = null;

    @ActPublic private Identity identity = new Identity();

    @ReadOnly
    public String name = identity.name;
    @ReadOnly
    public String tag = identity.tag;

    @ActPublic private boolean active = true;
    @ActPublic private int layer = 0;

    public GameObject(){
        Transform t = new Transform();
        t.setRestrictions(new ScriptRestrictions(false,false,false));
        addScript(t);
        preUpdateTransform.set(getTransform());
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

    public void setPosition(Vector2f pos, float z){
        getTransform().setPosition(pos.x(),pos.y(),z);
    }
    public void setPosition(float x,float y, float z)
    {
        getTransform().setPosition(x,y,z);
    }
    public Vector2f getPosition(){return getTransform().position();}
    public void setScale(Vector2f scale){
        getTransform().setScale(scale);
    }
    public void setScale(float x,float y)
    {
        getTransform().setScale(x,y);
    }
    public Vector2f getScale(){return  getTransform().scale();}
    public void setRotation(float z)
    {
        getTransform().setRotation(0,0,z);
    }
    public Vector3f getRotation(){return getTransform().rotation();}
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
        c.destroy();
        return scripts.remove(c);
    }
    private final Transform preUpdateTransform = new Transform();
    public void scriptUpdate(){
        if(!active)
            return;
        for (int i = 1; i < scripts.size(); i++) {
            Script c = scripts.get(i);
            if(!c.getActive())
                continue;
            if(c.updateOnScriptUpdate){
                c.getExternalScriptBehaviourPre().update(this);
                c.update();
                c.getExternalScriptBehaviourPost().update(this);
            }
        }
        getTransform().update();
        propagateToChildren();
        preUpdateTransform.set(getTransform());

    }
    private Transform delta = new Transform();
    private void propagateToChildren(){

        Transform.getDelta(preUpdateTransform, getTransform(), delta);

        if(!children.isEmpty()){
            //System.out.println("Pre update:" + preUpdateTransform);
            //System.out.println("Post update:" + getTransform());
            children.forEach((c)->{
                c.getTransform().inheritTransform(delta);
            });
        }
    }

    public void physicsUpdate(){
        if(!active)
            return;
        if(activePhysicsBody == null)
            return;

        if(activePhysicsBody.getActive())
        {
            activePhysicsBody.getExternalScriptBehaviourPre().update(this);
            activePhysicsBody.update();
            activePhysicsBody.getExternalScriptBehaviourPost().update(this);
        }
    }

    public void scriptStart(){
        preUpdateTransform.set(getTransform());

        if(!active)
            return;
        for(Script c : scripts){
            if(!c.getActive())
                continue;
            c.getExternalScriptBehaviourPre().start(this);
            c.start();
            c.getExternalScriptBehaviourPost().start(this);
        }
    }
    public void scriptAwake(){
        if(!active)
            return;
        scripts.forEach((c)->{
            if(!c.getActive())
                return;
            c.getExternalScriptBehaviourPre().awake(this);
            c.awake();
            c.getExternalScriptBehaviourPost().awake(this);

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
        for (Script s : scripts) {
            s.getExternalScriptBehaviourPre().destroy(this);
            s.destroy();
            s.getExternalScriptBehaviourPost().destroy(this);
        }
    }
    public void scriptUnload(Scene oldScene, Scene newScene){
        for(Script c : scripts){
            if(!c.getActive())
                continue;
            c.getExternalScriptBehaviourPre().unload(oldScene, newScene);
            c.unload(oldScene, newScene);
            c.getExternalScriptBehaviourPost().unload(oldScene, newScene);
        }
    }
    public void scriptParentAdded(Scene s){
        for(Script c : scripts){
            if(!c.getActive())
                continue;
            c.getExternalScriptBehaviourPre().gameObjectAddedToScene(s);
            c.gameObjectAddedToScene(s);
            c.getExternalScriptBehaviourPost().gameObjectAddedToScene(s);
        }
    }

    public void scriptPostRender(){
        if(!active)
            return;
        scripts.forEach((c)->{
            if(!c.getActive())
                return;
            c.getExternalScriptBehaviourPre().postRender(this);
            c.postRender();
            c.getExternalScriptBehaviourPost().postRender(this);
        });
    }

    public String name() {
        return identity.name;
    }
    public String tag() {
        return identity.tag;
    }
    public long uniqueID() {
        return identity.uniqueID;
    }

    public void setIdentity(String name, String tag){
        if(name == null)
            name = "GameObject";
        if(tag == null)
            tag = "gameObject";
        identity.name = name;
        identity.tag = tag;
        this.name = name;
        this.tag = tag;
    }

    public CopyOnWriteArrayList<Script> getScripts() {
        return scripts;
    }

    @Contract(pure = true)
    @Nullable
    public <T extends Script> T getScript(Class<T> clazz){
        if(clazz == null)
            return null;
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
            if(s.getClass() == clazz || clazz.isAssignableFrom(s.getClass())){
                list.add((T) s);
            }
        }
        return list;
    }

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

    public SpriteRenderer getSpriteRenderer(){
        if(getRenderer() instanceof SpriteRenderer sr){
            return sr;
        }
        return null;
    }

    public Renderer getRenderer(){
        return rendererRef;
    }

    public PhysicsBody getPhysicsBody(){
        CopyOnWriteArrayList<PhysicsBody> physicsBodies = getScripts(PhysicsBody.class);
        for (PhysicsBody pb : physicsBodies) {
            if(physicsBodies.size() == 1)
            {
                activePhysicsBody = pb;
                break;
            }
            if(pb.isInCorrectScene())
            {
                activePhysicsBody = pb;
                break;
            }
        }
        return activePhysicsBody;
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

    public HashMap<String, HashMap<String,String>> save(){
        HashMap<String, HashMap<String,String>> map = new HashMap<>();
        HashMap<String, String> identity = new HashMap<>();
        identity.put("name", this.identity.name);
        identity.put("tag", this.identity.tag);
        map.put("identity", identity);
        int i = 0;
        for (Script script : scripts) {
            i++;
            if(script == null)
                continue;
            if(!script.allowSaving())
                continue;
            if(script instanceof Save save){
                map.put(script.getClass().getName(),save.save());
            }
            else{
                if(!(script instanceof IgnoreSave)) {
                    Logger.log("WARNING: Script <" + script.getClass().getName() + "> does not implement save interface " +
                            "but is undergoing the save process. This script will be added but no fields will be saved.", Logger.WARN);
                }
                map.put(script.getClass().getName(), new HashMap<>());

            }
        }

        return map;
    }

    public void load(HashMap<String, HashMap<String,String>> map){
        HashMap<String, String> id = map.get("identity");
        setIdentity(id.get("name"), id.get("tag"));

       map.remove("identity");
        for (String scriptName : map.keySet()) {
            // Check scriptName exists. ex: org.JE.JE2.Manager
            // Create new instance with empty constructor
            // If It has the load interface, call it with map.get(scriptName)
            // If it doesn't have the load interface, log a warning
            try {
                Class<?> clazz = Class.forName(scriptName);
                if(clazz == Transform.class){
                    getTransform().load(map.get(scriptName));
                    continue;
                }
                Script script = (Script) clazz.getConstructor().newInstance();
                if(script instanceof Load load){
                    load.load(map.get(scriptName));
                    addScript(script);
                }
                else{
                    addScript(script);
                }
            } catch (Exception e) {
                Logger.log("WARNING: Script <" + scriptName + "> does not exist or does not have a default constructor " +
                        "but is undergoing the load process. This script will be ignored in the load output", Logger.WARN);
            }
        }
    }
    public String serialize(){
        HashMap<String, HashMap<String,String>> data = save();
        String[] lines = new String[data.size()];
        int i = 0;
        for (Map.Entry<String, HashMap<String, String>> entry : data.entrySet()) {
            String key = entry.getKey();
            HashMap<String, String> value = entry.getValue();
            lines[i] = key + ":";
            for (Map.Entry<String, String> entry2 : value.entrySet()) {
                String key2 = entry2.getKey();
                String value2 = entry2.getValue();
                lines[i] += key2 + "=" + value2 + ";";
            }
            i++;
        }
        // Combine string array into one string
        StringBuilder sb = new StringBuilder();
        for (String s : lines) {
            sb.append(s);
            sb.append("\n");
        }
        return sb.toString();
    }
    public void saveToFile(Filepath filepath){
        File file = new File(filepath.getDefault());
        try (FileWriter fr = new FileWriter(file)) {
            fr.write(serialize());
        } catch (IOException e) {
            Logger.log(new JE2Error(e));
        }
    }

    public static HashMap<String, HashMap<String,String>> deserialize(String[] input){
        System.out.println(Arrays.toString(input));
        HashMap<String, HashMap<String,String>> data = new HashMap<>();
        for (String line : input) {
            String[] split = line.split(":");
            String scriptName = split[0];
            HashMap<String, String> scriptDataMap = new HashMap<>();

            if(split.length >1){
                String[] scriptData = split[1].split(";");
                for (String s : scriptData) {
                    String[] split2 = s.split("=");
                    String key = split2[0];
                    if(split2.length>1){
                        String value = split2[1];
                        scriptDataMap.put(key, value);
                    }
                    else {
                        System.out.println("KEY: "+ key + ", HAS NO VALUE");

                    }                }
            }
            data.put(scriptName, scriptDataMap);
        }
        return data;
    }

    public static GameObject load(String[] input){
        GameObject newObj = new GameObject();
        newObj.load(deserialize(input));
        return newObj;
    }

    public void loadFromFile(Filepath filepath){
        String[] loadedData = DataLoader.readTextFile(filepath);
        load(deserialize(loadedData));
    }

    public boolean hasScript(Class<? extends Script> clazz) {
        if(clazz == null)
            return false;
        for (Script script : scripts) {
            if(script.getClass() == clazz){
                return true;
            }
        }
        return false;
    }
}
