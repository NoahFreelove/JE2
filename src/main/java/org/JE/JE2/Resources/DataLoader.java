package org.JE.JE2.Resources;

import org.JE.JE2.Annotations.JarSafe;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.util.Scanner;

public final class DataLoader {

    @JarSafe
    public static File getClassLoaderFile(String path){
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
    public static String[] getClassLoaderBytesAsString(String path){
        return new String(getClassLoaderBytes(path)).split("\n");
    }

    /**
     * You can use this to get resources as file paths.
     * This is not recommended for getting resources from a jar as it will not work (from my testing)
     * @param path The path to the resource from the root of the project
     * @return The absolute path to the resource
     */
    public static String getClassLoaderAbsoluteFilePath(String path){
        File result = getClassLoaderFile("/" + path);

        if(result == null)
            return "";
        else return result.getAbsolutePath();
    }

    /**
     * If compiling to a jar, this should be used to get resources
     * @param path The path to the resource from the root of the jar
     * @return The resource as a byte array
     */
    @JarSafe
    public static byte[] getClassLoaderBytes(String path){
        try {
            InputStream stream = DataLoader.class.getResourceAsStream("/" + path);
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

    public static byte[] getFileData(String path){
        File f = new File(path);
        if(!f.exists())
            return new byte[0];
        // Read file to byte array
        try {
            InputStream stream = f.toURI().toURL().openStream();
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
