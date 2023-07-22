package org.JE.JE2.Objects.Scripts.LambdaScript;

import org.JE.JE2.Objects.GameObject;
import org.JE.JE2.Objects.Scripts.Script;
import org.JE.JE2.Scene.Scene;

public interface ILambdaScript {
    default void start(GameObject parent){}
    default void update(GameObject parent){}
    default void awake(GameObject parent){}
    default void unload(Scene oldScene, Scene newScene){}
    default void destroy(GameObject parent){}
    default void onForeignScriptAdded(Script script){}
    default void onAddedToGameObject(GameObject gameObject){}
    default void gameObjectAddedToScene(Scene scene){}
    default void postRender(GameObject parent){}
}
