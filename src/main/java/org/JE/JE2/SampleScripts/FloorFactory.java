package org.JE.JE2.SampleScripts;

import org.JE.JE2.Objects.GameObject;
import org.JE.JE2.Objects.Identity;
import org.JE.JE2.Objects.Scripts.Physics.PhysicsBody;
import org.JE.JE2.Rendering.Shaders.ShaderProgram;
import org.JE.JE2.Rendering.Shaders.ShaderRegistry;
import org.JE.JE2.Rendering.Texture;
import org.JE.JE2.Resources.DataLoader;
import org.jbox2d.dynamics.BodyType;
import org.joml.Vector2f;

public class FloorFactory {

    public static GameObject createFloor(Texture texture, ShaderProgram sp, Vector2f position, Vector2f size){
        GameObject floor = GameObject.Sprite(sp,texture);
        floor.setScale(size);
        floor.setPosition(position);
        floor.getSpriteRenderer().getTextureSegment().customTile(size);
        PhysicsBody pb = new PhysicsBody().setMode(BodyType.STATIC);
        pb.defaultFriction = 0;
        floor.addScript(pb);
        floor.setIdentity("Floor", "floor");
        return floor;
    }
    public static GameObject createFloor(Vector2f pos, Vector2f size){
        return createFloor(Texture.get("floor"), new ShaderProgram(ShaderRegistry.lightSpriteShaderModule), pos, size);
    }
}
