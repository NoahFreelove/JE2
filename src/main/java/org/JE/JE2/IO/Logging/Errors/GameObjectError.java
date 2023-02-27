package org.JE.JE2.IO.Logging.Errors;

import org.JE.JE2.Objects.GameObject;

public class GameObjectError extends JE2Error {
    public GameObjectError(){
        MESSAGE = "Unknown GameObject Error";
        NAME = "GAMEOBJECT ERROR";
    }

    public GameObjectError(GameObject gameObject)
    {
        NAME = "GAMEOBJECT ERROR";
        MESSAGE = "Error with GameObject: " + gameObject.toString();
    }

    public GameObjectError(String message)
    {
        NAME = "GAMEOBJECT ERROR";
        MESSAGE = "Error with GameObject: " + message;
    }

    public GameObjectError(GameObject gameObject, String message)
    {
        NAME = "GAMEOBJECT ERROR";
        MESSAGE = "Error with: " + gameObject.toString() + " : " + message;
    }
}