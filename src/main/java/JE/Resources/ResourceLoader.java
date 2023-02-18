package JE.Resources;

import JE.Annotations.Nullable;

import java.io.File;
import java.net.URL;

public class ResourceLoader {
    @Nullable
    public static String getResourcePath(String path){
        try {
            URL resourceURL = ResourceLoader.class.getResource(path);
            File resourceFile = new File(resourceURL.getPath());
            return resourceFile.getAbsolutePath();
        }
        catch (Exception e){
            return null;
        }
    }

    @Nullable
    public static String get(String path){
        return getResourcePath("/" + path);
    }
}
