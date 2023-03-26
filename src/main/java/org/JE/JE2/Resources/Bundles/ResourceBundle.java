package org.JE.JE2.Resources.Bundles;

import java.io.Serializable;
import java.util.Objects;

public abstract class ResourceBundle implements Serializable {
    private transient String filepath;
    private boolean isLoaded = false;

    public String tryGetFilepath() {
        return Objects.requireNonNullElse(filepath, "/");
    }

    public boolean isLoaded() {
        return isLoaded;
    }
    protected void load(){
        isLoaded = true;
    }
    protected void unload(){
        isLoaded = false;
    }
    public void setFilepath(String filepath) {
        this.filepath = filepath;
    }
    public final void free(){
        freeResource();
        unload();
    }
    protected abstract void freeResource();
}
