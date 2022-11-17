package JE.Rendering.VertexBuffers;

import JE.Rendering.Shaders.ShaderProgram;
import org.joml.Vector2f;

public class VAO2f extends VAO {
    protected Vector2f[] vertices = new Vector2f[0];

    public VAO2f(){
        dataSize = 2;
        QueueGenerateBuffers();
    }

    public VAO2f(Vector2f[] vertices){
        this.vertices = vertices;
        this.data = dataConversion();
        dataSize = 2;
        QueueGenerateBuffers();
    }

    public VAO2f(Vector2f[] vertices, ShaderProgram sp){
        this.vertices = vertices;
        this.shaderProgram = sp;
        this.data = dataConversion();
        dataSize = 2;
        QueueGenerateBuffers();
    }
    public VAO2f(VAO2f vao){
        this.vertices = new Vector2f[vao.getVertices().length];
        this.data = dataConversion();
        dataSize = 2;
        this.shaderProgram = vao.shaderProgram;
        QueueGenerateBuffers();
    }

    public void addVertex(Vector2f vertex){
        Vector2f[] newVertices = new Vector2f[vertices.length + 1];
        System.arraycopy(vertices, 0, newVertices, 0, vertices.length);
        newVertices[newVertices.length - 1] = vertex;
        vertices = newVertices;
        QueueGenerateBuffers();
    }

    public void addVertices(Vector2f[] vertices){
        Vector2f[] newVertices = new Vector2f[this.vertices.length + vertices.length];
        System.arraycopy(this.vertices, 0, newVertices, 0, this.vertices.length);
        System.arraycopy(vertices, 0, newVertices, this.vertices.length, vertices.length);
        this.vertices = newVertices;
        QueueGenerateBuffers();
    }

    public void setVertices(Vector2f[] vertices){
        this.vertices = vertices;
        QueueGenerateBuffers();
    }


    public Vector2f[] getVertices(){
        return vertices;
    }

    @Override
    protected float[] dataConversion(){
        float[] data = new float[vertices.length * 2];
        for(int i = 0; i < vertices.length; i++){
            data[i*2] = vertices[i].x;
            data[i*2+1] = vertices[i].y;
        }
        return data;
    }
}
