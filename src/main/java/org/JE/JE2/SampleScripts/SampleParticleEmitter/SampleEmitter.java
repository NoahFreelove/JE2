package org.JE.JE2.SampleScripts.SampleParticleEmitter;

import org.JE.JE2.Objects.Scripts.ScreenEffects.Physical.Particles.Particle;
import org.JE.JE2.Objects.Scripts.ScreenEffects.Physical.Particles.ParticleEmitter;

public class SampleEmitter extends ParticleEmitter {
    public SampleEmitter(int maxAmount) {
        super(new SampleParticle(), maxAmount, 0);
        maxGenerationsPerFrame = 3;
        setDelayBetweenGenerations(200);
    }
}
