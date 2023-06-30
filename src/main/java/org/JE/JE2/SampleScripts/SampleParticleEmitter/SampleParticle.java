package org.JE.JE2.SampleScripts.SampleParticleEmitter;

import org.JE.JE2.IO.Filepath;
import org.JE.JE2.Manager;
import org.JE.JE2.Objects.Scripts.ScreenEffects.Physical.Particles.Particle;
import org.JE.JE2.Objects.Scripts.Transform;
import org.JE.JE2.Rendering.Texture;
import org.JE.JE2.Utility.Time;

public class SampleParticle extends Particle {

    private float yVelocity = 0f;
    private float xVelocity = 0f;
    private int rotateDirection = 1;
    private static Filepath fireFilepath = new Filepath("fire.png", true);
    public SampleParticle() {
        super(Texture.checkExistElseCreate("fire",-1,fireFilepath, true), Texture.checkExistElseCreate("fire",-1,fireFilepath,true), 2000, true);
        yVelocity = (float) (Math.random() * 3f);
        xVelocity = (float) (Math.random() * 2f);
        if(Math.random()>0.5f)
        {
            xVelocity*=-1;
            rotateDirection = -1;
        }
    }

    @Override
    public Particle clone(Transform t) {
        SampleParticle particle = new SampleParticle();
        particle.setRelativeT(t);
        return particle;
    }

    @Override
    public void particleUpdate() {
        yVelocity-=0.1f;
        if(yVelocity< -4f)
            yVelocity = -4f;

        float delta = yVelocity * Time.deltaTime();
        relativeT.translateY(delta);
        relativeT.translateX(xVelocity * Time.deltaTime());
        relativeT.rotateZ(30f * Time.deltaTime()*rotateDirection);
        relativeT.setScale(0.6f,0.6f);
    }
}
