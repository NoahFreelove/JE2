package JE.Sample.Objects;

import JE.Objects.Scripts.Base.Script;
import JE.Rendering.Camera;
import org.joml.Vector2f;

public class PlayerScript extends Script {
    public Camera cameraRef;

    @Override
    public void start() {
        cameraRef = getAttachedObject().getScript(Camera.class);
        if(cameraRef !=null)
            cameraRef.positionOffset = new Vector2f(0.5f,0.5f);
    }
}
