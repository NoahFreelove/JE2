package JE.Objects.Common;

import JE.Input.Keyboard;
import JE.Objects.Base.Sprites.Sprite;
import JE.Objects.Base.Identity;
import JE.Rendering.Camera;
import JE.Rendering.Shaders.BuiltIn.LightSprite.LightSpriteShader;
import JE.Window.Window;
import org.joml.Vector2f;
import org.joml.Vector2i;

public class Player extends Sprite {
    final int a = 65;
    final int d = 68;
    final int w = 87;
    final int s = 83;
    final int UP = 265;
    final int DOWN = 264;
    final int LEFT = 263;
    final int RIGHT = 262;
    float moveSpeed = 5f;
    public Camera camera;
    public Player(Vector2f position){
        super(new Vector2f[]{
                        new Vector2f(0,0),
                        new Vector2f(1,0),
                        new Vector2f(1,1),
                        new Vector2f(0,1)
                }, new LightSpriteShader(),

                "bin/texture1.png",
                new Vector2i(64,64));
        getTransform().position = new Vector2f(position);
        getTransform().zPos = 2;
        addComponent(camera = new Camera(this));
        setIdentity(new Identity("Player","Player"));
        getTransform().scale = new Vector2f(1,1);

        camera.positionOffset = new Vector2f(0.5f * getTransform().scale.x(),0.4f * getTransform().scale.y());
    }

    @Override
    public void update(){
        if(Keyboard.isKeyPressed(a) || Keyboard.isKeyPressed(LEFT)){
            getTransform().position.x -= moveSpeed * Window.deltaTime();
        }
        if(Keyboard.isKeyPressed(d) || Keyboard.isKeyPressed(RIGHT)){
            getTransform().position.x += moveSpeed * Window.deltaTime();
        }
        if(Keyboard.isKeyPressed(w) || Keyboard.isKeyPressed(UP)){
            getTransform().position.y += moveSpeed * Window.deltaTime();
        }
        if(Keyboard.isKeyPressed(s) || Keyboard.isKeyPressed(DOWN)){
            getTransform().position.y -= moveSpeed * Window.deltaTime();
        }
    }

}
