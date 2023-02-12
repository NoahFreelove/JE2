package JE.IO.Logging.Errors;

import JE.Objects.Scripts.Base.Script;

public class ScriptError extends JE2Error {
    public ScriptError(){
        MESSAGE = "Unknown Script Error";
        NAME = "SCRIPT ERROR";
    }

    public ScriptError(Script script)
    {
        NAME = "SCRIPT ERROR";
        MESSAGE = "Error with Script: " + script.getClass().getSimpleName();
    }

    public ScriptError(Script script, String message)
    {
        NAME = "SCRIPT ERROR";
        MESSAGE = "Error with Script: " + script.getClass().getSimpleName() + " : " + message;
    }

    public ScriptError(String message)
    {
        NAME = "SCRIPT ERROR";
        MESSAGE = "Error with Script: " + message;
    }
}
