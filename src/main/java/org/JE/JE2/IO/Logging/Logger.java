package org.JE.JE2.IO.Logging;

import org.JE.JE2.IO.Logging.Errors.JE2Error;

import java.util.ArrayList;
import java.util.Date;

public class Logger {

    public static boolean throwErrors = false;
    public static boolean logErrors = true;

    // may have performance hit
    public static boolean enableIgnoreList = false;
    public static ArrayList<Class> ignoreList = new ArrayList<>();

    public static boolean log = true;
    public static boolean logPetty = false;

    public static boolean showTime = false;
    public static boolean stackTrace = false;

    public static void log(String message){
        if(!log)
            return;

        if(showTime){
            System.out.println("[" + new Date().getTime() + "] " + message);
        }
        else {
            System.out.println(message);
        }
        if(stackTrace){
            // print caller of log function
            System.out.println("Called from: " + Thread.currentThread().getStackTrace()[2].getClassName() + "." + Thread.currentThread().getStackTrace()[2].getMethodName() + "()");
        }
    }
    public static void log(float f){
        log(f + "");
    }

    public static void log(int i){
        log(i + "");
    }

    public static void log(double d){
        log(d + "");
    }

    public static void log(boolean b){
        log(b + "");
    }

    public static void log(char c){
        log(c + "");
    }

    public static void log(Object o){
        log(o.toString());
    }

    public static void log(JE2Error error){
        if(!logErrors)
            return;

        if(enableIgnoreList)
            if(ignoreList.contains(error.getClass()))
                return;

        System.out.println("[" + new Date().getTime() + "] " + error.toString());
        if(throwErrors)
        {
            try {
                throw new Exception(error.toString());
            } catch (Exception e) {
                throw new RuntimeException(error.toString());
            }
        }
    }
}
