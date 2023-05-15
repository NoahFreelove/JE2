package org.JE.JE2.IO.Logging;

import org.JE.JE2.IO.Logging.Errors.JE2Error;
import org.JE.JE2.Window.Window;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

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

    public static int logLevel = 0;

    private static LogEntry[] logEntries = new LogEntry[1000];
    public static int logEntryIndex = 0;

    public static int DEFAULT_LOG_LEVEL = 10;
    public static int DEFAULT_ERROR_LOG_LEVEL = 20;
    public static int DEFAULT_MAX_LOG_LEVEL = Integer.MAX_VALUE-1;

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

        if(logLevel > level)
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
        log(message, DEFAULT_LOG_LEVEL);
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
    log(o.toString(),DEFAULT_LOG_LEVEL);
    }

    public static void log(Object o, int level){
        log(o.toString(), level);
    }

    public static void log(JE2Error error){
        log(error,DEFAULT_ERROR_LOG_LEVEL);
    }

    public static void log(JE2Error error, int level){
        if(!masterLog)
            return;
        if(!logErrors && !quietLog)
            return;
        if(logLevel > level)
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
                System.err.println("[" + time  + "] " + error.getMessage());
            appendedMessage += "[" + time  + "] " + error.getMessage();
        }
        else if (!quietLog) {
            System.err.println(error.getMessage());
        }
        appendedMessage += error.getMessage();

        logEntries[logEntryIndex] = new LogEntry(appendedMessage, level);
        logEntryIndex++;

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
        logLevel = 0;
        log = true;
        masterLog = true;
        quietLog = false;
        logErrors = true;
        showTime = true;
        stackTrace = true;
    }
}
