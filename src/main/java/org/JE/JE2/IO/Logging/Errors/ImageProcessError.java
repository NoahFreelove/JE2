package org.JE.JE2.IO.Logging.Errors;

public class ImageProcessError extends JE2Error {
    public ImageProcessError(){
       super("Unknown Image Processing Error");
    }

    public ImageProcessError(String message)
    {
        super("Error with Image Processing: " + message);
    }
}
