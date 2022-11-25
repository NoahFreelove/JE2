package JE.Utility;

import org.jbox2d.common.Vec2;
import org.jbox2d.common.Vec3;
import org.joml.Vector2f;
import org.joml.Vector3f;

public class JOMLtoJBOX {
    public static Vec3 vec3(Vector3f v){
        return new Vec3(v.x(), v.y(), v.z());
    }
    public static Vec2 vec2(Vector2f v){
        return new Vec2(v.x(), v.y());
    }

    public static Vector3f boxVec3(Vec3 v){
        return new Vector3f(v.x, v.y, v.z);
    }
    public static Vector2f boxVec2(Vec2 v){
        return new Vector2f(v.x, v.y);
    }
}
