package JE.Logging;

import JE.Logging.Errors.JE2Error;

import java.util.Date;

public class Logger {

    public static boolean throwErrors = false;

    public static void log(String message){
        System.out.println("[" + new Date().getTime() + "] " + message);
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
