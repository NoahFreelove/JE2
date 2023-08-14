package org.JE.JE2.Utility.Timeline.InterpolateFunctions;

import org.JE.JE2.Objects.Scripts.Transform;
import org.joml.Vector2f;

public interface TransformLerpFunc extends InterpolateFunc<Transform> {
    @Override
    default Transform interpolate(Transform v, Transform v2, float percent) {
        return lerp(v, v2, percent);
    }

    static Transform lerp(Transform start, Transform end, float t) {
        return new Transform(
                Vector2InterpolateFunc.lerp(start.position(), end.position(), t),
                Vector3InterpolateFunc.lerp(start.rotation(), end.rotation(), t),
                Vector2InterpolateFunc.lerp(start.scale(), end.scale(), t)
        );
    }
}
