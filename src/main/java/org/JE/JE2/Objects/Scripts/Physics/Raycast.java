package org.JE.JE2.Objects.Scripts.Physics;

import org.JE.JE2.Objects.GameObject;
import org.joml.Vector2f;

public record Raycast(boolean hit, GameObject gameObjectHit, Vector2f hitPoint) {}
