package JE.Objects;

import java.io.Serializable;

public class Identity implements Serializable {
    public String name;
    public String tag;
    public Identity(String name, String tag){
        this.name = name;
        this.tag = tag;
    }
    public Identity(){
        this.name = "GameObject";
        this.tag = "Untagged";
    }

    @Override
    public String toString(){
        return "Name: '" + name + "' Tag: '" + tag + "'";
    }
}
