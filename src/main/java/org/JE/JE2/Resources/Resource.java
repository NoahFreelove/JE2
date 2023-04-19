package org.JE.JE2.Resources;

import org.JE.JE2.Annotations.Nullable;
import org.JE.JE2.Resources.Bundles.ResourceBundle;

import java.io.Serializable;
import java.lang.ref.WeakReference;

public class Resource<T extends ResourceBundle> implements Serializable {
    public final Class<T> type;
    private transient T ref;
    private String name = "";
    private int ID = -1;

    private Resource(){
        type = null;
    }
    public Resource (T bundle, String name, int ID) {
        ref = bundle;
        type = (Class<T>) bundle.getClass();
        this.name = name;
        this.ID = ID;
    }

    @Nullable
    public T getBundle() {
        return ref;
    }

    @Override
    public boolean equals(Object obj){
        boolean equalName = false;
        boolean equalID = false;
        boolean equalData = false;

        if(obj instanceof Resource<?> resource)
        {
            equalID = ((getID() == resource.getID()) && (getID()>=0));
            equalName = ((getName().equals(resource.getName())) && (getName() !=null) && !getName().equals(""));

            if(resource.type == type){
                Resource<T> typeCasted = (Resource<T>) resource;

                /*if(ResourceManager.policy == ResourceLoadingPolicy.CHECK_BY_ID && equalID)
                    return true;*/
                if(ResourceManager.policy == ResourceLoadingPolicy.CHECK_BY_NAME && equalName)
                    return true;

                equalData = typeCasted.getBundle().compareData(getBundle().getData());


                if(ResourceManager.policy == ResourceLoadingPolicy.CHECK_BY_BYTE_DATA && equalData)
                    return true;


            }
            else return false;
        }

        return false;
    }

    public String getName() {
        return name;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }
}
