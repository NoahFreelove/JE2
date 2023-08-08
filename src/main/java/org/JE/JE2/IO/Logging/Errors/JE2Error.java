package org.JE.JE2.IO.Logging.Errors;

import org.JE.JE2.IO.Logging.Logger;

public class JE2Error extends Exception {
    int level = Logger.ERROR;
    public JE2Error(){
        super("Unknown Error");
    }

    public JE2Error(Exception e){
        super(e);
    }
    public JE2Error(String message){
        super(message);
    }
    public JE2Error(String message, int level){
        super(message);
        this.level = level;
    }
}
