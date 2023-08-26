package org.JE.JE2.Objects.Scripts.Attributes;

import org.JE.JE2.Objects.GameObject;
import org.JE.JE2.Objects.Scripts.Script;
import org.JE.JE2.Scene.Scene;

public interface IDontDestroyOnLoad {
    void addLiability(Scene oldScene, Scene newScene, GameObject object, Script script);
    default Class<? extends Script> getApplicableClass(){
        return Script.class;
    }
}
