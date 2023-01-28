package JE.Rendering.Renderers;

import JE.Annotations.GLThread;
import JE.Manager;
import JE.Objects.Base.GameObject;
import JE.Objects.Components.Common.Transform;
import JE.Rendering.Camera;
import JE.Rendering.Shaders.ShaderProgram;
import JE.Rendering.VertexBuffers.VAO2f;
import org.joml.Vector2f;


public class ShapeRenderer extends Renderer{
    private final VAO2f pointVAO;

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
        Render(t,additionalBufferSize, Manager.getCamera());
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
