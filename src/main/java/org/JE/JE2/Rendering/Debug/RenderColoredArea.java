package org.JE.JE2.Rendering.Debug;

import org.JE.JE2.Objects.GameObject;
import org.JE.JE2.Rendering.Renderers.Renderer;
import org.JE.JE2.Rendering.Shaders.ShaderProgram;
import org.JE.JE2.Rendering.Renderers.VertexBuffers.VAO2f;
import org.JE.JE2.UI.UIElements.Style.Color;
import org.JE.JE2.Utility.JE2Math;
import org.joml.Vector2f;

public class RenderColoredArea {

    public static GameObject getArea(Vector2f pos, Vector2f bounds){
        GameObject obj = new GameObject();
        obj.setPosition(pos);
        obj.setScale(bounds);
        Renderer renderer = new Renderer();
        renderer.material.setBaseColor(Color.GREEN.clone().a(0.5f));
        obj.addScript(renderer);
        return obj;
    }

    public static GameObject getArea(float x1, float y1, float x2, float y2){
        GameObject obj = new GameObject();
        obj.setPosition(x1,y1);
        obj.setScale(x2,y2);
        Renderer renderer = new Renderer();
        renderer.material.setBaseColor(Color.GREEN.clone().a(0.5f));
        obj.addScript(renderer);
        return obj;
    }

    public static GameObject getArea(Vector2f pos, Vector2f bounds, Color c){
        GameObject obj = new GameObject();
        obj.setPosition(pos);
        obj.setScale(bounds);
        Renderer renderer = new Renderer();
        renderer.material.setBaseColor(c);
        obj.addScript(renderer);
        return obj;
    }

    public static GameObject getArea(float x1, float y1, float x2, float y2, Color c){
        GameObject obj = new GameObject();
        obj.setPosition(x1,y1);
        obj.setScale(x2,y2);
        Renderer renderer = new Renderer();
        renderer.material.setBaseColor(c);
        obj.addScript(renderer);
        return obj;
    }

    public static GameObject getRadius(Vector2f pos, float radius, int sides, Color c){
        GameObject obj = new GameObject();
        obj.setPosition(pos);
        obj.setScale(radius,radius);

        sides = JE2Math.clamp(sides,30,200);
        VAO2f circlePoints = new VAO2f(generateCircleCoords(sides));

        Renderer renderer = new Renderer(circlePoints, ShaderProgram.defaultShader());

        renderer.material.setBaseColor(c);
        obj.addScript(renderer);
        return obj;
    }

    public static GameObject getRadius(float x, float y, float radius, int sides, Color c){
        GameObject obj = new GameObject();
        obj.setPosition(x,y);
        obj.setScale(radius,radius);
        sides = JE2Math.clamp(sides,30,200);
        VAO2f circlePoints = new VAO2f(generateCircleCoords(sides));
        Renderer renderer = new Renderer(circlePoints, ShaderProgram.defaultShader());

        renderer.material.setBaseColor(c);
        obj.addScript(renderer);
        return obj;
    }

    private static Vector2f[] generateCircleCoords(int sides){
        Vector2f[] coords = new Vector2f[sides];
        for(int i = 0; i < sides; i++){
            float angle = (float) (i * 2 * Math.PI / sides);
            coords[i] = new Vector2f((float) Math.cos(angle), (float) Math.sin(angle));
        }
        coords[sides-1] = coords[0];
        return coords;
    }
}
