package JE.Objects.Audio;

import JE.Audio.AudioSourcePlayer;
import JE.Manager;
import JE.Objects.Base.GameObject;
import JE.Objects.Gizmos.Gizmo;
import JE.UI.UIElements.Style.Color;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;

import static org.lwjgl.opengl.GL11.GL_LINES;

public class WorldSound extends GameObject {
    public final AudioSourcePlayer soundPlayer;
    public WorldSound(String filepath, boolean loops){
        addComponent(soundPlayer = new AudioSourcePlayer());

    }

    @Override
    public void update(){
        Vector2f cameraPos = Manager.getCamera().parentObject.getTransform().position();
        Vector2f worldPos = getTransform().position();
        Vector3f distance = new Vector3f(worldPos.x - cameraPos.x, worldPos.y - cameraPos.y,0);
        // Calculate rolloff
        float rolloff = 1 / (distance.length() * distance.length()) * soundPlayer.range;
        // if past range, set to 0
        if(rolloff > 1)
            rolloff = -1;
        soundPlayer.setGain(rolloff);
    }

    public Gizmo getRangeGizmo(){
        Vector2f[] vertices = new Vector2f[360];
        for(int i = 0; i < 360; i++){
            vertices[i] = new Vector2f((float)Math.cos(Math.toRadians(i)) * soundPlayer.range, (float)Math.sin(Math.toRadians(i)) * soundPlayer.range);
        }
        Gizmo rangeGizmo = new Gizmo();
        rangeGizmo.setVertices(vertices);
        rangeGizmo.setBaseColor(Color.createColor(1,1,1,1));
        rangeGizmo.setDrawMode(GL_LINES);
        rangeGizmo.getTransform().setPosition(getTransform().position());
        return rangeGizmo;
    }
}
