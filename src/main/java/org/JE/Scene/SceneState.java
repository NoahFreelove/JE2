package org.JE.Scene;

import org.JE.Objects.GameObject;

public class SceneState {

    private SceneState(){

    }

    public static SceneState saveState(Scene scene, SceneStateSaveType saveType) {
        return saveState(scene, saveType, new GameObject());
    }

    public static SceneState saveState(Scene scene, SceneStateSaveType saveType, GameObject... ignore) {

        return new SceneState();
    }
}
