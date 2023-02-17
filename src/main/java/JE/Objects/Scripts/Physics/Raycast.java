package JE.Objects.Scripts.Physics;

import JE.Objects.GameObject;
import org.joml.Vector2f;

public record Raycast(boolean hit, GameObject gameObjectHit, Vector2f hitPoint) {}
