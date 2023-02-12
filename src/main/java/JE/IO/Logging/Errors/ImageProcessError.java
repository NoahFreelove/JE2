package JE.IO.Logging.Errors;

public class ImageProcessError extends JE2Error {
    public ImageProcessError(){
        MESSAGE = "Unknown Image Process Error";
        NAME = "IMAGE PROCESS ERROR";
    }


    public ImageProcessError(boolean invalidFP)
    {
        NAME = "IMAGE PROCESS ERROR";
        if(invalidFP)
            MESSAGE = "Image Filepath is invalid";
    }
    public ImageProcessError(String message)
    {
        NAME = "IMAGE PROCESS ERROR";
        MESSAGE = "Error with Image Processing: " + message;
    }
}
