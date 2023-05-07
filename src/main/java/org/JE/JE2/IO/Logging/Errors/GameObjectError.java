package org.JE.JE2.IO.Logging.Errors;

import org.JE.JE2.Objects.GameObject;

public class GameObjectError extends JE2Error {
    public GameObjectError(){
        super("Unknown GameObject Error");
    }

    public GameObjectError(GameObject gameObject)
    {
        super("Error with GameObject: " + gameObject.toString());
    }

    public GameObjectError(String message)
    {
        super(message);
    }

    public GameObjectError(GameObject gameObject, String message)
    {
        super("Error with GameObject: " + gameObject.toString() + " : " + message);
    }
}
