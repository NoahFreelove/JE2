package org.JE.JE2.Scene.SceneLoading;

import org.JE.JE2.Scene.Scene;

public class SceneLoadType {

    public String sceneFilepath = "";
    public boolean loadFromFile = false;

    public Class<? extends Scene> sceneClass;
    public boolean loadFromClass = false;
    public Class<?>[] parameters;
    public Object[] fields;
    public Scene scene;
    public boolean loadFromObject = false;

    public SceneLoadType(String filepath){
        this.sceneFilepath = filepath;
        loadFromFile = true;
    }

    public SceneLoadType(Scene scene){
        this.scene = scene;
        loadFromObject = true;
    }
    public SceneLoadType(Class<? extends Scene> sceneClass, Class<?>[] parameters, Object[] fields){
        this.parameters = parameters;
        this.fields = fields;
        this.sceneClass = sceneClass;
        loadFromClass = true;
    }

    public SceneLoadType(){}

}