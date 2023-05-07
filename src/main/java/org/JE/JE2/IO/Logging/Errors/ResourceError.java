package org.JE.JE2.IO.Logging.Errors;

import org.JE.JE2.Resources.Resource;

public class ResourceError extends JE2Error {
    public ResourceError(){
        super("Unknown Resource Error");
    }
    public ResourceError(String message){
        super("Resource Error: " + message);
    }
    public ResourceError(String message, Resource<?> resource){
        super("Resource Error: " + message + " with resource: " + resource.getName() + " ID: " + resource.getID() + " Type: " + resource.type.getSimpleName());
    }
}
