package org.JE.JE2.Rendering.Renderers;

import org.JE.JE2.Annotations.GLThread;
import org.JE.JE2.Manager;
import org.JE.JE2.Objects.GameObject;
import org.JE.JE2.Rendering.Camera;
import org.JE.JE2.Rendering.Shaders.ShaderProgram;
import org.JE.JE2.Rendering.VertexBuffers.VAO2f;
import org.joml.Vector2f;


public class ShapeRenderer extends Renderer{
    private transient final VAO2f pointVAO;

    public ShapeRenderer(){
        pointVAO = new VAO2f(new Vector2f[]{}, ShaderProgram.defaultShader());
        vao = pointVAO;
    }

    @Override
    @GLThread
    public void Render(GameObject t) {
        Render(t, 0);
    }

    @Override
    @GLThread
    public void Render(GameObject t, int additionalBufferSize) {
        Render(t,additionalBufferSize, Manager.getMainCamera());
    }

    @Override
    @GLThread
    public void Render(GameObject t, int additionalBufferSize, Camera camera){
        pointVAO.Enable(1);
        super.Render(t, pointVAO.getVertices().length*2+additionalBufferSize, camera);
        pointVAO.Disable();
    }

    public void setPoints(Vector2f[] points){
        pointVAO.setVertices(points);
    }
}
