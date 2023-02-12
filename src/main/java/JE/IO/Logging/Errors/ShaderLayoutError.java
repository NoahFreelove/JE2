package JE.IO.Logging.Errors;

public class ShaderLayoutError extends ShaderError{
    public ShaderLayoutError(String message) {
        super(message);
        NAME = "SHADER LAYOUT ERROR";
    }
}
