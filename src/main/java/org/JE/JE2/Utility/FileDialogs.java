package org.JE.JE2.Utility;

import org.lwjgl.PointerBuffer;
import org.lwjgl.system.MemoryUtil;
import org.lwjgl.util.tinyfd.TinyFileDialogs;

import java.io.File;
import java.nio.ByteBuffer;

public class FileDialogs {

    public static File getFile(String title, String defaultPath, String[] extensions, boolean multiFiles){
        PointerBuffer filterPatterns = MemoryUtil.memAllocPointer(extensions.length);

        for (String etx : extensions) {
            ByteBuffer filterPatternBuffer = MemoryUtil.memUTF8(etx);
            filterPatterns.put(filterPatternBuffer);
        }
        filterPatterns.flip();

        String fp = TinyFileDialogs.tinyfd_openFileDialog(title, defaultPath, filterPatterns, null, multiFiles);

        // free
        MemoryUtil.memFree(filterPatterns);
        if(fp == null)
            return new File(defaultPath);
        return new File(fp);
    }

    /*public static void getFileAsync(String title, String defaultPath, String[] extensions, boolean multiFiles, RunnableGeneric<File> onComplete){
        Thread fileThread = new Thread(()->{
            onComplete.invoke(getFile(title,defaultPath,extensions,multiFiles));
        });
        Window.fileDialogs.add(fileThread);
        fileThread.start();
    }*/
}
