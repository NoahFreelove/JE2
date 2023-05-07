package org.JE.JE2.Utility;

import org.JE.JE2.IO.Logging.Logger;

public class GarbageCollection {

    public static void takeOutDaTrash(){
        long beforeUsedMem=Runtime.getRuntime().totalMemory()-Runtime.getRuntime().freeMemory();
        System.gc();
        long afterUsedMem=Runtime.getRuntime().totalMemory()-Runtime.getRuntime().freeMemory();
        float actualMemUsed=Math.abs(afterUsedMem-beforeUsedMem) / 1000000f;
        Logger.log("Garbage Collection: " + actualMemUsed + " mb freed.", 0);
    }

    public static float getMemoryUsageMb(){
        return (Runtime.getRuntime().totalMemory()-Runtime.getRuntime().freeMemory())/1000000f;
    }
}
