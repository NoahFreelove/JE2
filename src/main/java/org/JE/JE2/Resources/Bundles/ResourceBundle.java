package org.JE.JE2.Resources.Bundles;

import org.JE.JE2.IO.Filepath;

import java.io.Serializable;
import java.util.Objects;

public abstract class ResourceBundle implements Serializable {
    public transient Filepath filepath;
    private boolean isLoaded = false;

    public Filepath tryGetFilepath() {
        return Objects.requireNonNullElse(filepath, new Filepath("/",true));
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
    public void setFilepath(Filepath filepath) {
        this.filepath = filepath;
    }
    public final void free(){
        freeResource();
        unload();
    }
    public abstract boolean compareData(byte[] input);
    public abstract byte[] getData();
    protected abstract void freeResource();
}
