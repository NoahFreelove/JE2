package org.JE.JE2.Resources;

import org.JE.JE2.Annotations.JarSafe;
import org.JE.JE2.Annotations.Nullable;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.util.Scanner;

public class DataLoader {
    public static String getDataFilePath(String path){
        try {
            return getDataFile(path).getAbsolutePath();
        }
        catch (Exception e){
            return "";
        }
    }

    @Nullable
    @JarSafe
    public static File getDataFile(String path){
        try {
            URL resourceURL = DataLoader.class.getResource(path);
            File resourceFile = new File(resourceURL.getPath());
            return resourceFile;
        }
        catch (Exception e){
            return null;
        }
    }

    @JarSafe
    public static byte[] getDataBytes(String path){
        try {
            InputStream stream = DataLoader.class.getResourceAsStream(path);
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

            // buffer should be large enough to hold the entire file
            byte[] buffer = new byte[stream.available() + 1];
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

    @JarSafe
    public static String[] getBytesAsString(String path){
        return new String(getBytes(path)).split("\n");
    }

    /**
     * You can use this to get resources as file paths.
     * This is not recommended for getting resources from a jar as it will not work (from my testing)
     * @param path The path to the resource from the root of the project
     * @return The absolute path to the resource
     */
    @Nullable
    public static String get(String path){
        return getDataFilePath("/" + path);
    }

    /**
     * If compiling to a jar, this should be used to get resources
     * @param path The path to the resource from the root of the jar
     * @return The resource as a byte array
     */
    @JarSafe
    public static byte[] getBytes(String path){
        return getDataBytes("/" + path);
    }

    public static String[] readTextFile(String path){
        // Use a scanner to read the file
        StringBuilder sb = new StringBuilder();
        try {
            Scanner fileScanner = new Scanner(new File(path));
            while (fileScanner.hasNextLine()) {
                sb.append(fileScanner.nextLine()).append("\n");
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return sb.toString().split("\n");
    }
}
