package JE.Logging;

import JE.Logging.Errors.JE2Error;

import java.util.Date;

public class Logger {

    public static boolean throwErrors = false;
    public static boolean log = true;
    public static boolean showTime = false;
    public static boolean stackTrace = false;

    public static void log(String message){
        if(!log)
            return;
        if(stackTrace){
            // print caller of log function
            System.out.println("Called from: " + Thread.currentThread().getStackTrace()[2].getClassName() + "." + Thread.currentThread().getStackTrace()[2].getMethodName() + "()");
        }
        if(showTime){
            System.out.println("[" + new Date().getTime() + "] " + message);
        }
        else {
            System.out.println(message);
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
