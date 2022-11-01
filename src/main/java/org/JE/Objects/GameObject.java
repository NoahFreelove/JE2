package org.JE.Objects;

import org.JE.Objects.Components.Component;
import org.JE.Objects.Components.Transform;

import java.util.ArrayList;

public class GameObject {
    public boolean active = true;

    private ArrayList<Component> components = new ArrayList<>(){{
        add(new Transform());
    }};

    public GameObject(){}

    public Transform getTransform(){
        return (Transform) components.get(0);
    }
    public void setTransform(Transform transform){
        components.set(0, transform);
    }

    public boolean addComponent(Component c){
        return components.add(c);
    }
    public boolean removeComponent(Component c){
        if(c == components.get(0) || c == null || components.size() == 1){
            return false;
        }
        return components.remove(c);
    }

    public void Update(){
        if(!active)
            return;
        for(Component c : components){
            if(!c.active)
                continue;
            c.update();
        }
    }
    public void Start(){
        if(!active)
            return;
        for(Component c : components){
            if(!c.active)
                continue;
            c.start();
        }
    }
    public void Awake(){
        if(!active)
            return;
        for(Component c : components){
            if(!c.active)
                continue;
            c.awake();
        }
    }


}
