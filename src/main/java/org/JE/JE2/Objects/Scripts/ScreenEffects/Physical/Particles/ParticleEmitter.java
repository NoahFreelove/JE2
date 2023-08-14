package org.JE.JE2.Objects.Scripts.ScreenEffects.Physical.Particles;

import org.JE.JE2.Objects.Scripts.Transform;
import org.JE.JE2.Rendering.Camera;
import org.JE.JE2.Rendering.Renderers.SpriteRenderer;
import org.JE.JE2.Rendering.Shaders.ShaderProgram;
import org.JE.JE2.Utility.FlowControl.Delayer;
import org.joml.Vector2f;

import java.util.ArrayList;

public class ParticleEmitter extends SpriteRenderer {

    public ArrayList<Particle> particles;
    private Particle template;
    private int maxAmount;
    public int layer;
    public int maxGenerationsPerFrame = 1;
    private long delayBetweenGenerations = 0L;
    public float randXPos = 2f;
    public float randYPos = 2f;
    public float randMagnitude = 2f;
    public ParticleEmitter(Particle particleTemplate, int maxAmount, int layer){
        super(ShaderProgram.spriteShader());
        this.template = particleTemplate;
        this.maxAmount = maxAmount;
        this.layer = layer;
        particles = new ArrayList<>();
    }

    ArrayList<Integer> queuedRemovals = new ArrayList<>();

    @Override
    public void requestRender(Transform t, Camera camera) {

        removeDead();

        generateParticle();

        int i = 0;
        for (Particle p : particles) {
            if(p == null)
                return;
            if(!p.isAlive())
            {
                // we cant modify the list while iterating through it
                queuedRemovals.add(i);
                i++;
                continue;
            }

            p.particleUpdate();

            RenderTextureSegment(p.getTextSeg(), t, camera);
            i++;
        }
    }

    private void removeDead() {
        queuedRemovals.forEach((integer -> {
            if(integer< particles.size())
            {
                particles.get(integer).getTextSeg().destroy();
                particles.remove(integer.intValue());
            }
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
        Vector2f randomOffset = new Vector2f((float) Math.random() * randXPos, (float) Math.random() * randYPos);
        randomOffset.normalize();
        randomOffset.mul((float) Math.random() * randMagnitude);
        newParticle.setRelativeT(new Transform(randomOffset));
        particles.add(newParticle);
    }

    @Override
    public void destroy() {
        super.destroy();
        template.getTextSeg().destroy();
    }
}
