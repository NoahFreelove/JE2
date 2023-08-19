package org.JE.JE2.Resources;

import org.JE.JE2.Annotations.JarSafe;
import org.JE.JE2.IO.Filepath;
import org.JE.JE2.IO.Logging.Errors.JE2Error;
import org.JE.JE2.IO.Logging.Logger;
import org.JE.JE2.Objects.GameObject;
import org.JE.JE2.Scene.Scene;

import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.function.Consumer;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

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

    public static String[] readTextFile(Filepath path){
        return readTextFile(path.getDefault());
    }

    public static Scene zipToScene(Filepath fp){
        File file = new File(fp.getDefault());
        if(!file.exists()){
            Logger.log("File does not exist to unzip scene from: " + fp.getDefault(), Logger.WARN);
            return new Scene();
        }

        List<String> list = processZipAndExtractTextFiles(readZipFileIntoMemory(file.getAbsolutePath()));
        Scene scene = new Scene();
        list.forEach(s -> {
            GameObject go = GameObject.load(s.split("\n"));
            scene.add(go);
        });
        return scene;
    }

    public static byte[] readZipFileIntoMemory(String zipFilePath) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];

        try (InputStream is = new FileInputStream(zipFilePath)) {
            int bytesRead;
            while ((bytesRead = is.read(buffer)) != -1) {
                baos.write(buffer, 0, bytesRead);
            }
        }
        catch (IOException e){
            Logger.log(new JE2Error("IO Error when reading scene zip: " + e.getMessage()));
            return new byte[0];
        }

        return baos.toByteArray();
    }

    public static List<String> processZipAndExtractTextFiles(byte[] zipData) {
        List<String> textFileContents = new ArrayList<>();

        try (ByteArrayInputStream bais = new ByteArrayInputStream(zipData);
             ZipInputStream zis = new ZipInputStream(bais)) {

            ZipEntry entry;
            while ((entry = zis.getNextEntry()) != null) {
                if (!entry.isDirectory() && entry.getName().endsWith(".txt")) {
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    byte[] buffer = new byte[1024];
                    int bytesRead;

                    while ((bytesRead = zis.read(buffer)) != -1) {
                        baos.write(buffer, 0, bytesRead);
                    }

                    textFileContents.add(baos.toString("UTF-8"));
                }

                zis.closeEntry();
            }
        }
        catch (IOException e){
            Logger.log(new JE2Error("IO Error when extracting scene zip: " + e.getMessage()));
        }

        return textFileContents;
    }
}
