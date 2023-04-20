package org.JE.JE2.Objects.Scripts.CameraEffects;

import org.JE.JE2.Objects.Scripts.Script;
import org.JE.JE2.Rendering.Camera;
import org.joml.Vector2f;

public class CameraShake extends Script {
    public float magnitude = 1f;
    public Camera cameraReference;

    public CameraShake(){
        super();
    }
    @Override
    public void update(){
        if(cameraReference == null)
            return;
        cameraReference.viewportSize.x = (float) ((Math.random() * 10 - 2)*0.01*magnitude);
        cameraReference.viewportSize.y = (float) ((Math.random() * 10 - 2)*0.01*magnitude);
    }

    @Override
    public void setActive(boolean newState) {
        if(cameraReference !=null)
        {
            if(!newState){
                cameraReference.viewportSize.x = 0;
                cameraReference.viewportSize.y = 0;
            }
        }
        super.setActive(newState);
    }
}