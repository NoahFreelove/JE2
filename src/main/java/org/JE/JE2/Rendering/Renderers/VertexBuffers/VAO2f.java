package org.JE.JE2.Rendering.Renderers.VertexBuffers;

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
    public VAO2f(Vector2f[] vertices, int pregeneratedBuffer){
        this.vertices = vertices;
        this.data = dataConversion();
        dataSize = 2;
        if(pregeneratedBuffer > 0)
            vertexBufferID = pregeneratedBuffer;
    }
   public VAO2f(VAO2f vao){
        this.vertices = new Vector2f[vao.getVertices().length];
       for (int i = 0; i < vao.getVertices().length; i++) {
           this.vertices[i] = new Vector2f(vao.getVertices()[i]);
       }
        this.data = dataConversion();
        dataSize = 2;
        this.vertexBufferID = vao.vertexBufferID;
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
        QueueChangeBuffer();
    }


    public Vector2f[] getVertices(){
        return vertices;
    }

    @Override
    protected float[] dataConversion(){
        return vec2fToFloat(vertices);
    }

    public static float[] vec2fToFloat(Vector2f[] arr){
        float[] data = new float[arr.length * 2];
        for(int i = 0; i < arr.length; i++){
            data[i*2] = arr[i].x;
            data[i*2+1] = arr[i].y;
        }
        return data;
    }

    public String vertsToString(){
        StringBuilder sb = new StringBuilder();
        for (Vector2f v : vertices) {
            sb.append(v.x).append(",").append(v.y).append(",");
        }
        return sb.toString();
    }
    public static Vector2f[] stringToVerts(String s){
        String[] split = s.split(",");
        Vector2f[] verts = new Vector2f[split.length/2];
        for (int i = 0; i < split.length; i+=2) {
            verts[i/2] = new Vector2f(Float.parseFloat(split[i]),Float.parseFloat(split[i+1]));
        }
        return verts;
    }
}
