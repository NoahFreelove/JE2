package org.JE.JE2.Utility;

import java.lang.ref.WeakReference;

public class MemoryReference <T>{
    private final WeakReference<T> reference;
    public MemoryReference(T object){
        reference = new WeakReference<>(object);
        
    }

    public boolean hasBeenCollected(){
        return reference.get() == null;
    }
}
