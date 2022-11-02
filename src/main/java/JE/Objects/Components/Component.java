package JE.Objects.Components;

import java.io.Serializable;

public abstract class Component implements Serializable {
    public boolean active = true;
    public abstract void update();
    public abstract void start();
    public abstract void awake();
}
