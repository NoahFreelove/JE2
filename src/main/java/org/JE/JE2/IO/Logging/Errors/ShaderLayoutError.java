package org.JE.JE2.IO.Logging.Errors;

public class ShaderLayoutError extends ShaderError{

    public ShaderLayoutError(){
        super("Unknown Shader Layout Error");
    }

    public ShaderLayoutError(String message) {
        super("Shader Layout Error: " + message);
    }
}
