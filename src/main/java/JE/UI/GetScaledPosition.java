package JE.UI;

import org.joml.Vector2f;

public class GetScaledPosition {
    public static Vector2f getScaledPosition(float percentX, float percentY, Vector2f referenceViewportDimensions, Vector2f actualViewportDimensions){
        return new Vector2f(
                (percentX / 100f) * referenceViewportDimensions.x * (actualViewportDimensions.x / referenceViewportDimensions.x),
                (percentY / 100f) * referenceViewportDimensions.y * (actualViewportDimensions.y / referenceViewportDimensions.y)
        );
    }

}
