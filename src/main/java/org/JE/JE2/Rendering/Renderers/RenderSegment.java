package org.JE.JE2.Rendering.Renderers;

import org.JE.JE2.Objects.Scripts.Transform;
import org.JE.JE2.Rendering.Renderers.VertexBuffers.VAO;

public class RenderSegment {
    private VAO vao;
    private Transform relativeTransform;
    private int drawMode;
    private boolean wireframe = false;
    private int lightLayer;
    private int additionalBufferSize;
    private boolean scale = true;

    public RenderSegment(VAO vao, Transform relativeTransform, int drawMode) {
        this.vao = vao;
        this.relativeTransform = relativeTransform;
        this.drawMode = drawMode;
    }


    public VAO getVao() {
        return vao;
    }

    public Transform getRelativeTransform() {
        return relativeTransform;
    }

    public int getDrawMode() {
        return drawMode;
    }

    public boolean isWireframe() {
        return wireframe;
    }

    public int getLightLayer() {
        return lightLayer;
    }

    public int getAdditionalBufferSize() {
        return additionalBufferSize;
    }

    public void setVao(VAO vao) {
        this.vao = vao;
    }

    public void setRelativeTransform(Transform relativeTransform) {
        this.relativeTransform = relativeTransform;
    }

    public void setDrawMode(int drawMode) {
        this.drawMode = drawMode;
    }

    public void setWireframe(boolean wireframe) {
        this.wireframe = wireframe;
    }

    public void setLightLayer(int lightLayer) {
        this.lightLayer = lightLayer;
    }

    public void setAdditionalBufferSize(int additionalBufferSize) {
        this.additionalBufferSize = additionalBufferSize;
    }

    public boolean scale() {
        return scale;
    }

    public void setScale(boolean scale) {
        this.scale = scale;
    }
}
