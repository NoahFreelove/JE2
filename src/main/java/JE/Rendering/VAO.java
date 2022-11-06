package JE.Rendering;

import JE.Manager;
import org.joml.Vector2f;
import org.lwjgl.BufferUtils;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL15.*;

public class VAO {
    public ShaderProgram shaderProgram = new ShaderProgram();
    public Vector2f[] vertices = new Vector2f[0];
    public int vertexBufferID = 0;
    public VAO(){
        GenerateBuffers();
    }

    public VAO(Vector2f[] vertices){
        this.vertices = vertices;
        GenerateBuffers();
    }

    public VAO(Vector2f[] vertices, ShaderProgram sp){
        this.vertices = vertices;
        this.shaderProgram = sp;
        GenerateBuffers();
    }

    private void GenerateBuffers(){
        Runnable r = () -> {
            vertexBufferID = glGenBuffers();
            glBindBuffer(GL_ARRAY_BUFFER, vertexBufferID);
            FloatBuffer fb = BufferUtils.createFloatBuffer(vertices.length * 2 );
            for (Vector2f v : vertices) {
                fb.put(v.x);
                fb.put(v.y);
            }
            fb.flip();
            glBufferData(GL_ARRAY_BUFFER, fb, GL_STATIC_DRAW);
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

    public void setVertices(Vector2f[] vertices){
        this.vertices = vertices;
        GenerateBuffers();
    }
    public void setShaderProgram(ShaderProgram sp){
        this.shaderProgram = sp;
    }
}
