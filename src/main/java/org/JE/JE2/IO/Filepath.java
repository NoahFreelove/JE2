package org.JE.JE2.IO;

import org.JE.JE2.IO.Logging.Logger;
import org.JE.JE2.Resources.DataLoader;

public class Filepath {

    public final boolean isClassLoaderPath;
    String absolute;
    String classLoaderPath;
    public boolean flag_flipTexture = true;
    public Filepath(String filepath, boolean isClassLoaderPath){
        this.classLoaderPath = filepath;
        this.isClassLoaderPath = isClassLoaderPath;
        if(isClassLoaderPath){
            this.absolute = DataLoader.getClassLoaderAbsoluteFilePath(filepath);
        }
        else
            this.absolute = filepath;
        /*System.out.println("filepath: " + filepath);
        System.out.println("absolute: " + absolute);
        System.out.println("bytePath: " + classLoaderPath);
        System.out.println("isClassLoaderPath: " + this.isClassLoaderPath);
        System.out.println("______________");*/
    }
    public Filepath(String filepath, boolean isClassLoaderPath, boolean flag_flipTexture){
        this(filepath,isClassLoaderPath);
        this.flag_flipTexture = flag_flipTexture;
    }

    public Filepath(String filepath){
        this(filepath,false);
    }

    public String getPath(boolean forClassLoader)
    {
        if(forClassLoader)
        {
            if(!this.isClassLoaderPath)
                Logger.log("WARNING: Filepath: " + absolute + " : does not have a class loader path, returning absolute one...", Logger.WARN);
            return this.classLoaderPath;
        }
        else
            return absolute;
    }

    public static Filepath empty(){
        return new Filepath("",false);
    }


}
