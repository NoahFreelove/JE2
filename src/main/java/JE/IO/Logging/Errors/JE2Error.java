package JE.IO.Logging.Errors;

public class JE2Error {
    protected String NAME;
    protected String MESSAGE;
    protected boolean petty = false;

    public JE2Error(){
        MESSAGE = "Unknown Error";
        NAME = "ERROR";
    }
    public JE2Error(String message){
        MESSAGE = message;
        NAME = "ERROR";
    }
    public JE2Error(String name, String message){
        MESSAGE = message;
        NAME = name;
    }
    public String toString(){
        return NAME + " -> " + MESSAGE;
    }
}
