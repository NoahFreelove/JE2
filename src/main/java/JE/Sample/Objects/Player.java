package JE.Sample.Objects;

import JE.IO.UserInput.Keyboard.Combos.ComboList;
import JE.IO.UserInput.Keyboard.Keyboard;
import JE.Manager;
import JE.Objects.Components.Physics.PhysicsBody;
import JE.Objects.Identity;
import JE.Objects.Base.Sprite;
import JE.Rendering.Camera;
import JE.Rendering.Shaders.ShaderProgram;
import JE.Rendering.Texture;
import JE.Sample.Components.MovementController;
import JE.UI.UIObjects.UIWindow;


import org.jbox2d.dynamics.BodyType;
import org.joml.Vector2f;

public class Player extends Sprite {
    public Camera camera;
    private MovementController mc = new MovementController();

    public Player(){
        super(ShaderProgram.lightSpriteShader());
        Texture t = new Texture("bin/texture1.png");
        setTexture(t);
        getTransform().setZPos(2);
        addComponent(camera = new Camera());
        camera.parentObject = this;
        setIdentity(new Identity("Player","Player"));

        camera.positionOffset = new Vector2f(0.5f * getTransform().scale().x(),0.4f * getTransform().scale().y());
        Keyboard.keyReleasedEvents.add((key, mods) -> {
            if(Keyboard.nameToCode("F") == key){
                ((UIWindow)Manager.activeScene().world.UI.get(0)).toggleVisibility();
            }
        });

        mc.physicsBased = true;
        mc.absoluteXPositioning = true;

        addComponent(new PhysicsBody().create(BodyType.DYNAMIC, getTransform().position(), new Vector2f(1,1)));
        addComponent(mc);
    }
}
