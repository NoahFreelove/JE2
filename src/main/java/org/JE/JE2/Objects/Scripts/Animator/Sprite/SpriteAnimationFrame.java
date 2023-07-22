package org.JE.JE2.Objects.Scripts.Animator.Sprite;

import org.JE.JE2.Rendering.Renderers.SpriteRenderer;
import org.JE.JE2.Rendering.Texture;

public class SpriteAnimationFrame {
    private final Texture texture;
    private final Texture normalTexture;
    public final long duration;


    public SpriteAnimationFrame(Texture t, Texture normalTexture, long durationInMilliseconds){
        this.texture = t;
        this.duration = durationInMilliseconds;
        this.normalTexture = normalTexture;
    }

    public void Activate(SpriteRenderer sprite){
        if(sprite == null)
            return;
        sprite.setTexture(texture);
        sprite.setNormalTexture(normalTexture);
    }
}
