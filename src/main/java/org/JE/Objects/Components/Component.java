package org.JE.Objects.Components;

import org.JE.Objects.GameObject;

public abstract class Component {
    public boolean active = true;
    public abstract void update();
    public abstract void start();
    public abstract void awake();
}
