package org.JE.JE2.IO.Logging.Errors;

public class ShaderError extends JE2Error {
    public static JE2Error invalidProgramIDError;
    static {
        invalidProgramIDError = new JE2Error("Shader Error: INVALID PROGRAM ID", 0);
    }
    public ShaderError(){
        super("Unknown Shader Error");
    }

    public ShaderError(String message){
        super("Shader Error: " + message);
    }

    public ShaderError(String message, String vertexSource, String fragmentSource){
        super("Error with Shader: " + message + " :\nVERTEX\n" + vertexSource + "\nFRAGMENT\n" + fragmentSource);
    }
}
