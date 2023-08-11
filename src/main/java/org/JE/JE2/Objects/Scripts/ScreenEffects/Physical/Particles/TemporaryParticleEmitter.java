package org.JE.JE2.Objects.Scripts.ScreenEffects.Physical.Particles;

import org.JE.JE2.Manager;
import org.JE.JE2.Rendering.Renderers.VertexBuffers.VAO2f;
import org.JE.JE2.Rendering.Texture;
import org.JE.JE2.Utility.Time;
import org.joml.Vector2f;

public class TemporaryParticleEmitter extends ParticleEmitter{
    private float emitterLifespan = 1f;
    public TemporaryParticleEmitter(Texture particleTexture, long particleLifespan, float emitterLifespan, int maxAmount) {
        super(new Particle(new Vector2f[]{
                new Vector2f(0,0),
                new Vector2f(1,0),
                new Vector2f(1,1),
                new Vector2f(0,1)
        },particleTexture,particleTexture,particleLifespan,true), maxAmount, 10);
        this.emitterLifespan = emitterLifespan;
    }

    @Override
    public void update() {
        super.update();
        emitterLifespan-= Time.deltaTime();
        if(emitterLifespan<=0){
            setActive(false);
            getAttachedObject().removeScript(this);
        }
    }
}
