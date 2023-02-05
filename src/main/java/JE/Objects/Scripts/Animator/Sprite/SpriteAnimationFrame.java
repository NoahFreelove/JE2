package JE.Objects.Scripts.Animator.Sprite;

import JE.Rendering.Renderers.SpriteRenderer;
import JE.Rendering.Texture;

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
