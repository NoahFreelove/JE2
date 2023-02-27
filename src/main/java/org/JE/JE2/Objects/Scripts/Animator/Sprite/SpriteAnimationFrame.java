package org.JE.JE2.Objects.Scripts.Animator.Sprite;

import org.JE.JE2.Rendering.Renderers.SpriteRenderer;
import org.JE.JE2.Rendering.Texture;

public class SpriteAnimationFrame {
    private final Texture texture;
    public final float duration;


    public SpriteAnimationFrame(Texture t, float durationInMilliseconds){
        this.texture = t;
        this.duration = durationInMilliseconds;
    }

    public void Activate(SpriteRenderer sprite){
        if(sprite == null)
            return;
        sprite.setTexture(texture);
    }
}
