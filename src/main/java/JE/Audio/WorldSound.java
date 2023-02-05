package JE.Audio;

import JE.Manager;

import org.joml.Vector2f;
import org.joml.Vector3f;

public class WorldSound extends AudioSourcePlayer {

    @Override
    public void update(){
        Vector2f cameraPos = Manager.getMainCamera().getAttachedObject().getTransform().position();
        Vector2f worldPos = getAttachedObject().getTransform().position();
        Vector3f distance = new Vector3f(worldPos.x - cameraPos.x, worldPos.y - cameraPos.y,0);
        // Calculate rolloff
        float rolloff = 1 / (distance.length() * distance.length()) * range;
        // if past range, set to 0
        if(rolloff > 1)
            rolloff = -1;
        setGain(rolloff);
    }
}
