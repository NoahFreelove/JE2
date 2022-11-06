package JE.Logging.Errors;

import JE.Objects.Components.Component;

public class ComponentError extends JE2Error {
    public ComponentError(){
        MESSAGE = "Unknown Component Error";
        NAME = "COMPONENT ERROR";
    }

    public ComponentError(Component component)
    {
        NAME = "COMPONENT ERROR";
        MESSAGE = "Error with Component: " + component.getClass().getSimpleName();
    }

    public ComponentError(Component component, String message)
    {
        NAME = "COMPONENT ERROR";
        MESSAGE = "Error with Component: " + component.getClass().getSimpleName() + " : " + message;
    }
}
