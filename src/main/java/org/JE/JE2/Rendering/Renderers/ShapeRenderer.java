package org.JE.JE2.Rendering.Renderers;

import org.JE.JE2.Annotations.GLThread;
import org.JE.JE2.Annotations.HideFromInspector;
import org.JE.JE2.Objects.Scripts.Transform;
import org.JE.JE2.Rendering.Camera;
import org.JE.JE2.Rendering.Shaders.ShaderProgram;
import org.JE.JE2.Rendering.Renderers.VertexBuffers.VAO2f;
import org.joml.Vector2f;


public class ShapeRenderer extends Renderer{


    public ShapeRenderer(){
        super();
    }
    public void setPoints(Vector2f[] points){
        renderSegments[0].getVao().setData(VAO2f.vec2fToFloat(points));
    }

}
