package org.JE.JE2.Objects;

import java.io.Serializable;

public class Identity implements Serializable {
    public String name;
    public String tag;
    public final long uniqueID;
    public Identity(String name, String tag){
        this.name = name;
        this.tag = tag;
        this.uniqueID = System.currentTimeMillis() * 1000 + (long)(Math.random() * 1000);
    }
    public Identity(){
        this.name = "GameObject";
        this.tag = "Untagged";
        this.uniqueID = System.currentTimeMillis() * 1000 + (long)(Math.random() * 1000);
    }

    @Override
    public String toString(){
        return "Name: '" + name + "' Tag: '" + tag + "'";
    }
}
