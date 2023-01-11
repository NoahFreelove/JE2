package JE.Objects.Common;

import JE.IO.ImageProcessor;
import JE.Input.Keyboard;
import JE.Objects.Base.Identity;
import JE.Objects.Base.Sprites.Sprite;
import JE.Rendering.Camera;
import JE.Rendering.Shaders.BuiltIn.LightSprite.LightSpriteShader;
import JE.Rendering.Texture;
import JE.Resources.Resource;
import JE.Resources.ResourceType;
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
    public Player(){
        super();
        Texture t = new Texture("bin/light.png", new Vector2i(128,128));
        setTexture(t);
        setNormalTexture(new Texture("bin/texture1_N.png", new Vector2i(64,64)));
        getTransform().zPos = 2;
        setShader(new LightSpriteShader());
        addComponent(camera = new Camera());
        //camera.viewportSize = new Vector4i(0,0,640,720);
        camera.parentObject = this;
        setIdentity(new Identity("Player","Player"));

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