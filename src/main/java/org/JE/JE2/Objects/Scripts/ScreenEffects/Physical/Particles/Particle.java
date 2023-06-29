package org.JE.JE2.Objects.Scripts.ScreenEffects.Physical.Particles;

import org.JE.JE2.Objects.Scripts.Transform;
import org.JE.JE2.Rendering.Texture;
import org.joml.Vector2f;

public class Particle {
    protected Transform relativeT;
    private Texture sprite;
    private Texture spriteNormal;
    private boolean enabled = true;
    private long lifespan = 0L;
    private long timestamp = 0L;
    private boolean hasLifespan = false;

    public Particle(Transform relativeT, Texture sprite, Texture spriteNormal) {
        this.relativeT = relativeT;
        this.sprite = sprite;
        this.spriteNormal = spriteNormal;
    }

    public Particle(Texture sprite, Texture spriteNormal, long lifespan, boolean hasLifespan) {
        this.relativeT = new Transform();
        this.sprite = sprite;
        this.spriteNormal = spriteNormal;
        this.lifespan = lifespan;
        this.timestamp = System.currentTimeMillis() + lifespan;
        this.hasLifespan = hasLifespan;
    }

    public Particle(Vector2f posOffset, Texture sprite, Texture spriteNormal) {
        this.relativeT = new Transform();
        relativeT.setPosition(posOffset);
        this.sprite = sprite;
        this.spriteNormal = spriteNormal;
    }

    public Particle clone(Transform t){
        Particle particle = new Particle(getSprite(), getSpriteNormal(), lifespan, hasLifespan);
        particle.setRelativeT(t);
        return particle;
    }

    public Transform getRelativeT() {
        return relativeT;
    }

    public void setRelativeT(Transform relativeT) {
        this.relativeT = relativeT;
    }

    public Texture getSprite() {
        return sprite;
    }

    public void setSprite(Texture sprite) {
        this.sprite = sprite;
    }

    public Texture getSpriteNormal() {
        return spriteNormal;
    }

    public void setSpriteNormal(Texture spriteNormal) {
        this.spriteNormal = spriteNormal;
    }

    /**
     * Set the lifespan of the particle or disable it
     * @param time time in milliseconds
     * @param hasLifespan whether the particle has a lifespan
     */
    public void setLifespan(long time, boolean hasLifespan)
    {
        this.timestamp = System.currentTimeMillis() + time;
        this.lifespan = time;
        this.hasLifespan = hasLifespan;
    }

    public boolean isAlive(){
        if(hasLifespan)
            return timestamp > System.currentTimeMillis();
        else
            return enabled;
    }
    public void particleUpdate(){}
}
