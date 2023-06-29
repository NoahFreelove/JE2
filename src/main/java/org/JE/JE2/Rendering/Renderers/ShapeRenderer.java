package org.JE.JE2.Rendering.Renderers;

import org.JE.JE2.Annotations.GLThread;
import org.JE.JE2.Annotations.HideFromInspector;
import org.JE.JE2.Manager;
import org.JE.JE2.Objects.GameObject;
import org.JE.JE2.Objects.Scripts.Transform;
import org.JE.JE2.Rendering.Camera;
import org.JE.JE2.Rendering.Shaders.ShaderProgram;
import org.JE.JE2.Rendering.VertexBuffers.VAO2f;
import org.joml.Vector2f;


public class ShapeRenderer extends Renderer{
    @HideFromInspector
    private transient final VAO2f pointVAO;

    public ShapeRenderer(){
        pointVAO = new VAO2f(new Vector2f[]{});
        setShaderProgram(ShaderProgram.defaultShader());
        vao = pointVAO;
    }


    @Override
    @GLThread
    public void Render(Transform t, int additionalBufferSize, int layer, Camera camera){
        pointVAO.Enable(1);
        super.Render(t, pointVAO.getVertices().length*2+additionalBufferSize, layer, camera);
        pointVAO.Disable();
    }

    public void setPoints(Vector2f[] points){
        pointVAO.setVertices(points);
    }

    @Override
    public void load() {
        if(pointVAO !=null)
        {
            getShaderProgram().presetIndex = defaultShaderIndex;
            getShaderProgram().presetIndex = defaultShaderIndex;
            vao = pointVAO;
            pointVAO.load();
            vao.load();
        }
        super.load();
    }
}
