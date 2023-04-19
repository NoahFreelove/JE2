package org.JE.JE2.Scene.SceneLoading;

import org.JE.JE2.Scene.Scene;

public abstract class LoadingSequence {

    public LoadingStage stage = LoadingStage.NOT_STARTED;
    private boolean canceled = false;
    protected Scene loadedScene;

    protected SceneLoadType type;

    public LoadingSequence(SceneLoadType type) {
        this.type = type;
    }

    protected void loadSceneData(){
        if(type.loadFromClass){
            // Create new instance of class from java reflect default constructor
            Class<? extends Scene> clazz = type.sceneClass;
            try {
                loadedScene = clazz.getConstructor(type.parameters).newInstance(type.fields);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        else if(type.loadFromObject){
            loadedScene = type.scene;
        }
        else if (type.loadFromFile){
            //TODO: implement
        }
    }

    public abstract void warmupObjects();
    public abstract void finalization();
    public abstract void sceneSwap();
    public abstract void sceneStart();

    public void cancel(){
        canceled = true;
    }

    public void initiate(){
        if(canceled)
            return;
        stage = LoadingStage.WARMING_OBJECTS;
        warmupObjects();
    }

    protected void next(){
        if(canceled)
            return;
        if(stage == LoadingStage.WARMING_OBJECTS){
            stage = LoadingStage.SWAPPING_SCENE;
            sceneSwap();
        }
        else if (stage == LoadingStage.SWAPPING_SCENE){
            stage = LoadingStage.STARTING_SCENE;
            sceneStart();
        }
        else if (stage == LoadingStage.STARTING_SCENE){
            stage = LoadingStage.FINALIZATION;
            finalization();
        }
        else if (stage == LoadingStage.FINALIZATION)
            stage = LoadingStage.COMPLETED;
    }
    public boolean isCompleted(){
        return (stage == LoadingStage.COMPLETED);
    }
}
