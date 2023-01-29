package JE.Objects.Common;

import JE.IO.UserInput.Keyboard;
import JE.Manager;
import JE.Objects.Components.Physics.PhysicsBody;
import JE.Objects.Identity;
import JE.Objects.Base.Sprite;
import JE.Rendering.Camera;
import JE.Rendering.Shaders.ShaderProgram;
import JE.Rendering.Texture;
import JE.UI.UIObjects.UIWindow;
import JE.Window.Window;

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.BodyType;
import org.joml.Vector2f;
import org.joml.Vector4i;

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
    public PhysicsBody pb;
    public Player(){
        super(ShaderProgram.lightSpriteShader());
        Texture t = new Texture("bin/texture1.png");
        setTexture(t);
        getTransform().zPos = 2;
        addComponent(camera = new Camera());
        camera.parentObject = this;
        setIdentity(new Identity("Player","Player"));

        camera.positionOffset = new Vector2f(0.5f * getTransform().scale.x(),0.4f * getTransform().scale.y());
        Keyboard.keyReleasedEvents.add((key, mods) -> {
            if(Keyboard.nameToCode("F") == key){
                ((UIWindow)Manager.activeScene().world.UI.get(0)).toggleVisibility();
            }
        });
        addComponent(pb = new PhysicsBody().create(BodyType.DYNAMIC, getTransform().position, new Vector2f(1,1)));
    }

    @Override
    public void update(){
        if(Keyboard.isKeyPressed(a) || Keyboard.isKeyPressed(LEFT)){
            getTransform().position.x -= moveSpeed * Window.deltaTime();
            //pb.body.setLinearVelocity(new Vec2(-moveSpeed,pb.body.getLinearVelocity().y));
        }
        if(Keyboard.isKeyPressed(d) || Keyboard.isKeyPressed(RIGHT)){
            getTransform().position.x += moveSpeed * Window.deltaTime();
            //pb.body.setLinearVelocity(new Vec2(moveSpeed,pb.body.getLinearVelocity().y));
        }
        if(Keyboard.isKeyPressed(w) || Keyboard.isKeyPressed(UP)){
            if(pb.onGround)
                pb.body.setLinearVelocity(new Vec2(pb.body.getLinearVelocity().x,moveSpeed*1));
        }
    }

}
