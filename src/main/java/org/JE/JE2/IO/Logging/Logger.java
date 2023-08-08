package org.JE.JE2.IO.Logging;

import org.JE.JE2.IO.Logging.Errors.JE2Error;
import org.JE.JE2.Utility.RunnableArg;
import org.JE.JE2.Utility.RunnableGeneric;
import org.joml.Vector2f;

import java.util.ArrayList;
import java.util.Calendar;

public class Logger {

    public static boolean masterLog = true;

    public static boolean quietLog = false;

    public static boolean throwErrors = false;
    public static boolean logErrors = true;

    // may have performance hit
    public static boolean enableIgnoreList = false;
    public static ArrayList<Class<?>> ignoreList = new ArrayList<>();

    public static boolean log = true;

    public static boolean showTime = false;
    public static boolean stackTrace = false;

    public static int logThreshold = 0;

    private static LogEntry[] logEntries = new LogEntry[1000];
    public static int logEntryIndex = 0;

    private static LogEntry[] errorEntries = new LogEntry[1000];
    public static int errorEntriesIndex = 0;
    public static ArrayList<RunnableGeneric<JE2Error>> errorListeners = new ArrayList<>();

    public static int DEBUG = 0;
    public static int LOG = 10;
    public static int WARN = 15;
    public static int ERROR = 20;
    public static int MAX = Integer.MAX_VALUE-1;

    public static void log(String message, int level){
        if(!masterLog)
            return;
        if(!log && !quietLog)
            return;

        if(enableIgnoreList){

            for(Class<?> c : ignoreList){
                String className = Thread.currentThread().getStackTrace()[2].getClassName();
                if(!className.equals("org.JE.JE2.IO.Logging.Logger")){
                    className = Thread.currentThread().getStackTrace()[3].getClassName();
                }
                if(className.equals(c.getName()))
                    return;
            }
        }

        if(logThreshold > level)
            return;

        if(logEntryIndex >= logEntries.length){
            LogEntry[] newLogEntries = new LogEntry[logEntries.length * 2];
            System.arraycopy(logEntries, 0, newLogEntries, 0, logEntries.length);
            logEntries = newLogEntries;
        }

        String appendedMessage = "";

        if(showTime){
            String time = Calendar.getInstance().get(Calendar.HOUR_OF_DAY) + ":" + Calendar.getInstance().get(Calendar.MINUTE) + ":" + Calendar.getInstance().get(Calendar.SECOND);
            if(!quietLog)
                System.out.println("[" + time + "] " + message);
            appendedMessage += "[" + time + "] ";
        }
        else if(!quietLog) {
            System.out.println(message);
        }
        appendedMessage += message;

        if(stackTrace){
            // print caller of log function
            String className = Thread.currentThread().getStackTrace()[2].getClassName();
            if(!className.equals("org.JE.JE2.IO.Logging.Logger"))
            {
                if(!quietLog)
                    System.out.println("Called from: " + className + "." + Thread.currentThread().getStackTrace()[2].getMethodName() + "()");
                appendedMessage += " Called from: " + className + "." + Thread.currentThread().getStackTrace()[2].getMethodName() + "()";
            }
            else
            {
                className = Thread.currentThread().getStackTrace()[3].getClassName();
                if(!quietLog)
                    System.out.println("Called from: " + className + "." + Thread.currentThread().getStackTrace()[3].getMethodName() + "()");
                appendedMessage += " Called from: " + className + "." + Thread.currentThread().getStackTrace()[3].getMethodName() + "()";
            }
        }

        logEntries[logEntryIndex] = new LogEntry(appendedMessage, level);
        logEntryIndex++;
    }

    public static void log(String message){
        log(message, LOG);
    }

    public static void log(float f){
        log(String.valueOf(f));
    }

    public static void log(int i){
        log(String.valueOf(i));
    }

    public static void log(double d){
        log(String.valueOf(d));
    }

    public static void log(boolean b){
        log(String.valueOf(b));
    }

    public static void log(char c){
        log(String.valueOf(c));
    }

    public static void log(Object o){
        log(o.toString(), LOG);
    }
    public static void log(Vector2f vec){
        log("X: " + vec.x() + " Y:" + vec.y());
    }
    public static void log(Vector2f vec, int decimals){

        float x = vec.x();
        float y = vec.y();

        x = (float) (Math.round(x * Math.pow(10, decimals)) / Math.pow(10, decimals));
        y = (float) (Math.round(y * Math.pow(10, decimals)) / Math.pow(10, decimals));

        log("X: " + x + " Y:" + y);
    }
    public static void log(String var1, float val1, String var2, float val2){

        log(var1 + ": " + val1 + " | " + var2 + ": " + val2);
    }
    public static void log(Object o, int level){
        log(o.toString(), level);
    }

    public static void log(JE2Error error){
        log(error, ERROR);
    }

    public static void log(JE2Error error, int level){
        if(!masterLog)
            return;
        if(!logErrors)
            return;
        if(logThreshold > level)
            return;

        if(errorEntriesIndex >= errorEntries.length){
            LogEntry[] newLogEntries = new LogEntry[errorEntries.length * 2];
            System.arraycopy(errorEntries, 0, newLogEntries, 0, errorEntries.length);
            errorEntries = newLogEntries;
        }
        String appendedMessage = "";
        if(showTime){
            String time = Calendar.getInstance().get(Calendar.HOUR_OF_DAY) + ":" + Calendar.getInstance().get(Calendar.MINUTE) + ":" + Calendar.getInstance().get(Calendar.SECOND);
            if(!quietLog)
                System.err.println("[" + time  + "] " + error.getMessage());
            appendedMessage += "[" + time  + "] " + error.getMessage();
        }
        else if (!quietLog) {
            System.err.println(error.getMessage());
        }
        appendedMessage += error.getMessage();

        errorEntries[errorEntriesIndex] = new LogEntry(appendedMessage, level);
        errorEntriesIndex++;

        errorListeners.forEach(listener -> listener.invoke(error));
        if(throwErrors)
        {
            throw new RuntimeException(error);
        }
    }

    public static LogEntry[] getEntries(){
        LogEntry[] formattedArray = new LogEntry[logEntryIndex];
        System.arraycopy(logEntries, 0, formattedArray, 0, logEntryIndex);
        return formattedArray;
    }

    public static void disableAllLogging(){
        log = false;
        logErrors = false;
    }

    public static void enableAllLogging(){
        log = true;
        logErrors = true;
    }

    public static void logEverything(){
        logThreshold = 0;
        log = true;
        masterLog = true;
        quietLog = false;
        logErrors = true;
        showTime = true;
        stackTrace = true;
    }
}
