package org.JE.JE2.Objects;

import org.JE.JE2.Annotations.ActPublic;

import java.io.Serializable;

public class Identity implements Serializable {
    public String name;
    public String tag;

    public final long uniqueID;
    private static long uniqueOffset = 0;

    public Identity(String name, String tag){
        this.name = name;
        this.tag = tag;
        this.uniqueID = generateID();
    }

    public Identity(){
        this.name = "GameObject";
        this.tag = "Untagged";
        this.uniqueID = generateID();
    }

    private Identity(String name, String tag, long uniqueID) {
        this.name = name;
        this.tag = tag;
        this.uniqueID = uniqueID;
    }

    public static Identity createFakeID(String name, String tag, long uniqueID){
        return new Identity(name,tag,uniqueID);
    }

    @Override
    public String toString(){
        return "Name: '" + name + "' Tag: '" + tag + "'";
    }

    static long generateID(){
        uniqueOffset++;
        if(uniqueOffset >= Long.MAX_VALUE-1)
            uniqueOffset = 0;
        return uniqueOffset;
    }

    @Override
    public boolean equals(Object obj)
    {
        if(obj instanceof Identity other)
        {
            return other.uniqueID == this.uniqueID;
        }
        return false;
    }
    public boolean equals(Identity other, boolean strict){
        if(strict)
            return equals(other);
        else {
            return other.name.equals(this.name) && other.tag.equals(this.tag);
        }
    }
}
