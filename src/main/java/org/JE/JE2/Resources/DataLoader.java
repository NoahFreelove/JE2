package org.JE.JE2.Resources;

import org.JE.JE2.Annotations.JarSafe;
import org.JE.JE2.IO.Filepath;
import org.JE.JE2.IO.Logging.Errors.JE2Error;
import org.JE.JE2.IO.Logging.Logger;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
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

        if(result == null) {
            String rootPath = DataLoader.class.getClassLoader().getResource("").getPath().substring(1);
            return rootPath + path;
        }
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
            try (InputStream stream = DataLoader.class.getResourceAsStream("/" + path)) {
                if(stream == null)
                    return new byte[0];
                return getBytesFromStream(stream);
            }
        }
        catch (Exception e){
            return new byte[]{};
        }
    }

    public static byte[] getFileData(String path){
        File f = new File(path);
        if(!f.exists())
            return new byte[0];

        try (InputStream stream = f.toURI().toURL().openStream()) {
            return getBytesFromStream(stream);
        }
        catch (Exception e){
            return new byte[]{};
        }
    }

    private static byte[] getBytesFromStream(InputStream stream){
        byte[] arr;
        try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()) {

            // buffer should be large enough to hold the entire file
            byte[] buffer = new byte[stream.available() + 1];
            int length;
            while ((length = stream.read(buffer)) != -1) {
                byteArrayOutputStream.write(buffer, 0, length);
            }
            arr = byteArrayOutputStream.toByteArray();
            stream.close();
        } catch (IOException e) {
            return new byte[0];
        }
        return arr;
    }

    public static byte[] getBytes(Filepath filepath){
        if(filepath.isClassLoaderPath){
            return getClassLoaderBytes(filepath.getPath(true));
        }
        else
            return getFileData(filepath.getPath(false));
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
            Logger.log(new JE2Error(e));
        }
        return sb.toString().split("\n");
    }
}
