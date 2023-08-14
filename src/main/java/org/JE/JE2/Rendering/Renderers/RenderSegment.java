package org.JE.JE2.Rendering.Renderers;

import org.JE.JE2.Objects.Scripts.Transform;
import org.JE.JE2.Rendering.Renderers.VertexBuffers.VAO2f;

public class RenderSegment {
    protected VAO2f vao;
    private Transform relativeTransform;
    private int drawMode;
    private boolean wireframe = false;
    private int lightLayer;
    private int additionalBufferSize;
    private boolean scale = true;
    private boolean active = true;
    private boolean renderDistance = true;
    protected boolean refuseDestroy = false;

    public RenderSegment(VAO2f vao, Transform relativeTransform, int drawMode) {
        this.vao = vao;
        this.relativeTransform = relativeTransform;
        this.drawMode = drawMode;
    }

    public VAO2f getVao() {
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

    public void setVao(VAO2f vao) {
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

    public boolean isScale() {
        return scale;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public boolean usesRenderDistance() {
        return renderDistance;
    }

    public void setRenderDistance(boolean renderDistance) {
        this.renderDistance = renderDistance;
    }

    public boolean useRenderDistance() {
        return renderDistance;
    }

    public boolean refuseDestroy() {
        return refuseDestroy;
    }

    public void setRefuseDestroy(boolean refuseDestroy) {
        this.refuseDestroy = refuseDestroy;
    }

    public void destroy() {
        if(refuseDestroy())
            return;
        getVao().destroy();
    }

    public static Transform[] getTransforms(RenderSegment[] segs)
    {
        Transform[] transforms = new Transform[segs.length];
        for (int i = 0; i < segs.length; i++) {
            transforms[i] = segs[i].getRelativeTransform();
        }
        return transforms;
    }
}
