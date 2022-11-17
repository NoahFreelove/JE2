package JE.Logging.Errors;

public class ShaderError extends JE2Error {
    public ShaderError(){
        MESSAGE = "Unknown Shader Error";
        NAME = "SHADER ERROR";
    }

    public ShaderError(String message){
        MESSAGE = message;
        NAME = "SHADER ERROR";
    }

    public ShaderError(String message, String shaderSource)
    {
        NAME = "SHADER ERROR";
        MESSAGE = "Error with Shader: " + message + " :\n" + shaderSource;
    }

    public ShaderError(String shaderSource, boolean vertexShader){
        NAME = "SHADER COMPILE ERROR";
        MESSAGE = (vertexShader? "Vertex" : "Fragment") + " Shader Compile Error. Source: \n" + shaderSource;

    }

}
