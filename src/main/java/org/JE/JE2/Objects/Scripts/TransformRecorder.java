package org.JE.JE2.Objects.Scripts;

import org.joml.Vector2f;
import org.joml.Vector3f;

import java.util.ArrayList;

public class TransformRecorder extends Script {

    ArrayList<Vector2f> positions = new ArrayList<>();
    ArrayList<Vector3f> rotations = new ArrayList<>();
    ArrayList<Vector2f> scales = new ArrayList<>();

    public boolean recording = false;
    public boolean playing = false;
    public int currentFrame = 0;
    @Override
    public void update() {
        if(recording)
        {
            positions.add(new Vector2f(getAttachedObject().getTransform().position()));
            rotations.add(new Vector3f(getAttachedObject().getTransform().rotation()));
            scales.add(new Vector2f(getAttachedObject().getTransform().scale()));
            currentFrame++;
        } else if (playing) {
            if(currentFrame <= 1)
            {
                currentFrame = 1;
            }
            if(positions.size()<1)
                return;
            /*System.out.println("size: " + positions.size());
            System.out.println(currentFrame-1);*/
            getAttachedObject().getTransform().setPosition(positions.get(currentFrame-1));
            getAttachedObject().getTransform().setRotation(rotations.get(currentFrame-1));
            getAttachedObject().getTransform().setScale(scales.get(currentFrame-1));
            currentFrame--;
        }

    }

    public void setFrame(int i)
    {
        currentFrame = i;
        if(currentFrame>positions.size())
            currentFrame = positions.size();
    }

    public void reset()
    {
        positions.clear();
        rotations.clear();
        scales.clear();
        currentFrame = 0;
    }
}
