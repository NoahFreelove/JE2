package org.JE.JE2.Rendering.VertexBuffers;

import org.JE.JE2.Rendering.Shaders.ShaderProgram;
import org.joml.Vector3f;

public class VAO3f extends VAO{
    protected Vector3f[] vertices = new Vector3f[0];

    public VAO3f(){
        dataSize = 3;
        QueueGenerateBuffers();
    }

    public VAO3f(Vector3f[] vertices){
        this.vertices = vertices;
        this.data = dataConversion();
        dataSize = 3;
        QueueGenerateBuffers();
    }

    public VAO3f(VAO2f vao){
        this.vertices = new Vector3f[vao.getVertices().length];
        this.data = dataConversion();
        dataSize = 3;
        QueueGenerateBuffers();
    }

    public void addVertex(Vector3f vertex){
        Vector3f[] newVertices = new Vector3f[vertices.length + 1];
        System.arraycopy(vertices, 0, newVertices, 0, vertices.length);
        newVertices[newVertices.length - 1] = vertex;
        vertices = newVertices;
        QueueGenerateBuffers();
    }

    public void addVertices(Vector3f[] vertices){
        Vector3f[] newVertices = new Vector3f[this.vertices.length + vertices.length];
        System.arraycopy(this.vertices, 0, newVertices, 0, this.vertices.length);
        System.arraycopy(vertices, 0, newVertices, this.vertices.length, vertices.length);
        this.vertices = newVertices;
        QueueGenerateBuffers();
    }

    public void setVertices(Vector3f[] vertices){
        this.vertices = vertices;
        QueueGenerateBuffers();
    }


    public Vector3f[] getVertices(){
        return vertices;
    }

    @Override
    protected float[] dataConversion(){
        float[] data = new float[vertices.length * 3];
        for(int i = 0; i < vertices.length; i++){
            data[i * 3] = vertices[i].x;
            data[i * 3 + 1] = vertices[i].y;
            data[i * 3 + 2] = vertices[i].z;
        }
     return data;
    }
}
