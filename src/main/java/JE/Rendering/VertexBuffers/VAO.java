package JE.Rendering.VertexBuffers;

import JE.Annotations.GLThread;
import JE.Manager;
import JE.Rendering.Shaders.ShaderProgram;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL20;

import java.nio.FloatBuffer;

import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL15.GL_STATIC_DRAW;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL20.glDisableVertexAttribArray;

public class VAO {
    protected float[] data;
    protected int vertexBufferID = 0;
    protected int location;

    public ShaderProgram shaderProgram = new ShaderProgram();
    protected int dataSize = 1;

    public VAO(){
        QueueGenerateBuffers();
    }

    public VAO(float[] data){
        this.data = data;
        QueueGenerateBuffers();
    }
    public VAO(float[] data, ShaderProgram sp){
        this.data = data;
        this.shaderProgram = sp;
        QueueGenerateBuffers();
    }

    protected float[] dataConversion(){
        return data;
    }

    protected void QueueGenerateBuffers(){
        Runnable r = this::GenerateBuffers;
        Manager.QueueGLFunction(r);
    }

    @GLThread
    private void GenerateBuffers(){
        this.data = dataConversion();
        vertexBufferID = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vertexBufferID);
        FloatBuffer fb = BufferUtils.createFloatBuffer(data.length * dataSize);
        for (float v : data) {
            fb.put(v);
        }
        fb.flip();
        glBufferData(GL_ARRAY_BUFFER, fb, GL_STATIC_DRAW);
    }

    @GLThread
    public void setDataNow(float[] data){
        this.data = data;
        GenerateBuffers();
    }

    @GLThread
    public void Enable(int location){
        this.location = location;
        glUseProgram(shaderProgram.programID);
        glEnableVertexAttribArray(location);
        glBindBuffer(GL20.GL_ARRAY_BUFFER, vertexBufferID);
        glVertexAttribPointer(location, dataSize, GL_FLOAT, false, 0, 0);
    }
    @GLThread
    public void Disable(){
        glDisableVertexAttribArray(location);
    }

    public void setData(float[] data){
        this.data = data;
        QueueGenerateBuffers();
    }
    public float[] getData(){
        return data;
    }

    public void setShaderProgram(ShaderProgram sp){
        this.shaderProgram = sp;
    }
}
