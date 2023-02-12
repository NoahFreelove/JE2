package JE.IO.Logging.Errors;

public class ResourceError extends JE2Error {
    protected String NAME;
    protected String MESSAGE;

    public ResourceError(){
        MESSAGE = "Unknown Resource Error";
        NAME = "RESOURCE ERROR";
    }
    public ResourceError(String message){
        MESSAGE = message;
        NAME = "RESOURCE ERROR";
    }
    public ResourceError(String name, String message){
        MESSAGE = message;
        NAME = name;
    }
}
