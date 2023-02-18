package JE.SampleScripts;

import JE.Objects.GameObject;
import JE.Objects.Identity;
import JE.Objects.Scripts.Physics.PhysicsBody;
import JE.Rendering.Shaders.ShaderProgram;
import JE.Rendering.Texture;
import JE.Resources.ResourceLoader;
import org.jbox2d.dynamics.BodyType;
import org.joml.Vector2f;

public class FloorFactory {

    public static GameObject createFloor(Texture texture, ShaderProgram sp, Vector2f position, Vector2f size){
        GameObject floor = GameObject.Sprite(sp,texture);
        floor.setScale(size);
        floor.setPosition(position);
        floor.getSpriteRenderer().defaultTile();
        floor.addScript(new PhysicsBody().setMode(BodyType.STATIC));
        floor.setIdentity(new Identity("Floor", "floor"));
        return floor;
    }
    public static GameObject createFloor(Vector2f pos, Vector2f size){
        return createFloor(new Texture(ResourceLoader.get("texture2.png")), ShaderProgram.spriteShader(), pos, size);
    }
}
