package org.JE.JE2.SampleScripts;

import org.JE.JE2.Objects.Scripts.Script;
import org.JE.JE2.Objects.Scripts.Serialize.Load;
import org.JE.JE2.Objects.Scripts.Serialize.Save;
import org.JE.JE2.Rendering.Camera;
import org.joml.Vector2f;

import java.util.HashMap;

public class PlayerScript extends Script implements Save, Load {
    public Camera cameraRef;

    @Override
    public void start() {
        cameraRef = getAttachedObject().getScript(Camera.class);
        if(cameraRef !=null) {
            cameraRef.checkRenderDistance = false;
            cameraRef.positionOffset = new Vector2f(0.5f, 0.5f);
        }
    }

    @Override
    public void load(HashMap<String, String> data) {

    }

    @Override
    public HashMap<String, String> save() {
        return new HashMap<>();
    }
}
