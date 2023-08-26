package org.JE.JE2.Objects.Scripts.Attributes;

import org.JE.JE2.Objects.GameObject;
import org.JE.JE2.Objects.Identity;
import org.JE.JE2.Objects.Scripts.Physics.PhysicsBody;
import org.JE.JE2.Objects.Scripts.Script;
import org.JE.JE2.Objects.Scripts.Serialize.Load;
import org.JE.JE2.Objects.Scripts.Serialize.Save;
import org.JE.JE2.Rendering.Camera;
import org.JE.JE2.Scene.Scene;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.function.Consumer;

public class DontDestroyOnLoad extends Script implements Load, Save {
    private final boolean propagatesToChildren;
    private ArrayList<IDontDestroyOnLoad> liabilities = new ArrayList<>();

    public DontDestroyOnLoad(){
        propagatesToChildren = false;
        addDefaultLiabilities();
    }

    public DontDestroyOnLoad(boolean propagatesToChildren){
        this.propagatesToChildren = propagatesToChildren;
        addDefaultLiabilities();
    }

    private void addDefaultLiabilities(){
        liabilities.add(new IPhysicsLiability(){});
        liabilities.add(new ICameraLiability(){});
    }

    @Override
    public void onAddedToGameObject(GameObject gameObject) {
        propagateToChildren();
    }

    @Override
    public void start() {
        propagateToChildren();
    }

    private void propagateToChildren(){
        if(propagatesToChildren){
            for (GameObject child : getAttachedObject().getChildren()) {
                if(!child.hasScript(DontDestroyOnLoad.class))
                    child.addScript(new DontDestroyOnLoad(true));
            }
        }
    }

    @Override
    public void update(){
        //Logger.log(getAttachedObject().getTransform().position(), 2);
    }
    @Override
    public void unload(Scene oldScene, Scene newScene) {
        GameObject o = getAttachedObject();
        if (o == null) return;
        o.linkedScene = newScene;

        if(!newScene.setIfExists(Identity.createFakeID(o.name, o.tag, o.uniqueID()), o, true)){
            newScene.add(o);
        }
        addLiabilities(oldScene, newScene);
    }

    private void addLiabilities(Scene oldScene, Scene newScene){
        if(getAttachedObject() == null || !getActive())
            return;
        Camera c = getAttachedObject().getScript(Camera.class);
        if (c != null)
            newScene.setCamera(c);

        for (Script script : getAttachedObject().getScripts()) {
            for (IDontDestroyOnLoad dont :
                    liabilities) {
                if (dont.getApplicableClass() == script.getClass()) {
                    dont.addLiability(oldScene,newScene, getAttachedObject(), script);
                }
            }
        }

        //getAttachedObject().getScripts().forEach(script -> System.out.println(script.getClass().getSimpleName()));
    }

    @Override
    public void load(HashMap<String, String> data) {

    }

    @Override
    public HashMap<String, String> save() {
        return new HashMap<>();
    }

    public void addLiability(IDontDestroyOnLoad liability){
        liabilities.add(liability);
    }
}
