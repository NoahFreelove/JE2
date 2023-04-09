package org.JE.JE2.Resources;

import org.JE.JE2.Annotations.Nullable;
import org.JE.JE2.Resources.Bundles.AudioBundle;
import org.JE.JE2.Resources.Bundles.ResourceBundle;
import org.JE.JE2.Resources.Bundles.TextureBundle;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Consumer;

public class ResourceManager implements Serializable {
    private final static CopyOnWriteArrayList<Resource<TextureBundle>> textures = new CopyOnWriteArrayList<>();
    private final static CopyOnWriteArrayList<Resource<AudioBundle>> sounds = new CopyOnWriteArrayList<>();

    public static void indexResource(Resource<?> resource){
        if(resource.type == TextureBundle.class){
            textures.add((Resource<TextureBundle>) resource);
        }
        if(resource.type == AudioBundle.class){
            sounds.add((Resource<AudioBundle>) resource);
        }
    }

    public static Resource<?> getByName(String name, Class<?> type){
        if(type == TextureBundle.class){
            for (Resource<TextureBundle> t : textures) {
                if(t.getName().equals(name)){
                    return t;
                }
            }
        }
        if(type == AudioBundle.class){
            for (Resource<AudioBundle> s : sounds) {
                if(s.getName().equals(name)){
                    return s;
                }
            }
        }
        return null;
    }

    public static Resource<?> getByID(int ID, Class<?> type){
        if(type == TextureBundle.class){
            for (Resource<TextureBundle> t : textures) {
                if(t.getID() == ID){
                    return t;
                }
            }
        }
        if(type == AudioBundle.class){
            for (Resource<AudioBundle> s : sounds) {
                if(s.getID() == ID){
                    return s;
                }
            }
        }
        return null;
    }

    public static Resource<?> getIfExists(Resource<?> resource){
        if(policy == ResourceLoadingPolicy.DONT_CHECK_IF_EXISTS)
            return null;

        if(resource.type == TextureBundle.class){
            Resource<TextureBundle> casted = (Resource<TextureBundle>)resource;
            for (Resource<TextureBundle> t : textures) {
                if(casted.equals(t)){
                    return t;
                }
            }
        }
        if(resource.type == AudioBundle.class){
            Resource<AudioBundle> casted = (Resource<AudioBundle>)resource;
            for (Resource<AudioBundle> s : sounds) {
                if(casted.equals(s)){
                    return s;
                }
            }
        }
        return null;
    }

    public static Resource<? extends ResourceBundle> getIfExists(Class<? extends ResourceBundle> clazz, String name, int ID){
        if(policy == ResourceLoadingPolicy.DONT_CHECK_IF_EXISTS)
            return null;

        // Check if texture or audio bundle

        if(clazz == TextureBundle.class){
            for (Resource<TextureBundle> t : textures) {
                if(policy== ResourceLoadingPolicy.CHECK_BY_NAME && t.getName().equals(name)){
                    return t;
                }
                if(policy== ResourceLoadingPolicy.CHECK_BY_ID && t.getID() == ID){
                    return t;
                }
            }
        }
        if(clazz == AudioBundle.class){
            for (Resource<AudioBundle> s : sounds) {
                if(s.getID() == ID || s.getName().equals(name)){
                    return s;
                }
            }
        }

        return null;
    }

    public static ResourceLoadingPolicy policy = ResourceLoadingPolicy.CHECK_BY_NAME;
}
