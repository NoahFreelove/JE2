package org.JE.JE2.IO.Logging.Errors;

public class IOError extends JE2Error {

    public IOError() {
        super("Unknown IO error");
    }

    public IOError(String message){
        super("IO Error: " + message);
    }
}
