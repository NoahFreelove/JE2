package JE.Resources;

import JE.Annotations.Nullable;

import java.io.Serializable;
import java.util.ArrayList;

public class ResourceManager implements Serializable {
    public static ArrayList<Resource> textures = new ArrayList<>();
    public static ArrayList<Resource> sounds = new ArrayList<>();

    @Nullable
    public Resource getTextureByName(String name)
    {
        for (Resource texture : textures) {
            if(texture.name.equals(name))
                return texture;
        }
        return null;
    }

    @Nullable
    public Resource getSoundByName(String name)
    {
        for (Resource sound : sounds) {
            if(sound.name.equals(name))
                return sound;
        }
        return null;
    }
}
