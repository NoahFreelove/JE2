package org.JE.JE2.Objects.Scripts.ScreenEffects.Physical.Particles;

import org.JE.JE2.IO.Logging.Logger;
import org.JE.JE2.Manager;
import org.JE.JE2.Objects.GameObject;
import org.JE.JE2.Objects.Scripts.Transform;
import org.JE.JE2.Rendering.Camera;
import org.JE.JE2.Rendering.Renderers.SpriteRenderer;
import org.JE.JE2.Rendering.Shaders.ShaderProgram;
import org.JE.JE2.Utility.Delayer;
import org.joml.Vector2f;
import org.joml.Vector4f;

import java.util.ArrayList;

public class ParticleEmitter extends SpriteRenderer {

    public ArrayList<Particle> particles;
    private Particle template;
    private int maxAmount;
    public int layer;
    public int maxGenerationsPerFrame = 1;
    private long delayBetweenGenerations = 0L;

    public ParticleEmitter(Particle particleTemplate, int maxAmount, int layer){
        super(ShaderProgram.spriteShader());
        this.template = particleTemplate;
        this.maxAmount = maxAmount;
        this.layer = layer;
        particles = new ArrayList<>();
    }

    Transform relative = new Transform();
    ArrayList<Integer> queuedRemovals = new ArrayList<>();
    @Override
    public void Render(Transform t, int additionalBuffer, int layer, Camera camera){
        int i = 0;

        removeDead();
        generateParticle();

        //System.out.println(particles.size());
        for (Particle particle : particles) {
            if(particle == null)
                return;
            if(!particle.isAlive())
            {
                // we cant modify the list while iterating through it
                queuedRemovals.add(i);
                continue;
            }

            particle.particleUpdate();

            setTexture(particle.getSprite());
            setNormalTexture(particle.getSpriteNormal());

            relative.set(getAttachedObject().getTransform());
            relative.relativeAdd(particle.getRelativeT());

            //System.out.println(particle.getSprite().resource.getID());

            super.Render(relative,0, layer, Manager.getMainCamera());
            i++;
        }
    }

    private void removeDead() {
        queuedRemovals.forEach((integer -> {
            particles.remove(integer.intValue());
        }));
        queuedRemovals.clear();
    }

    protected Delayer generationDelayer = new Delayer(delayBetweenGenerations);
    protected void generateParticle(){
        if(!generationDelayer.check())
            return;
        generationDelayer.reset();
        for (int i = 0; i < maxGenerationsPerFrame; i++) {
            if(particles.size() < maxAmount) {
                spawnParticle(template.clone(getAttachedObject().getTransform()));
            }
            else
                break;
        }
    }
    protected void setDelayBetweenGenerations(long durationMilli){
        delayBetweenGenerations = durationMilli;
        generationDelayer.setDuration(delayBetweenGenerations);
    }

    protected void spawnParticle(Particle newParticle){
        Vector2f randomOffset = new Vector2f((float) Math.random() * 2 - 1, (float) Math.random() * 2 - 1);
        randomOffset.normalize();
        randomOffset.mul((float) Math.random() * 2f);
        newParticle.setRelativeT(new Transform(randomOffset));
        particles.add(newParticle);
    }
}
