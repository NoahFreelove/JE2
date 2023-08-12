package org.JE.JE2.Rendering.Renderers.VertexBuffers;

import org.JE.JE2.Annotations.GLThread;
import org.JE.JE2.Manager;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL20;

import java.io.Serializable;
import java.nio.FloatBuffer;

import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;

public class VAO implements Serializable {
    protected float[] data = {};
    protected transient int vertexBufferID = -1;
    protected transient int location;

    protected int dataSize = 1;

    public VAO(){
        QueueGenerateBuffers();
    }

    public VAO(float[] data){
        this.data = data;
        QueueGenerateBuffers();
    }


    public VAO(float[] data, int pregeneratedBuffer){
        this.data = data;
        if(pregeneratedBuffer > 0)
            vertexBufferID = pregeneratedBuffer;

    }

    public VAO(VAO2f vao)
    {
        this(vao.dataConversion());
    }

    protected float[] dataConversion(){
        return data;
    }

    protected void QueueGenerateBuffers(){
        Runnable r = this::GenerateBuffers;
        Manager.queueGLFunction(r);
    }

    protected void QueueChangeBuffer(){
        Runnable r = this::GenerateBuffers;
        Manager.queueGLFunction(r);
    }

    @GLThread
    private int GenerateBuffers(){
        Disable();
        vertexBufferID = glGenBuffers();
        changeBuffer();
        return vertexBufferID;
    }

    @GLThread
    protected void changeBuffer(){
        this.data = dataConversion();
        glBindBuffer(GL_ARRAY_BUFFER, vertexBufferID);
        FloatBuffer fb = BufferUtils.createFloatBuffer(data.length * dataSize);
        for (float v : data) {
            fb.put(v);
        }
        fb.flip();
        glBufferData(GL_ARRAY_BUFFER, fb, GL_STATIC_DRAW);

        glBindBuffer(GL_ARRAY_BUFFER, 0);
    }

    @GLThread
    public void setDataNow(float[] data){
        this.data = data;
        GenerateBuffers();
    }

    @GLThread
    public boolean Enable(int location){
        if(vertexBufferID == -1){
            System.out.println("regenerating buffer: " + GenerateBuffers());
        }

        if(vertexBufferID == -1){
            return false;
        }
        this.location = location;
        glEnableVertexAttribArray(location);
        glBindBuffer(GL20.GL_ARRAY_BUFFER, vertexBufferID);
        glVertexAttribPointer(location, dataSize, GL_FLOAT, true, 0, 0);
        return true;
    }

    @GLThread
    public void Disable(){
        glDisableVertexAttribArray(location);
    }

    public void setData(float[] data){
        this.data = data;
        QueueChangeBuffer();
    }
    public float[] getData(){
        return data;
    }

    public void destroy() {
        glDeleteBuffers(vertexBufferID);
    }


    public int getDataSize() {
        return dataSize;
    }

    public int getVertexBufferID() {
        return vertexBufferID;
    }
}
