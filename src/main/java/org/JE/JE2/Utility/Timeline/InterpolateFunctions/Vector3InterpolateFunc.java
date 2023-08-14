package org.JE.JE2.Utility.Timeline.InterpolateFunctions;

import org.joml.Vector2f;
import org.joml.Vector3f;

public interface Vector3InterpolateFunc extends InterpolateFunc<Vector3f>{
    @Override
    default Vector3f interpolate(Vector3f v, Vector3f v2, float percent)
    {
        return lerp(v, v2, percent);
    }

    static Vector3f lerp(Vector3f v, Vector3f v2, float percent){
        return new Vector3f(
                v.x + (v2.x - v.x) * percent,
                v.y + (v2.y - v.y) * percent,
                v.z + (v2.z - v.z) * percent
        );
    }

}
