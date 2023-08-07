package org.JE.JE2.Objects.Scripts.ScreenEffects.Physical.Particles;

import org.JE.JE2.Objects.Scripts.Transform;
import org.JE.JE2.Rendering.Renderers.TextureSegment;
import org.JE.JE2.Rendering.Renderers.VertexBuffers.VAO2f;
import org.JE.JE2.Rendering.Texture;
import org.joml.Vector2f;

import static org.lwjgl.opengl.GL11.GL_TRIANGLE_FAN;

public class Particle {
    private TextureSegment textSeg;
    private boolean enabled = true;
    private long lifespan = 0L;
    private long timestamp = 0L;
    private boolean hasLifespan = false;

    public Particle(Transform relativeT, Texture sprite, Texture spriteNormal) {
        textSeg = new TextureSegment(relativeT, sprite, spriteNormal);
    }

    public Particle(VAO2f vao, Texture sprite, Texture spriteNormal, long lifespan, boolean hasLifespan) {
        this.textSeg = new TextureSegment(vao, new Transform(), GL_TRIANGLE_FAN, sprite, spriteNormal);
        this.lifespan = lifespan;
        this.timestamp = System.currentTimeMillis() + lifespan;
        this.hasLifespan = hasLifespan;
    }

    public Particle(Vector2f posOffset, Texture sprite, Texture spriteNormal) {
        Transform t = new Transform();
        t.setPosition(posOffset);
        this.textSeg = new TextureSegment(t,sprite,spriteNormal);
    }

    public Particle clone(Transform t){
        Particle particle = new Particle(getTextSeg().getVao2fRef(), getSprite(), getSpriteNormal(), lifespan, hasLifespan);
        particle.setRelativeT(t);
        particle.getTextSeg().setAdditionalBufferSize(particle.getTextSeg().getVao().getData().length);
        return particle;
    }

    public Transform getRelativeT() {
        return textSeg.getRelativeTransform();
    }

    public void setRelativeT(Transform relativeT) {
        textSeg.setRelativeTransform(relativeT);
    }

    public Texture getSprite() {
        return textSeg.getTexture();
    }

    public void setSprite(Texture sprite) {
        textSeg.setTexture(sprite);
    }

    public Texture getSpriteNormal() {
        return textSeg.getNormal();
    }

    public void setSpriteNormal(Texture spriteNormal) {
        textSeg.setNormal(spriteNormal);
    }

    public TextureSegment getTextSeg() {
        return textSeg;
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
    public void particleUpdate(){
    }
}
