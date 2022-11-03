package JE.Objects;

import JE.Objects.Components.SpriteRenderer;

public class Sprite extends GameObject {
    public Sprite(){
        super();
        addComponent(new SpriteRenderer());
    }
}
