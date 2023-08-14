package org.JE.JE2.Utility.Timeline.InterpolateFunctions;

import org.joml.Vector2f;

public interface Vector2InterpolateFunc extends InterpolateFunc<Vector2f>{
    @Override
    default Vector2f interpolate(Vector2f v, Vector2f v2, float percent) {
        return lerp(v, v2, percent);
    }

    static Vector2f lerp(Vector2f start, Vector2f end, float t) {
        return new Vector2f(
                start.x + (end.x - start.x) * t,
                start.y + (end.y - start.y) * t
        );
    }
}
