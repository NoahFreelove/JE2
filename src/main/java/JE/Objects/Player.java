package JE.Objects;

import JE.Input.Keyboard;
import JE.Manager;
import JE.Objects.Base.Identity;
import JE.Objects.Base.Sprite;
import JE.Rendering.Camera;
import JE.Window.Window;
import org.joml.Vector2f;
import org.joml.Vector2i;

public class Player extends Sprite {
    int a = Keyboard.nameToCode("A");
    int d = Keyboard.nameToCode("D");
    int w = Keyboard.nameToCode("W");
    int s = Keyboard.nameToCode("S");
    float moveSpeed = 5f;
    public Camera camera;
    public Player(Vector2f position){
        super(new Vector2f[]{
                        new Vector2f(0,0),
                        new Vector2f(1,0),
                        new Vector2f(1,1),
                        new Vector2f(0,1)
                },

                "bin/texture1.png",
                new Vector2i(64,64));
        getTransform().position = new Vector2f(position);
        getTransform().zPos = 2;
        addComponent(camera = new Camera(this));
        setIdentity(new Identity("Player","Player"));
        camera.positionOffset = new Vector2f(0.5f,0.3f);


    }

    @Override
    public void Update(){
        if(Keyboard.keys[a]){
            getTransform().position.x -= moveSpeed * Window.deltaTime;
        }
        if(Keyboard.keys[d]){
            getTransform().position.x += moveSpeed * Window.deltaTime;
        }
        if(Keyboard.keys[w]){
            getTransform().position.y += moveSpeed * Window.deltaTime;
        }
        if(Keyboard.keys[s]){
            getTransform().position.y -= moveSpeed * Window.deltaTime;
        }
    }

}
