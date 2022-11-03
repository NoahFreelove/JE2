package JE.Rendering;

import JE.Manager;
import org.joml.Vector2f;
import org.lwjgl.BufferUtils;

import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL15.glBindBuffer;
import static org.lwjgl.opengl.GL15.glGenBuffers;

public class VAO {
    public ShaderProgram shaderProgram = new ShaderProgram();
    public Vector2f[] vertices = new Vector2f[0];

    public VAO(){
        GenerateBuffers();
    }

    public VAO(Vector2f[] vertices){
        this.vertices = vertices;
        GenerateBuffers();
    }

    private void GenerateBuffers(){
        Runnable r = () -> {
            IntBuffer vertexBuffer = BufferUtils.createIntBuffer(100);
            glGenBuffers(vertexBuffer);

        };

        Manager.QueueGLFunction(r);
    }

    public void addVertex(Vector2f vertex){
        Vector2f[] newVertices = new Vector2f[vertices.length + 1];
        System.arraycopy(vertices, 0, newVertices, 0, vertices.length);
        newVertices[newVertices.length - 1] = vertex;
        vertices = newVertices;
    }

    public void addVertices(Vector2f[] vertices){
        Vector2f[] newVertices = new Vector2f[this.vertices.length + vertices.length];
        System.arraycopy(this.vertices, 0, newVertices, 0, this.vertices.length);
        System.arraycopy(vertices, 0, newVertices, this.vertices.length, vertices.length);
        this.vertices = newVertices;
    }
}
