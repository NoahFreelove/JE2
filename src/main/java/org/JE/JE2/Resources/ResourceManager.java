package org.JE.JE2.Resources;

import org.JE.JE2.Annotations.JarSafe;
import org.JE.JE2.IO.Filepath;
import org.JE.JE2.Objects.Audio.AudioSourcePlayer;
import org.JE.JE2.Rendering.Texture;
import org.JE.JE2.Resources.Bundles.AudioBundle;
import org.JE.JE2.Resources.Bundles.ResourceBundle;
import org.JE.JE2.Resources.Bundles.TextureBundle;
import org.JE.JE2.Utility.Triplet;

import java.io.Serializable;
import java.util.concurrent.CopyOnWriteArrayList;

public class ResourceManager implements Serializable {
    private final static CopyOnWriteArrayList<Resource<TextureBundle>> textures = new CopyOnWriteArrayList<>();
    private final static CopyOnWriteArrayList<Resource<AudioBundle>> sounds = new CopyOnWriteArrayList<>();
    public final static CopyOnWriteArrayList<Triplet<String,String,Class<? extends ResourceBundle>>> queuedWarmupAssets = new CopyOnWriteArrayList<>();

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
                /*if(policy== ResourceLoadingPolicy.CHECK_BY_ID && t.getID() == ID){
                    return t;
                }*/
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

    /**
     * This method trusts that you won't give it duplicate resources!
     * @param assets The byte paths of the assets to load
     * @param names Their names to be referenced when checking if they exist
     * @param classes Their respective Bundle type
     */
    @JarSafe
    public static void warmupAssets(String[] names, Filepath[] assets, Class<? extends ResourceBundle>[] classes){
        int prevPolicy = policy.ordinal();
        policy = ResourceLoadingPolicy.DONT_CHECK_IF_EXISTS;
        if(assets.length != names.length || assets.length != classes.length)
            return;

        for (int i = 0; i < assets.length; i++) {
            //System.out.println("Warming up asset: " + names[i] + " with path: " + assets[i] + " and class: " + classes[i].getName());
            warmupAsset(names[i], assets[i], classes[i]);
        }
        policy = ResourceLoadingPolicy.values()[prevPolicy];
    }

    @JarSafe
    public static void warmupAssets(String[] names, Filepath[] assets, Class<?extends ResourceBundle> clazz){
        int prevPolicy = policy.ordinal();
        policy = ResourceLoadingPolicy.DONT_CHECK_IF_EXISTS;
        if(assets.length != names.length)
            return;

        for (int i = 0; i < assets.length; i++) {
            //System.out.println("Warming up asset: " + names[i] + " with path: " + assets[i] + " and class: " + classes[i].getName());
            warmupAsset(names[i], assets[i], clazz);
        }
        policy = ResourceLoadingPolicy.values()[prevPolicy];
    }
    @JarSafe
    public static void warmupAssets(Triplet<String, Filepath, Class<? extends ResourceBundle>>[] assets){
        for (Triplet<String,Filepath,Class<? extends ResourceBundle>> t: assets) {
            warmupAsset(t.x,t.y,t.z);
        }
    }

    @JarSafe
    public static void warmupAsset(String name, Filepath asset, Class<? extends ResourceBundle> clazz){
        if(clazz== TextureBundle.class)
        {
            Texture.checkExistElseCreate(name,-1, asset);
        } else if (clazz == AudioBundle.class) {
            AudioSourcePlayer.checkExistElseCreate(name,-1, asset);
        }
    }

    public static void warmupQueue(){
        warmupAssets(queuedWarmupAssets.toArray(new Triplet[0]));
    }

    public static void clearTextureCache(){
        textures.clear();
    }
}
