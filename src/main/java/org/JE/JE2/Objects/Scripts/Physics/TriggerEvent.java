package org.JE.JE2.Objects.Scripts.Physics;

import org.JE.JE2.Objects.GameObject;

public interface TriggerEvent{
    default void onTriggerEnter(GameObject other){}
    default void onTriggerExit(GameObject other){}
    default void onTrigger(GameObject other){}
}
