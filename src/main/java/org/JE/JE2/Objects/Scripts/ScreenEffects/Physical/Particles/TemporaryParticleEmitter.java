package org.JE.JE2.Objects.Scripts.ScreenEffects.Physical.Particles;

import org.JE.JE2.Rendering.Texture;
import org.JE.JE2.Utility.Time;

public class TemporaryParticleEmitter extends ParticleEmitter{
    private float emitterLifespan = 1f;
    public TemporaryParticleEmitter(Texture particleTexture, long particleLifespan, float emitterLifespan, int maxAmount) {
        super(new Particle(particleTexture,particleTexture,particleLifespan,true), maxAmount, 10);
        this.emitterLifespan = emitterLifespan;
    }

    @Override
    public void update() {
        super.update();
        emitterLifespan-= Time.deltaTime();
        if(emitterLifespan<=0){
            setActive(false);
        }
    }
}
