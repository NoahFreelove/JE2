package org.JE.JE2.Utility;

import org.joml.Vector2f;

public class JE2Math {

    public static float clamp(float f, float min, float max){
        if(f<min)
            return min;
        return java.lang.Math.min(f, max);
    }

    public static float clamp(float f){
        if(f<0)
            return 0;
        return java.lang.Math.min(f, 1);
    }

    public static float clamp0(float f){
        if(f<0)
            return 0;
        return f;
    }

    public static int clamp(int f, int min, int max){
        if(f<min)
            return min;
        return java.lang.Math.min(f, max);
    }

    public static Vector2f interpolate(Vector2f a, Vector2f b, float t){
        return new Vector2f(a.x + (b.x-a.x)*t, a.y + (b.y-a.y)*t);
    }

    public static float floatExp(float val, int exp){
        return (float) (val* Math.pow(10,exp));
    }

    public static float lerp(int a, int b, float level) {
        return a + (b-a)*level;
    }
}
