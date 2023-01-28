package JE.Objects.Components.Animator.Sprite;

import JE.Objects.Base.Sprite;
import JE.Rendering.Renderers.SpriteRenderer;
import JE.Rendering.Texture;

public class SpriteAnimationFrame {
    private final Texture texture;
    public final float duration;


    public SpriteAnimationFrame(Texture t, float durationInMilliseconds){
        this.texture = t;
        this.duration = durationInMilliseconds;
    }

    public void Activate(Sprite sprite){
        SpriteRenderer sr = (SpriteRenderer)sprite.renderer;
        if(sr == null)
            return;
        sr.setTexture(texture);
    }
}
