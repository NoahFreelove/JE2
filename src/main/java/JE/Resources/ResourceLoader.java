package JE.Resources;

import JE.Annotations.Nullable;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
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

    public static byte[] getResource(String path){
        try {
            InputStream stream = ResourceLoader.class.getResourceAsStream(path);
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int length;
            while ((length = stream.read(buffer)) != -1) {
                byteArrayOutputStream.write(buffer, 0, length);
            }

            return byteArrayOutputStream.toByteArray();
        }
        catch (Exception e){
            return new byte[]{};
        }
    }

    /**
     * You can use this to get resources as file paths.
     * This is not recommended for getting resources from a jar as it will not work (from my testing)
     * @param path The path to the resource from the root of the project
     * @return The absolute path to the resource
     */
    @Nullable
    public static String get(String path){
        return getResourcePath("/" + path);
    }

    /**
     * If compiling to a jar, this should be used to get resources
     * @param path The path to the resource from the root of the jar
     * @return The resource as a byte array
     */
    public static byte[] getBytes(String path){
        return getResource("/" + path);
    }
}
