package org.JE.JE2.Utility.Timeline.InterpolateFunctions;

public interface InterpolateFunc<T> {

    T interpolate(T v, T v2, float percent);
}
