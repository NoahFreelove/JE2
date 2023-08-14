package org.JE.JE2.Objects.Scripts.ScreenEffects.Physical.Particles;

import org.JE.JE2.Manager;
import org.JE.JE2.Objects.GameObject;
import org.JE.JE2.Objects.Scripts.Script;
import org.JE.JE2.Rendering.Renderers.VertexBuffers.VAO2f;
import org.JE.JE2.Rendering.Texture;
import org.JE.JE2.Utility.Time;
import org.joml.Vector2f;

public class TemporaryParticleEmitter extends Script {
    private float emitterLifespan = 1f;
    private ParticleEmitter pe;
    public TemporaryParticleEmitter(ParticleEmitter pe, float emitterLifespan){
        this.pe = pe;
        this.emitterLifespan = emitterLifespan;
    }

    @Override
    public void onAddedToGameObject(GameObject gameObject) {
        gameObject.addScript(pe);
    }

    @Override
    public void update() {
        super.update();
        emitterLifespan-= Time.deltaTime();
        if(emitterLifespan<=0){
            setActive(false);
            getAttachedObject().removeScript(pe);
        }
    }
}
