package JE.Objects;

import JE.Objects.Base.GameObject;
import JE.Rendering.Renderers.Renderer;
import JE.Rendering.Shaders.ShaderProgram;

import static org.lwjgl.opengl.GL11.GL_LINE;

public class Line extends GameObject {

    protected Renderer lineRenderer;

    public Line(){
        super();
        addComponent(lineRenderer = new Renderer());
        lineRenderer.getVAO().shaderProgram = new ShaderProgram();
        lineRenderer.setDrawMode(GL_LINE);
    }

}
