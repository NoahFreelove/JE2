package org.JE.JE2.Utility;

import org.JE.JE2.IO.Logging.Logger;

public class GarbageCollection {

    public static void takeOutDaTrash(){
        // get current memory usage then call Systen.gc() then get after memory usage and compare

        long beforeUsedMem=Runtime.getRuntime().totalMemory()-Runtime.getRuntime().freeMemory();
        System.gc();
        long afterUsedMem=Runtime.getRuntime().totalMemory()-Runtime.getRuntime().freeMemory();
        long actualMemUsed=afterUsedMem-beforeUsedMem;
        Logger.log("Garbage Collection: " + actualMemUsed + " bytes freed.", true);
    }
}
