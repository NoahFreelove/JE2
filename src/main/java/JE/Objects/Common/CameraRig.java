package JE.Objects.Common;

import JE.Objects.Base.GameObject;
import JE.Objects.Base.Identity;
import JE.Rendering.Camera;

public class CameraRig extends GameObject {
    public Camera camera;
    public CameraRig(){
        super();
        addComponent(camera = new Camera());
        camera.parentObject = this;
    }
}

