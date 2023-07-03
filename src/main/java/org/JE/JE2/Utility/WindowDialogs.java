package org.JE.JE2.Utility;

import org.lwjgl.PointerBuffer;
import org.lwjgl.system.MemoryUtil;
import org.lwjgl.util.tinyfd.TinyFileDialogs;

import java.io.File;
import java.nio.ByteBuffer;

public class WindowDialogs {

    public static String[] getFiles(String title, String defaultPath, String[] extensions, boolean multiFiles){
        PointerBuffer filterPatterns = MemoryUtil.memAllocPointer(extensions.length);
        for (String etx : extensions) {
            if(!etx.startsWith("*."))
                etx = "*." + etx;
            ByteBuffer filterPatternBuffer = MemoryUtil.memUTF8(etx);
            filterPatterns.put(filterPatternBuffer);
        }
        filterPatterns.flip();

        String fp = TinyFileDialogs.tinyfd_openFileDialog(title, defaultPath, filterPatterns, null, multiFiles);

        // free
        MemoryUtil.memFree(filterPatterns);
        if(fp == null)
            return new String[]{defaultPath};
        return fp.split("\\|");
    }

    public static File getFile(String title, String defaultPath, String[] extensions){
        return new File(getFiles(title,defaultPath,extensions,false)[0]);
    }

    public static void infoBox(String title, String message){
        TinyFileDialogs.tinyfd_messageBox(title,message,"ok","info",true);

    }
}
