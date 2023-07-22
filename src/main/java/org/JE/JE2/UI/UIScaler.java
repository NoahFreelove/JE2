package org.JE.JE2.UI;

import org.JE.JE2.UI.UIObjects.UIWindow;
import org.joml.Vector2f;

public class UIScaler {
    public static int BASE_WINDOW_WIDTH;
    public static int BASE_WINDOW_HEIGHT;
    public static float MULTIPLIERX = 1;
    public static float MULTIPLIERY = 1;


    public static void setNewRes(int newWidth, int newHeight){
        if(BASE_WINDOW_WIDTH == 0 && BASE_WINDOW_HEIGHT == 0){
            BASE_WINDOW_WIDTH = newWidth;
            BASE_WINDOW_HEIGHT = newHeight;
        }
        MULTIPLIERX = (float)newWidth / (float)BASE_WINDOW_WIDTH;
        MULTIPLIERY = (float)newHeight / (float)BASE_WINDOW_HEIGHT;

    }

    public static Vector2f scaleUI(Vector2f referenceScale)
    {
        return new Vector2f(referenceScale.x * MULTIPLIERX, referenceScale.y * MULTIPLIERY);
    }
}
