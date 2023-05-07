package org.JE.JE2.IO.Logging.Errors;

import org.JE.JE2.Objects.Scripts.Script;

public class ScriptError extends JE2Error {
    public ScriptError(){
        super("Unknown Script Error");
    }

    public ScriptError(Script script)
    {
        super("Error with Script: " + script.getClass().getSimpleName());
    }

    public ScriptError(Script script, String message)
    {
       super("Error with Script: " + script.getClass().getSimpleName() + " : " + message);
    }

    public ScriptError(String message)
    {
        super("Error with Script: " + message);
    }
}
