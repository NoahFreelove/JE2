package org.JE.JE2.Objects.Scripts.Physics;

import org.JE.JE2.Objects.GameObject;

public interface TriggerEvent{
    void onTriggerEnter(GameObject other);
    void onTriggerExit(GameObject other);
    void onTrigger(GameObject other);
}
