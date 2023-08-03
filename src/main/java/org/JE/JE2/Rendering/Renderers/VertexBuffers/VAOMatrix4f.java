package org.JE.JE2.Rendering.Renderers.VertexBuffers;

import org.joml.Matrix4f;

public class VAOMatrix4f extends VAO {
    protected Matrix4f matrix = new Matrix4f();

    public VAOMatrix4f() {
        dataSize = 16;
        QueueGenerateBuffers();
    }

    public VAOMatrix4f(Matrix4f matrix) {
        this.matrix = matrix;
        this.data = dataConversion();
        dataSize = 3;
        QueueGenerateBuffers();
    }

    public VAOMatrix4f(VAO2f vao) {
        this.matrix = new Matrix4f();
        this.data = dataConversion();
        dataSize = 3;
        QueueGenerateBuffers();
    }

    public void setMatrix(Matrix4f matrix) {
        this.matrix = matrix;
        QueueGenerateBuffers();
    }

    public Matrix4f getMatrix() {
        return matrix;
    }

    @Override
    protected float[] dataConversion() {
        float[] data = new float[16];
        matrix.get(data);
        return data;
    }
}
